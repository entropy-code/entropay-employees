ALTER TABLE employees.clients
    RENAME COLUMN created_on TO created_at;

ALTER TABLE employees.clients
    RENAME COLUMN modified_on TO modified_at;

ALTER TABLE employees.clients
    RENAME COLUMN is_active TO deleted;

ALTER TABLE employees.clients
    ALTER COLUMN deleted SET DEFAULT FALSE;

alter table employees.clients
    rename to client;

