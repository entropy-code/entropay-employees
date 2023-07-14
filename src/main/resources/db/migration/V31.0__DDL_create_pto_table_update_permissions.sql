CREATE TABLE pto (
    id           UUID                        NOT NULL,
    created_at   TIMESTAMP					 NOT NULL,
    modified_at  TIMESTAMP 					 NOT NULL,
    deleted      BOOLEAN                     NOT NULL,
    start_date   TIMESTAMP 					 NOT NULL,
    end_date     TIMESTAMP 					 NOT NULL,
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
    ADD CONSTRAINT FK_PTO_ON_PTO_TYPE FOREIGN KEY (leave_type_id) REFERENCES leave_type(id)
;

--ROLE MANAGER_HR permissions
UPDATE config SET permissions='[
  {"entity": "employees", "actions": ["create", "read", "update", "delete"]},
  {"entity": "contracts", "actions": ["create", "read", "update", "delete"]},
  {"entity": "assignments", "actions": ["create", "read", "update", "delete"]},
  {"entity": "clients", "actions": ["create", "read", "update", "delete"]},
  {"entity": "vacations", "actions": ["create", "read", "update", "delete"]},
  {"entity": "ptos", "actions": ["create", "read", "update", "delete"]}
]'::json, menu='[
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
  },
  {
    "name": "Ptos",
    "href": "/#/ptos",
    "icon": "ptos",
    "key": 5
  }
]'::json, modified_at='2022-12-28 15:19:29.592' WHERE id='b1bf3a8c-ced5-443f-90a3-ee9d283a18c0'::uuid;


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
]'::json, menu='[
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
  },
  {
    "name": "Ptos",
    "href": "/#/ptos",
    "icon": "ptos",
    "key": 5
  },
  {
    "name": "Settings",
    "icon": "settings",
    "key": 6,
    "submenu": [
      {
        "name": "Companies",
        "href": "/#/companies",
        "key": 61
      },
      {
        "name": "Countries",
        "href": "/#/countries",
        "key": 62
      },
      {
        "name": "Holidays",
        "href": "/#/holidays",
        "key": 63
      },
      {
        "name": "Leave Types",
        "href": "/#/leave-types",
        "key": 64
      },
      {
        "name": "Projects",
        "href": "/#/projects",
        "key": 65
      },
      {
        "name": "Project types",
        "href": "/#/project-types",
        "key": 66
      },
      {
        "name": "Roles",
        "href": "/#/roles",
        "key": 67
      },
      {
        "name": "Seniorities",
        "href": "/#/seniorities",
        "key": 68
      },
      {
        "name": "Technologies",
        "href": "/#/technologies",
        "key": 69
      },
      {
        "name": "Tenants",
        "href": "/#/tenants",
        "key": 611
      }
    ]
  }
]'::json, modified_at='2023-05-09 11:00:00.000' WHERE id='d172386e-46b3-4c6f-b030-14b9204ea059'::uuid;


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
]'::json, menu='[
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
  },
  {
    "name": "Ptos",
    "href": "/#/ptos",
    "icon": "ptos",
    "key": 5
  },
  {
    "name": "Settings",
    "icon": "settings",
    "key": 6,
    "submenu": [
      {
        "name": "Companies",
        "href": "/#/companies",
        "key": 61
      },
      {
        "name": "Projects",
        "href": "/#/projects",
        "key": 62
      },
      {
        "name": "Project types",
        "href": "/#/project-types",
        "key": 63
      },
      {
        "name": "Roles",
        "href": "/#/roles",
        "key": 64
      },
      {
        "name": "Seniorities",
        "href": "/#/seniorities",
        "key": 65
      },
      {
        "name": "Technologies",
        "href": "/#/technologies",
        "key": 66
      },
      {
        "name": "Tenants",
        "href": "/#/tenants",
        "key": 67
      }
    ]
  }
]'::json,
modified_at='2022-12-28 15:19:29.000' WHERE id='3c245af9-19fc-4e02-b9b0-a85e8ed06594'::uuid;

