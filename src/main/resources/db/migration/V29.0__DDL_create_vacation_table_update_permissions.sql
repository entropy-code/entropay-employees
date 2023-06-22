
CREATE TABLE vacation
(
    id          uuid
        CONSTRAINT vacation_pk
            PRIMARY KEY,
    year        varchar(100) NOT NULL,
    credit      numeric      NULL,
    debit      numeric      NULL,
    employee_id  UUID NOT NULL,
    created_at  timestamp    NOT NULL,
    modified_at timestamp    NOT NULL,
    deleted     boolean      NOT NULL,
    CONSTRAINT vacation_fk FOREIGN KEY (employee_id) REFERENCES employee(id)
)
;

UPDATE config SET "role"='ROLE_MANAGER_HR', permissions='[
  {"entity": "employees", "actions": ["create", "read", "update", "delete"]},
  {"entity": "contracts", "actions": ["create", "read", "update", "delete"]},
  {"entity": "assignments", "actions": ["create", "read", "update", "delete"]},
  {"entity": "clients", "actions": ["create", "read", "update", "delete"]},
  {"entity": "vacations", "actions": ["create", "read", "update", "delete"]}
]'::json::json, menu='[
  {
    "name": "Employees",
    "href": "/#/employees",
    "icon": "employees",
    "key": 1
  },
  {
    "name": "Contracts",
    "href": "/#/contracts",
    "icon": "contracts",
    "key": 2
  },
  {
    "name": "Assignments",
    "href": "/#/assignments",
    "icon": "assignments",
    "key": 3
  },
  {
    "name": "Clients",
    "href": "/#/clients",
    "icon": "clients",
    "key": 4
  }
]'::json::json, created_at='2022-12-28 15:19:29.592', modified_at='2022-12-28 15:19:29.592', deleted=false WHERE id='b1bf3a8c-ced5-443f-90a3-ee9d283a18c0'::uuid;