ALTER TABLE client
    RENAME COLUMN contact TO contact_full_name;

ALTER TABLE client
    ADD contact_email varchar(255) NULL;