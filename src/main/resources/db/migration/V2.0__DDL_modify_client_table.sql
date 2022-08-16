ALTER TABLE clients
    RENAME COLUMN created_on TO created_at;

ALTER TABLE clients
    RENAME COLUMN modified_on TO modified_at;

ALTER TABLE clients
    RENAME COLUMN is_active TO deleted;

ALTER TABLE clients
    ALTER COLUMN deleted SET DEFAULT FALSE;

alter table clients
    rename to client;

