UPDATE public.config
SET permissions='[
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
]'::json,menu='[
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
]'::json
WHERE id='3c245af9-19fc-4e02-b9b0-a85e8ed06594'::uuid::uuid;
