CREATE TABLE IF NOT EXISTS clients (
    id                 UUID                        NOT NULL,
    name               VARCHAR(255)                NOT NULL,
    address            VARCHAR(255),
    contact            VARCHAR(255),
    preferred_currency VARCHAR(255)                NOT NULL,
    created_on         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modified_on        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    is_active          BOOLEAN                     NOT NULL,
    CONSTRAINT pk_clients PRIMARY KEY (id)
)
;

ALTER TABLE clients
    ADD CONSTRAINT uc_clients_name UNIQUE (name)
;