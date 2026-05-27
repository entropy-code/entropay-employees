-- Backing sequence for auto-generated Employee.internal_id values.
-- Initialized so that the next value is one greater than the highest historical
-- numeric suffix found in the employee table (including soft-deleted rows),
-- guaranteeing that internal IDs are never reused.
CREATE SEQUENCE IF NOT EXISTS employee_internal_id_seq;

SELECT setval(
    'employee_internal_id_seq',
    COALESCE(
        (SELECT MAX(SUBSTRING(internal_id FROM 2)::int)
         FROM employee
         WHERE internal_id ~ '^E[0-9]+$'),
        0
    ) + 1,
    false
);
