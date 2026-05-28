# MCP read-only tools — end-to-end validation runbook

Run this before the stacked PR series (foundation + Employee 360 + Time off + Reimbursements + Reports) is promoted from draft to ready-for-review. The parent story marks the end-to-end validation as **bloqueante**: the sensitive-masking pipeline must be confirmed against a real MCP client with a non-admin token, not only through the in-process tool tests.

The unit tests in this series invoke each tool through Spring AI's own `MethodToolCallback` (the exact path the production MCP server uses to serialize tool results before sending them to the client), so the test path is faithful — but it skips the transport layer, the JWT-to-roles resolution against `entropay-users-auth`, and the real Cognito user pool. This runbook closes that loop.

## Prerequisites

- AWS SSO refreshed: `aws sso login --profile entropy-prod` (or `entropy-dev` if validating dev).
- Local stack reachable from your machine.
  - PostgreSQL running (Docker Compose covers this).
  - `entropay-users-auth` running on `:8000` (needs Cognito config for the env you're validating).
  - `entropay-employees` running on `:8100` with the stacked branch checked out.
- A test Cognito user in each role you want to exercise. The five platform roles:
  - `ADMIN`
  - `HR_DIRECTOR`
  - `MANAGER_HR`
  - `ANALYST`
  - `DEVELOPMENT`
- An MCP client. Either:
  - **Claude Desktop** with the MCP server configured (Settings → Developer → Edit Config), or
  - **MCP Inspector** (`npx @modelcontextprotocol/inspector`), or
  - **`curl`** against `/mcp/sse` if you want a quick smoke without a real client.

## Bringing up the stack

```bash
cd repos/entropay-employees
make setup           # one-time: install deps + start Postgres
make run             # boots Spring Boot on :8100 with the MCP endpoint at /mcp/sse
```

The MCP endpoint advertises via the Protected Resource Metadata at `http://localhost:8100/.well-known/oauth-protected-resource`.

## 1. Discovery — the MCP client sees every tool

Connect your MCP client to `http://localhost:8100/mcp/sse`. The client should successfully discover **13 tools** advertised by name:

- `list_roster`
- `get_employee`
- `get_employee_summary`
- `list_employee_assignments`
- `list_employee_feedbacks`
- `get_vacation_balance`
- `list_employee_ptos`
- `list_upcoming_ptos`
- `list_reimbursements`
- `get_turnover_report`
- `get_billing_report`
- `get_margin_report`
- `get_salaries_report`

This matches the `McpToolDiscoveryTest` snapshot. If the count differs, investigate before continuing.

## 2. Sensitive masking — the bloqueante check

For each non-admin role, sign in to the MCP client with that role's token and run the listed tool. Capture the response (or a screenshot) for the PR description.

The masking rule is: **non-admin caller viewing an internal employee → the field returns `null`. Admin caller OR external employee → the raw value is returned.**

### 2a — list_employee_assignments (one tool per domain)

```
> list_employee_assignments for <internal employee UUID>
```

Expected for `HR_DIRECTOR`, `MANAGER_HR`, `ANALYST`, `DEVELOPMENT`:
- `billableRate` is `null` for every entry.

Expected for `ADMIN`:
- `billableRate` is the raw number.

### 2b — get_employee_summary

```
> get_employee_summary <internal employee UUID or name>
```

Expected for `HR_DIRECTOR`, `MANAGER_HR`, `ANALYST`, `DEVELOPMENT`:
- `currentRate` is `null`.
- `currentSalary` is `null`.
- All other fields (profile, vacation, feedbacks, reimbursements) are present.

Expected for `ADMIN`:
- `currentRate` and `currentSalary` are the raw numbers.

### 2c — get_salaries_report

```
> get_salaries_report
```

Expected for `HR_DIRECTOR`, `MANAGER_HR` (the two non-admin roles allowed on this tool):
- Every internal-employee row has `salary` = `null`.
- External-employee rows (rare) keep their raw `salary`.

Expected for `ADMIN`:
- All salaries are the raw numbers.

### 2d — get_billing_report and get_margin_report

These are `ADMIN`-only at the service layer, so non-admin tokens should receive an authorization error rather than a payload. Verify the error response is clear ("access denied" or similar) and **does not** leak rows.

For `ADMIN` only: verify the raw `rate`, `total`, `paid`, `margin` numbers are returned.

## 3. Authorization — calling an unauthorized tool returns a clear error

Sign in as `ANALYST` (intentionally excluded from Reimbursements in the UI and excluded from `get_billing_report`/`get_margin_report`/`get_vacation_balance`). Run each of those tools.

Expected: the MCP client surfaces a clear authorization error. No partial data, no empty list.

## 4. External-employee masking sanity check

Sign in as any non-admin role and call `get_employee_summary` for an **external** employee (one not in `EmployeeService.getInternalEmployeeIds()`). Expected: `currentRate` and `currentSalary` come back as the raw numbers — proving the masking is correctly scoped to internal employees only.

## Capturing the validation

For each step above, attach the request and the relevant response excerpt to the parent PR (`feat/mcp-tools-reports`). The PR cannot be promoted from draft to ready-for-review until at least one tool per domain has been exercised with a non-admin token against the real MCP transport.

If any step fails:

1. Capture the discrepancy.
2. Open a follow-up before resolving the draft, even if it means stacking another PR onto the series.
3. Re-run the affected step after the fix.

## Where to look if something doesn't add up

- Discovery count mismatch → check `McpServerConfig.mcpToolCallbackProvider` matches the snapshot in `McpToolDiscoveryTest`.
- Masking didn't fire → `SensitiveInformationSerializer` must resolve `SensitiveInformationService` per-call (regression check from PR 1).
- Authorization errors come back empty → verify `@Secured` is on each query-service method, not on the tool method.
- Role doesn't resolve correctly → `McpJwtAuthenticationConverter` calls `entropay-users-auth /auth/identity`; check the gateway is up and the Cognito user has the right group.

— claude-opus-4-7
