CREATE TABLE tenant (
    id           UUID                        NOT NULL,
    name         VARCHAR(255),
    display_name VARCHAR(255),
    created_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modified_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    deleted      BOOLEAN                     NOT NULL
)
;

alter table tenant
    add constraint pk_tenant
        primary key (id)
;

CREATE TABLE company (
    id           UUID                        NOT NULL,
    tenant_id    UUID                        NOT NULL,
    name         VARCHAR(255),
    address_line VARCHAR(255),
    zip_code     VARCHAR(50),
    city         VARCHAR(100),
    state        VARCHAR(100),
    country      VARCHAR(100),
    created_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modified_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    deleted      BOOLEAN                     NOT NULL
)
;

alter table company
    add constraint pk_company
        primary key (id)
;

alter table company
    add constraint tenant_id_fk
        foreign key (tenant_id) references tenant(id)
;

alter table client
    rename column address to address_line
;

INSERT INTO tenant
SELECT '8a6c45de-a257-4f07-95e1-9e163f71f2f1', 'entropy', 'Entropy Team', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE
;

INSERT into company
SELECT 'c671d478-aefb-407c-be73-af5bf78db18b', (SELECT id from tenant where name = 'entropy'), 'EntropyLLC', '2035 Sunset Lake Road, Suite b-2'
    , '19702', 'Newark', 'Delaware', 'United States', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE
;

alter table client
    add company_id uuid
;

alter table client
    add constraint company_fk
        foreign key (company_id) references company(id)
;

UPDATE client
SET company_id = (SELECT c.id
                  FROM company c
                       JOIN tenant t
                            ON t.id = c.tenant_id
                  WHERE t.name = 'entropy')
;

alter table client
    alter column company_id set not null
;

alter table employee
    add company_id uuid;

alter table employee
    add constraint employee_company_fk
        foreign key (company_id) references company(id)
;

UPDATE employee
SET company_id = (SELECT c.id
                  FROM company c
                       JOIN tenant t
                            ON t.id = c.tenant_id
                  WHERE t.name = 'entropy')
;

alter table employee
    alter column company_id set not null
;

alter table employee
    add internal_id varchar(255);


