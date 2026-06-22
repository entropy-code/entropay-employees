-- Grant USAGE, SELECT, and UPDATE on employee_internal_id_seq to every role that
-- already has SELECT/INSERT/UPDATE privileges on the employee table.
--
-- Why: in environments where Flyway runs as a privileged role and the application
-- connects as a separate, less-privileged role (the standard RDS setup on dev/prod),
-- new objects created by Flyway are owned by the Flyway role and the app role gets
-- no implicit privileges. Tables in this codebase happen to have working grants from
-- baseline setup, but sequences are a separate object class and were not covered,
-- which broke `GET /employees/next-internal-id` and any future `nextval` call from
-- the app on dev.
--
-- Mirroring the employee table's grantees onto the sequence keeps the fix
-- environment-agnostic (no hard-coded role names) and idempotent — re-running the
-- block is safe because GRANT is additive.
DO $$
DECLARE
    r RECORD;
BEGIN
    FOR r IN
        SELECT DISTINCT grantee
        FROM information_schema.table_privileges
        WHERE table_schema = current_schema()
          AND table_name = 'employee'
          AND privilege_type IN ('SELECT', 'INSERT', 'UPDATE')
          AND grantee <> 'PUBLIC'
    LOOP
        EXECUTE format(
            'GRANT USAGE, SELECT, UPDATE ON SEQUENCE employee_internal_id_seq TO %I',
            r.grantee
        );
    END LOOP;
END $$;
