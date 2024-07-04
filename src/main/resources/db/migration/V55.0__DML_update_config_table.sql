-- added the salaries report to the menu and permissions for the admin role

UPDATE public.config SET permissions = e'[
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
  {"entity": "ptos", "actions": ["create", "read", "update", "delete"]},
  {"entity": "reports/employees", "actions": ["create", "read", "update", "delete"]},
  {"entity": "reports/ptos/employees", "actions": ["create", "read", "update", "delete"]},
  {"entity": "reports/salaries", "actions": ["create", "read", "update", "delete"]}
]', menu = e'[
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
  },
  {
    "name": "Reports",
    "icon": "reports",
    "key": 7,
    "submenu": [
      {
        "name": "Employees",
        "href": "/#/reports/employees",
        "key": 71
      },
      {
        "name": "PTOs",
        "href": "/#/reports/ptos/employees",
        "key": 72
      },
      {
        "name": "Salaries",
        "href": "/#/reports/salaries",
        "key": 73
      }
    ]
  }
]', modified_at = CURRENT_TIMESTAMP WHERE role = 'ROLE_ADMIN';
