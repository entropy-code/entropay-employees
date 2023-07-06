CREATE TABLE pto (
    id           UUID                        NOT NULL,
    created_at   TIMESTAMP					 NOT NULL,
    modified_at  TIMESTAMP 					 NOT NULL,
    deleted      BOOLEAN                     NOT NULL,
    from_date    TIMESTAMP 					 NOT NULL,
    "to_date"    TIMESTAMP 					 NOT NULL,
    status       VARCHAR(50)                 NOT NULL,
    details      VARCHAR(2000),
    days         INTEGER,
    labour_hours INTEGER,
    employee_id  UUID,
    leave_type_id     UUID,
    CONSTRAINT pk_pto PRIMARY KEY (id)
)
;

ALTER TABLE pto
    ADD CONSTRAINT FK_PTO_ON_EMPLOYEE FOREIGN KEY (employee_id) REFERENCES employee(id)
;

ALTER TABLE pto
    ADD CONSTRAINT FK_PTO_ON_PTO_TYPE FOREIGN KEY (pto_type) REFERENCES leave_type(id)
;

--ROLE MANAGER_HR permissions
UPDATE config SET permissions='[
  {"entity": "employees", "actions": ["create", "read", "update", "delete"]},
  {"entity": "contracts", "actions": ["create", "read", "update", "delete"]},
  {"entity": "assignments", "actions": ["create", "read", "update", "delete"]},
  {"entity": "clients", "actions": ["create", "read", "update", "delete"]},
  {"entity": "vacations", "actions": ["create", "read", "update", "delete"]},
  {"entity": "ptos", "actions": ["create", "read", "update", "delete"]}
]'::json::json WHERE id='b1bf3a8c-ced5-443f-90a3-ee9d283a18c0'::uuid;

--ROLE ADMIN permissions
UPDATE config SET permissions='[
  {"entity": "employees", "actions": ["create", "read", "update", "delete"]},
  {"entity": "contracts", "actions": ["create", "read", "update", "delete"]},
  {"entity": "assignments", "actions": ["create", "read", "update", "delete"]},
  {"entity": "clients", "actions": ["create", "read", "update", "delete"]},
  {"entity": "companies", "actions": ["create", "read", "update", "delete"]},
  {"entity": "projects", "actions": ["create", "read", "update", "delete"]},
  {"entity": "project-types", "actions": ["create", "read", "update", "delete"]},
  {"entity": "roles", "actions": ["create", "read", "update", "delete"]},
  {"entity": "seniorities", "actions": ["create", "read", "update", "delete"]},
  {"entity": "technologies", "actions": ["create", "read", "update", "delete"]},
  {"entity": "tenants", "actions": ["create", "read", "update", "delete"]},
  {"entity": "leave-types", "actions": ["create", "read", "update", "delete"]},
  {"entity": "holidays", "actions": ["create", "read", "update", "delete"]},
  {"entity": "countries", "actions": ["create", "read", "update", "delete"]},
  {"entity": "vacations", "actions": ["create", "read", "update", "delete"]},
  {"entity": "ptos", "actions": ["create", "read", "update", "delete"]}
]'::json::json WHERE id='d172386e-46b3-4c6f-b030-14b9204ea059'::uuid;

--ROLE DEVELOPMENT permissions
UPDATE config SET permissions='[
  {"entity": "employees", "actions": ["create", "read", "update", "delete"]},
  {"entity": "contracts", "actions": ["create", "read", "update", "delete"]},
  {"entity": "assignments", "actions": ["create", "read", "update", "delete"]},
  {"entity": "clients", "actions": ["create", "read", "update", "delete"]},
  {"entity": "companies", "actions": ["create", "read", "update", "delete"]},
  {"entity": "projects", "actions": ["create", "read", "update", "delete"]},
  {"entity": "project-types", "actions": ["create", "read", "update", "delete"]},
  {"entity": "roles", "actions": ["create", "read", "update", "delete"]},
  {"entity": "seniorities", "actions": ["create", "read", "update", "delete"]},
  {"entity": "technologies", "actions": ["create", "read", "update", "delete"]},
  {"entity": "tenants", "actions": ["create", "read", "update", "delete"]},
  {"entity": "vacations", "actions": ["create", "read", "update", "delete"]},
  {"entity": "ptos", "actions": ["create", "read", "update", "delete"]}
]'::json::json WHERE id='3c245af9-19fc-4e02-b9b0-a85e8ed06594'::uuid;
