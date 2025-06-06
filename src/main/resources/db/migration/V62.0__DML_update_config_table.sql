--added end reasons to the settings menu

UPDATE public.config
SET menu='[
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
        "name": "End Reasons",
        "href": "/#/end-reasons",
        "key": 610
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
      }
    ]
  },
  {
    "name": "Overtimes",
    "href": "/#/overtimes",
    "icon": "overtimes",
    "key": 8
  }
]'::json,permissions='[
  {"entity": "employees", "actions": ["create", "read", "update"]},
  {"entity": "contracts", "actions": ["create", "read", "update", "delete"]},
  {"entity": "assignments", "actions": ["create", "read", "update", "delete"]},
  {"entity": "clients", "actions": ["create", "read", "update"]},
  {"entity": "companies", "actions": ["create", "read", "update", "delete"]},
  {"entity": "projects", "actions": ["create", "read", "update"]},
  {"entity": "project-types", "actions": ["create", "read", "update", "delete"]},
  {"entity": "roles", "actions": ["create", "read", "update", "delete"]},
  {"entity": "seniorities", "actions": ["create", "read", "update", "delete"]},
  {"entity": "technologies", "actions": ["create", "read", "update", "delete"]},
  {"entity": "end-reasons", "actions": ["create", "read", "update", "delete"]},
  {"entity": "tenants", "actions": ["create", "read", "update", "delete"]},
  {"entity": "leave-types", "actions": ["create", "read", "update", "delete"]},
  {"entity": "holidays", "actions": ["create", "read", "update", "delete"]},
  {"entity": "countries", "actions": ["create", "read", "update", "delete"]},
  {"entity": "vacations", "actions": ["create", "read", "update", "delete"]},
  {"entity": "ptos", "actions": ["create", "read", "update", "delete"]},
  {"entity": "reports/employees", "actions": ["create", "read", "update", "delete"]},
  {"entity": "reports/ptos/employees", "actions": ["create", "read", "update", "delete"]},
  {"entity": "overtimes", "actions": ["create", "read", "update", "delete"]}
]'::json
WHERE id='b1bf3a8c-ced5-443f-90a3-ee9d283a18c0'::uuid::uuid; --manager hr role
UPDATE public.config
SET menu='[
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
        "name": "End Reasons",
        "href": "/#/end-reasons",
        "key": 610
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
      }
    ]
  },
  {
    "name": "Overtimes",
    "href": "/#/overtimes",
    "icon": "overtimes",
    "key": 8
  }
]'::json,permissions='[
  {"entity": "employees", "actions": ["create", "read", "update"]},
  {"entity": "contracts", "actions": ["create", "read", "update", "delete"]},
  {"entity": "assignments", "actions": ["create", "read", "update", "delete"]},
  {"entity": "clients", "actions": ["create", "read", "update"]},
  {"entity": "companies", "actions": ["create", "read", "update", "delete"]},
  {"entity": "projects", "actions": ["create", "read", "update"]},
  {"entity": "project-types", "actions": ["create", "read", "update", "delete"]},
  {"entity": "roles", "actions": ["create", "read", "update", "delete"]},
  {"entity": "seniorities", "actions": ["create", "read", "update", "delete"]},
  {"entity": "technologies", "actions": ["create", "read", "update", "delete"]},
  {"entity": "end-reasons", "actions": ["create", "read", "update", "delete"]},
  {"entity": "tenants", "actions": ["create", "read", "update", "delete"]},
  {"entity": "leave-types", "actions": ["create", "read", "update", "delete"]},
  {"entity": "holidays", "actions": ["create", "read", "update", "delete"]},
  {"entity": "countries", "actions": ["create", "read", "update", "delete"]},
  {"entity": "vacations", "actions": ["create", "read", "update", "delete"]},
  {"entity": "ptos", "actions": ["create", "read", "update", "delete"]},
  {"entity": "reports/employees", "actions": ["create", "read", "update", "delete"]},
  {"entity": "reports/ptos/employees", "actions": ["create", "read", "update", "delete"]},
  {"entity": "overtimes", "actions": ["create", "read", "update", "delete"]}
]'::json
WHERE id='160ec32b-b39f-4966-add0-c82e03d2edb7'::uuid::uuid; --hr director role
UPDATE public.config
SET menu='[
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
        "name": "End Reasons",
        "href": "/#/end-reasons",
        "key": 610
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
      	"name": "PTOs",
        "href": "/#/reports/ptos/employees",
        "key": 72
      }
    ]
  },
  {
    "name": "Overtimes",
    "href": "/#/overtimes",
    "icon": "overtimes",
    "key": 8
  }
]'::json,permissions='[
  {"entity": "employees", "actions": ["create", "read", "update"]},
  {"entity": "contracts", "actions": ["create", "read", "delete"]},
  {"entity": "assignments", "actions": ["create", "read", "update", "delete"]},
  {"entity": "clients", "actions": ["create", "read", "update"]},
  {"entity": "companies", "actions": ["create", "read", "update", "delete"]},
  {"entity": "projects", "actions": ["create", "read", "update"]},
  {"entity": "project-types", "actions": ["create", "read", "update", "delete"]},
  {"entity": "roles", "actions": ["create", "read", "update", "delete"]},
  {"entity": "seniorities", "actions": ["create", "read", "update", "delete"]},
  {"entity": "technologies", "actions": ["create", "read", "update", "delete"]},
  {"entity": "end-reasons", "actions": ["create", "read", "update", "delete"]},
  {"entity": "tenants", "actions": ["create", "read", "update", "delete"]},
  {"entity": "leave-types", "actions": ["create", "read", "update", "delete"]},
  {"entity": "holidays", "actions": ["create", "read", "update", "delete"]},
  {"entity": "countries", "actions": ["create", "read", "update", "delete"]},
  {"entity": "vacations", "actions": ["create", "read", "update", "delete"]},
  {"entity": "ptos", "actions": ["create", "read", "update", "delete"]},
  {"entity": "reports/ptos/employees", "actions": ["create", "read", "update", "delete"]},
  {"entity": "overtimes", "actions": ["create", "read", "update", "delete"]}
]'::json
WHERE id='3c245af9-19fc-4e02-b9b0-a85e8ed06594'::uuid::uuid; --dev role
UPDATE public.config
SET menu='[
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
        "name": "End Reasons",
        "href": "/#/end-reasons",
        "key": 610
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
      }
    ]
  },
  {
    "name": "Overtimes",
    "href": "/#/overtimes",
    "icon": "overtimes",
    "key": 8
  }
]'::json,permissions='[
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
  {"entity": "end-reasons", "actions": ["create", "read", "update", "delete"]},
  {"entity": "tenants", "actions": ["create", "read", "update", "delete"]},
  {"entity": "leave-types", "actions": ["create", "read", "update", "delete"]},
  {"entity": "holidays", "actions": ["create", "read", "update", "delete"]},
  {"entity": "countries", "actions": ["create", "read", "update", "delete"]},
  {"entity": "vacations", "actions": ["create", "read", "update", "delete"]},
  {"entity": "ptos", "actions": ["create", "read", "update", "delete"]},
  {"entity": "reports/employees", "actions": ["create", "read", "update", "delete"]},
  {"entity": "reports/ptos/employees", "actions": ["create", "read", "update", "delete"]},
  {"entity": "overtimes", "actions": ["create", "read", "update", "delete"]}
]'::json
WHERE id='d172386e-46b3-4c6f-b030-14b9204ea059'::uuid::uuid; --admin ro