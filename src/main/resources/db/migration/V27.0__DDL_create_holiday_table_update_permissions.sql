CREATE TABLE country
(
  id          uuid
    CONSTRAINT country_pk
      PRIMARY KEY,
  name        varchar(100) NOT NULL,
  created_at  timestamp    NOT NULL,
  modified_at timestamp    NOT NULL,
  deleted     boolean      NOT NULL
)
;

CREATE TABLE holiday_calendar
(
  id uuid CONSTRAINT holiday_pk PRIMARY KEY,
  country_id  uuid NOT NULL,
  date  timestamp    NOT NULL,
  description varchar(100) NOT NULL,
  created_at  timestamp    NOT NULL,
  modified_at timestamp    NOT NULL,
  deleted     boolean      NOT NULL,
  CONSTRAINT holiday_country_fk FOREIGN KEY (country_id) REFERENCES country(id)
)
;

UPDATE config SET "role"='ROLE_ADMIN', permissions='[
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
  {"entity": "countries", "actions": ["create", "read", "update", "delete"]}
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
        "name": "Countries",
        "href": "/#/countries",
        "key": 52
      },
      {
        "name": "Holidays",
        "href": "/#/holidays",
        "key": 53
      },
      {
        "name": "Leave Types",
        "href": "/#/leave-types",
        "key": 54
      },
      {
        "name": "Projects",
        "href": "/#/projects",
        "key": 55
      },
      {
        "name": "Project types",
        "href": "/#/project-types",
        "key": 56
      },
      {
        "name": "Roles",
        "href": "/#/roles",
        "key": 57
      },
      {
        "name": "Seniorities",
        "href": "/#/seniorities",
        "key": 58
      },
      {
        "name": "Technologies",
        "href": "/#/technologies",
        "key": 59
      },
      {
        "name": "Tenants",
        "href": "/#/tenants",
        "key": 511
      }
    ]
  }
]'::json::json, created_at='2022-12-28 15:19:29.000', modified_at='2023-05-09 11:00:00.000', deleted=false WHERE id='d172386e-46b3-4c6f-b030-14b9204ea059'::uuid;













