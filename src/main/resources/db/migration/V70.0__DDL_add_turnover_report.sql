-- Update permissions for ROLE_ADMIN
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
    {"entity": "reports/salaries", "actions": ["create", "read", "update", "delete"]},
    {"entity": "reports/billing", "actions": ["create", "read", "update", "delete"]},
    {"entity": "reports/margin", "actions": ["create", "read", "update", "delete"]},
    {"entity": "reports/margin", "actions": ["create", "read", "update", "delete"]},
    {"entity": "reports/turnover/flat", "actions": ["create", "read", "update", "delete"]},
    {"entity": "overtimes", "actions": ["create", "read", "update", "delete"]},
    {"entity": "feedback/employee", "actions": ["create", "read", "update", "delete"]},
    {"entity": "feedback/client", "actions": ["create", "read", "update", "delete"]}
]' WHERE role = 'ROLE_ADMIN';

-- Update permissions for ROLE_MANAGER_HR
UPDATE public.config SET permissions = e'[
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
    {"entity": "reports/salaries", "actions": ["create", "read", "update", "delete"]},
    {"entity": "reports/billing", "actions": ["create", "read", "update", "delete"]},
    {"entity": "reports/margin", "actions": ["create", "read", "update", "delete"]},
    {"entity": "reports/turnover/flat", "actions": ["create", "read", "update", "delete"]},
    {"entity": "overtimes", "actions": ["create", "read", "update", "delete"]},
    {"entity": "feedback/employee", "actions": ["create", "read", "update", "delete"]},
    {"entity": "feedback/client", "actions": ["create", "read", "update", "delete"]}
]' WHERE role = 'ROLE_MANAGER_HR';

-- Update menu for ROLE_ADMIN
UPDATE public.config SET menu = e'[
    {
      "name": "Employees",
      "href": "/#/employees",
      "icon": "employees",
      "key": 1
    },
    {
      "name": "Feedback",
      "icon": "feedback",
      "key": 2,
      "submenu": [
        {
          "name": "Employee",
          "href": "/#/feedback/employee",
          "key": 21
        },
        {
          "name": "Client",
          "href": "/#/feedback/client",
          "key": 22
        }
      ]
    },
    {
      "name": "Contracts",
      "href": "/#/contracts",
      "icon": "contracts",
      "key": 3
    },
    {
      "name": "Assignments",
      "href": "/#/assignments",
      "icon": "assignments",
      "key": 4
    },
    {
      "name": "Clients",
      "href": "/#/clients",
      "icon": "clients",
      "key": 5
    },
    {
      "name": "Ptos",
      "href": "/#/ptos",
      "icon": "ptos",
      "key": 6
    },
    {
      "name": "Settings",
      "icon": "settings",
      "key": 7,
      "submenu": [
        {
          "name": "Companies",
          "href": "/#/companies",
          "key": 701
        },
        {
          "name": "Countries",
          "href": "/#/countries",
          "key": 702
        },
        {
          "name": "Holidays",
          "href": "/#/holidays",
          "key": 703
        },
        {
          "name": "Leave Types",
          "href": "/#/leave-types",
          "key": 704
        },
        {
          "name": "Projects",
          "href": "/#/projects",
          "key": 705
        },
        {
          "name": "Project types",
          "href": "/#/project-types",
          "key": 706
        },
        {
          "name": "Roles",
          "href": "/#/roles",
          "key": 707
        },
        {
          "name": "Seniorities",
          "href": "/#/seniorities",
          "key": 708
        },
        {
          "name": "Technologies",
          "href": "/#/technologies",
          "key": 709
        },
        {
          "name": "Tenants",
          "href": "/#/tenants",
          "key": 710
        }
      ]
    },
    {
      "name": "Reports",
      "icon": "reports",
      "key": 8,
      "submenu": [
        {
          "name": "Employees",
          "href": "/#/reports/employees",
          "key": 81
        },
        {
          "name": "PTOs",
          "href": "/#/reports/ptos/employees",
          "key": 82
        },
        {
          "name": "Salaries",
          "href": "/#/reports/salaries",
          "key": 83
        },
        {
          "name": "Billing",
          "href": "/#/reports/billing",
          "key": 84
        },
        {
          "name": "Margin",
          "href": "/#/reports/margin",
          "key": 85
        },
        {
          "name": "Turnover",
          "href": "/#/reports/turnover/flat",
          "key": 86
        }
      ]
    },
    {
      "name": "Overtimes",
      "href": "/#/overtimes",
      "icon": "overtimes",
      "key": 9
    }
]' WHERE role = 'ROLE_ADMIN';

-- Update menu for ROLE_MANAGER_HR
UPDATE public.config SET menu = e'[
  {
    "name": "Employees",
    "href": "/#/employees",
    "icon": "employees",
    "key": 1
  },
  {
      "name": "Feedback",
      "icon": "feedback",
      "key": 2,
      "submenu": [
        {
          "name": "Employee",
          "href": "/#/feedback/employee",
          "key": 21
        },
        {
          "name": "Client",
          "href": "/#/feedback/client",
          "key": 22
        }
      ]
    },
  {
    "name": "Contracts",
    "href": "/#/contracts",
    "icon": "contracts",
    "key": 3
  },
  {
    "name": "Assignments",
    "href": "/#/assignments",
    "icon": "assignments",
    "key": 4
  },
  {
    "name": "Clients",
    "href": "/#/clients",
    "icon": "clients",
    "key": 5
  },
  {
    "name": "Ptos",
    "href": "/#/ptos",
    "icon": "ptos",
    "key": 6
  },
  {
    "name": "Settings",
    "icon": "settings",
    "key": 7,
    "submenu": [
      {
        "name": "Companies",
        "href": "/#/companies",
        "key": 701
      },
      {
        "name": "Countries",
        "href": "/#/countries",
        "key": 702
      },
      {
        "name": "Holidays",
        "href": "/#/holidays",
        "key": 703
      },
      {
        "name": "Leave Types",
        "href": "/#/leave-types",
        "key": 704
      },
      {
        "name": "Projects",
        "href": "/#/projects",
        "key": 705
      },
      {
        "name": "Project types",
        "href": "/#/project-types",
        "key": 706
      },
      {
        "name": "Roles",
        "href": "/#/roles",
        "key": 707
      },
      {
        "name": "Seniorities",
        "href": "/#/seniorities",
        "key": 708
      },
      {
        "name": "Technologies",
        "href": "/#/technologies",
        "key": 709
      },
      {
        "name": "End Reasons",
        "href": "/#/end-reasons",
        "key": 710
      },
      {
        "name": "Tenants",
        "href": "/#/tenants",
        "key": 711
      }
    ]
  },
  {
    "name": "Reports",
    "icon": "reports",
    "key": 8,
    "submenu": [
      {
        "name": "Employees",
        "href": "/#/reports/employees",
        "key": 81
      },
      {
      	"name": "PTOs",
        "href": "/#/reports/ptos/employees",
        "key": 82
      },
      {
        "name": "Salaries",
        "href": "/#/reports/salaries",
        "key": 83
      },
      {
        "name": "Billing",
        "href": "/#/reports/billing",
        "key": 84
      },
      {
        "name": "Margin",
        "href": "/#/reports/margin",
        "key": 85
      },
      {
        "name": "Turnover",
        "href": "/#/reports/turnover/flat",
        "key": 86
      }
    ]
  },
  {
    "name": "Overtimes",
    "href": "/#/overtimes",
    "icon": "overtimes",
    "key": 9
  }
]' WHERE role = 'ROLE_MANAGER_HR';