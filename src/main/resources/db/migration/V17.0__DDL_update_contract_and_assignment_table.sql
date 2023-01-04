ALTER TABLE contract
    RENAME COLUMN hours_per_week TO hours_per_month;

ALTER TABLE assignment
    RENAME COLUMN hours_per_week TO hours_per_month;