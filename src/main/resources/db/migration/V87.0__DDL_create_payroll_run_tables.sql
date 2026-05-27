-- Payroll run header: one per (period, attempt). Items are calculated per active employee in that period.
CREATE TABLE IF NOT EXISTS payroll_run (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    period DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    triggered_by_user_id UUID,
    triggered_by_email VARCHAR(255),
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    error_message VARCHAR(2000),
    total_amount NUMERIC(14, 2),
    employee_count INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- One non-deleted run per period (re-running deletes the prior DRAFT before insert).
CREATE UNIQUE INDEX IF NOT EXISTS idx_payroll_run_period_unique_active
    ON payroll_run(period) WHERE deleted = FALSE;

CREATE INDEX IF NOT EXISTS idx_payroll_run_status ON payroll_run(status);
CREATE INDEX IF NOT EXISTS idx_payroll_run_deleted ON payroll_run(deleted);

CREATE TABLE IF NOT EXISTS payroll_item (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    payroll_run_id UUID NOT NULL,
    employee_id UUID NOT NULL,
    contract_id UUID,
    client_name VARCHAR(255),
    payment_platform VARCHAR(100),
    modality VARCHAR(20),
    base_salary NUMERIC(14, 2) NOT NULL DEFAULT 0,
    proportional_salary NUMERIC(14, 2) NOT NULL DEFAULT 0,
    country_working_hours_in_month INTEGER,
    pto_hours_in_month NUMERIC(8, 2) NOT NULL DEFAULT 0,
    unpaid_leave_hours_in_month NUMERIC(8, 2) NOT NULL DEFAULT 0,
    unpaid_leave_deduction NUMERIC(14, 2) NOT NULL DEFAULT 0,
    billable_hours_in_month NUMERIC(8, 2) NOT NULL DEFAULT 0,
    overtime_hours NUMERIC(8, 2) NOT NULL DEFAULT 0,
    overtime_amount NUMERIC(14, 2) NOT NULL DEFAULT 0,
    reimbursements_amount NUMERIC(14, 2) NOT NULL DEFAULT 0,
    hardware_clawback NUMERIC(14, 2) NOT NULL DEFAULT 0,
    vacation_cashout NUMERIC(14, 2) NOT NULL DEFAULT 0,
    adjustment NUMERIC(14, 2) NOT NULL DEFAULT 0,
    previous_balance NUMERIC(14, 2) NOT NULL DEFAULT 0,
    notes VARCHAR(1000),
    total_amount NUMERIC(14, 2) NOT NULL DEFAULT 0,
    is_final_settlement BOOLEAN NOT NULL DEFAULT FALSE,
    calculation_error VARCHAR(2000),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_payroll_item_run FOREIGN KEY (payroll_run_id) REFERENCES payroll_run(id) ON DELETE CASCADE,
    CONSTRAINT fk_payroll_item_employee FOREIGN KEY (employee_id) REFERENCES employee(id),
    CONSTRAINT fk_payroll_item_contract FOREIGN KEY (contract_id) REFERENCES contract(id)
);

CREATE INDEX IF NOT EXISTS idx_payroll_item_run ON payroll_item(payroll_run_id);
CREATE INDEX IF NOT EXISTS idx_payroll_item_employee ON payroll_item(employee_id);
CREATE INDEX IF NOT EXISTS idx_payroll_item_deleted ON payroll_item(deleted);

-- Append payroll permissions for the three roles that should see the screen. Using jsonb || avoids
-- having to re-emit the entire permissions array (V82-style) and so won't clobber unrelated entries
-- added by other migrations or by hand.
UPDATE public.config
SET permissions = (permissions::jsonb || '[
    {"entity": "payroll-runs", "actions": ["create", "read", "update", "delete"]},
    {"entity": "payroll-items", "actions": ["read", "update"]}
]'::jsonb)::json
WHERE role IN ('ROLE_ADMIN', 'ROLE_MANAGER/HR', 'ROLE_HR_DIRECTOR');

-- Append the Payroll menu entry. Key 12 is past the highest current top-level key (11 = Skills under ADMIN).
UPDATE public.config
SET menu = (menu::jsonb || '[
    {
      "name": "Payroll",
      "href": "/#/payroll-runs",
      "icon": "payroll-runs",
      "key": 12
    }
]'::jsonb)::json
WHERE role IN ('ROLE_ADMIN', 'ROLE_MANAGER/HR', 'ROLE_HR_DIRECTOR');
