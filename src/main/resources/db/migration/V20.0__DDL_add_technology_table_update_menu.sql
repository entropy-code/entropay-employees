CREATE TABLE "technology" (
                            id uuid NOT NULL,
                            "name" varchar(100) NOT NULL,
                            created_at timestamp NOT NULL,
                            modified_at timestamp NOT NULL,
                            deleted bool NOT NULL,
                            CONSTRAINT pk_technology PRIMARY KEY (id),
                            CONSTRAINT uc_technology_name UNIQUE (name)
);

CREATE TABLE employee_technology(
                              employee_id  UUID NOT NULL,
                              technology_id UUID NOT NULL,
                              FOREIGN KEY (employee_id) REFERENCES employee(id),
                              FOREIGN KEY (technology_id) REFERENCES "technology"(id)
);

UPDATE config SET "role"='ROLE_ADMIN', permissions='[
  {"entity": "employees", "actions": ["create", "read", "update", "delete"]},
  {"entity": "contracts", "actions": ["create", "read", "update", "delete"]},
  {"entity": "assignments", "actions": ["create", "read", "update", "delete"]},
  {"entity": "clients", "actions": ["create", "read", "update", "delete"]}
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
  },
  {
    "name": "Settings",
    "icon": "settings",
    "key": 5,
    "submenu": [
      {
        "name": "Companies",
        "href": "/#/companies",
        "key": 51
      },
      {
        "name": "Projects",
        "href": "/#/projects",
        "key": 52
      },
      {
        "name": "Project types",
        "href": "/#/project-types",
        "key": 53
      },
      {
        "name": "Roles",
        "href": "/#/roles",
        "key": 54
      },
      {
        "name": "Seniorities",
        "href": "/#/seniorities",
        "key": 55
      },
      {
        "name": "Technologies",
        "href": "/#/technologies",
        "key": 56
      },
      {
        "name": "Tenants",
        "href": "/#/tenants",
        "key": 57
      }
    ]
  }
]'::json::json, created_at='2022-12-28 15:19:29.000', modified_at='2022-12-28 15:19:29.000', deleted=false WHERE id='d172386e-46b3-4c6f-b030-14b9204ea059'::uuid;

UPDATE config SET "role"='ROLE_DEVELOPMENT', permissions='[
  {"entity": "employees", "actions": ["create", "read", "update", "delete"]},
  {"entity": "contracts", "actions": ["create", "read", "update", "delete"]},
  {"entity": "assignments", "actions": ["create", "read", "update", "delete"]},
  {"entity": "clients", "actions": ["create", "read", "update", "delete"]}
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
  },
  {
    "name": "Settings",
    "icon": "settings",
    "key": 5,
    "submenu": [
      {
        "name": "Companies",
        "href": "/#/companies",
        "key": 51
      },
      {
        "name": "Projects",
        "href": "/#/projects",
        "key": 52
      },
      {
        "name": "Project types",
        "href": "/#/project-types",
        "key": 53
      },
      {
        "name": "Roles",
        "href": "/#/roles",
        "key": 54
      },
      {
        "name": "Seniorities",
        "href": "/#/seniorities",
        "key": 55
      },
      {
        "name": "Tenants",
        "href": "/#/tenants",
        "key": 56
      },
      {
        "name": "Technologies",
        "href": "/#/technologies",
        "key": 56
      },
      {
        "name": "Tenants",
        "href": "/#/tenants",
        "key": 57
      }
    ]
  }
]'::json::json, created_at='2022-12-28 15:19:29.000', modified_at='2022-12-28 15:19:29.000', deleted=false WHERE id='3c245af9-19fc-4e02-b9b0-a85e8ed06594'::uuid;