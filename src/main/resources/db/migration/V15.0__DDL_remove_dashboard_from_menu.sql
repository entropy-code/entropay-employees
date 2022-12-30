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
    "key": 2
  },
  {
    "name": "Clients",
    "href": "/#/clients",
    "icon": "clients",
    "key": 3
  },
  {
    "name": "Settings",
    "icon": "settings",
    "key": 4,
    "submenu": [
      {
        "name": "Companies",
        "href": "/#/companies",
        "key": 41
      },
      {
        "name": "Projects",
        "href": "/#/projects",
        "key": 42
      },
      {
        "name": "Project types",
        "href": "/#/project-types",
        "key": 43
      },
      {
        "name": "Roles",
        "href": "/#/roles",
        "key": 44
      },
      {
        "name": "Seniorities",
        "href": "/#/seniorities",
        "key": 45
      },
      {
        "name": "Tenants",
        "href": "/#/tenants",
        "key": 46
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
    "key": 2
  },
  {
    "name": "Clients",
    "href": "/#/clients",
    "icon": "clients",
    "key": 3
  },
  {
    "name": "Settings",
    "icon": "settings",
    "key": 4,
    "submenu": [
      {
        "name": "Companies",
        "href": "/#/companies",
        "key": 41
      },
      {
        "name": "Projects",
        "href": "/#/projects",
        "key": 42
      },
      {
        "name": "Project types",
        "href": "/#/project-types",
        "key": 43
      },
      {
        "name": "Roles",
        "href": "/#/roles",
        "key": 44
      },
      {
        "name": "Seniorities",
        "href": "/#/seniorities",
        "key": 45
      },
      {
        "name": "Tenants",
        "href": "/#/tenants",
        "key": 46
      }
    ]
  }
]'::json::json, created_at='2022-12-28 15:19:29.000', modified_at='2022-12-28 15:19:29.000', deleted=false WHERE id='3c245af9-19fc-4e02-b9b0-a85e8ed06594'::uuid;

UPDATE config SET "role"='ROLE_MANAGER_HR', permissions='[
  {"entity": "employees", "actions": ["create", "read", "update", "delete"]},
  {"entity": "contracts", "actions": ["create", "read", "update", "delete"]},
  {"entity": "assignments", "actions": ["create", "read", "update", "delete"]},
  {"entity": "clients", "actions": ["create", "read", "update", "delete"]}
]'::json::json, menu='[
  {
    "name": "Employees",
    "href": "/#/employees",
    "icon": "employees",
    "key": 2
  },
  {
    "name": "Clients",
    "href": "/#/clients",
    "icon": "clients",
    "key": 3
  }
]'::json::json, created_at='2022-12-28 15:19:29.592', modified_at='2022-12-28 15:19:29.592', deleted=false WHERE id='b1bf3a8c-ced5-443f-90a3-ee9d283a18c0'::uuid;


UPDATE config SET "role"='ROLE_ANALYST', permissions='[
  {"entity": "employees", "actions": ["read"]},
  {"entity": "contracts", "actions": ["read"]},
  {"entity": "assignments", "actions": ["read"]},
  {"entity": "clients", "actions": ["read"]}
]'::json::json, menu='[
  {
    "name": "Employees",
    "href": "/#/employees",
    "icon": "employees",
    "key": 2
  },
  {
    "name": "Clients",
    "href": "/#/clients",
    "icon": "clients",
    "key": 3
  }
]'::json::json, created_at='2022-12-28 15:19:29.000', modified_at='2022-12-28 15:19:29.000', deleted=false WHERE id='8fe02eb2-2d93-40c5-8ebe-1bbfa86c9456'::uuid;
