-- Create education_level table
CREATE TABLE IF NOT EXISTS education_level (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- Insert education levels
INSERT INTO education_level (name) VALUES
    ('High School Completed'),
    ('University Degree Completed'),
    ('University Incompleted'),
    ('University In Progress'),
    ('Postgraduate / Master''s / PhD'),
    ('Other (Optional)')
ON CONFLICT (name) DO NOTHING;

-- Migrate data and restructure employee_education table
ALTER TABLE employee_education
ADD COLUMN education_level_id UUID;

-- Migrate data from old column to new
UPDATE employee_education ee
SET education_level_id = el.id
FROM education_level el
WHERE ee.education_level = el.name;

-- Drop old column
ALTER TABLE employee_education
DROP COLUMN education_level;

-- Make the new column NOT NULL
ALTER TABLE employee_education
ALTER COLUMN education_level_id SET NOT NULL;

-- Add foreign key constraint
ALTER TABLE employee_education
ADD CONSTRAINT fk_employee_education_level
FOREIGN KEY (education_level_id) REFERENCES education_level(id);

-- Create index for better performance
CREATE INDEX IF NOT EXISTS idx_employee_education_level_id
ON employee_education(education_level_id);

-- Add education level permissions for ROLE_ADMIN
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
    {"entity": "reports/turnover/flat", "actions": ["create", "read", "update", "delete"]},
    {"entity": "overtimes", "actions": ["create", "read", "update", "delete"]},
    {"entity": "feedback/employee", "actions": ["create", "read", "update", "delete"]},
    {"entity": "feedback/client", "actions": ["create", "read", "update", "delete"]},
    {"entity": "reimbursement-categories", "actions": ["create", "read", "update", "delete"]},
    {"entity": "reimbursements", "actions": ["create", "read", "update", "delete"]},
    {"entity": "skills", "actions": ["create", "read", "update", "delete"]},
    {"entity": "feedback-summary", "actions": ["create", "read", "update", "delete"]},
    {"entity": "benefits", "actions": ["create", "read", "update", "delete"]},
    {"entity": "education-levels", "actions": ["create", "read", "update", "delete"]}
]' WHERE role = 'ROLE_ADMIN';

-- Add education level permissions for ROLE_MANAGER_HR
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
    {"entity": "feedback/client", "actions": ["create", "read", "update", "delete"]},
    {"entity": "reimbursement-categories", "actions": ["create", "read", "update", "delete"]},
    {"entity": "reimbursements", "actions": ["create", "read", "update", "delete"]},
    {"entity": "skills", "actions": ["create", "read", "update", "delete"]},
    {"entity": "feedback-summary", "actions": ["create", "read", "update", "delete"]},
    {"entity": "benefits", "actions": ["create", "read", "update", "delete"]},
    {"entity": "education-levels", "actions": ["create", "read", "update", "delete"]}
]' WHERE role = 'ROLE_MANAGER_HR';

-- Update menu for ROLE_ADMIN (add education level to Settings submenu)
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
          "name": "Benefits",
          "href": "/#/benefits",
          "key": 701
        },
        {
          "name": "Companies",
          "href": "/#/companies",
          "key": 702
        },
        {
          "name": "Countries",
          "href": "/#/countries",
          "key": 703
        },
        {
          "name": "Education Levels",
          "href": "/#/education-levels",
          "key": 704
        },
        {
          "name": "Holidays",
          "href": "/#/holidays",
          "key": 705
        },
        {
          "name": "Leave Types",
          "href": "/#/leave-types",
          "key": 706
        },
        {
          "name": "Projects",
          "href": "/#/projects",
          "key": 707
        },
        {
          "name": "Project types",
          "href": "/#/project-types",
          "key": 708
        },
        {
          "name": "Reimbursement Categories",
          "href": "/#/reimbursement-categories",
          "key": 709
        },
        {
          "name": "Roles",
          "href": "/#/roles",
          "key": 710
        },
        {
          "name": "Seniorities",
          "href": "/#/seniorities",
          "key": 711
        },
        {
          "name": "Technologies",
          "href": "/#/technologies",
          "key": 712
        },
        {
          "name": "Tenants",
          "href": "/#/tenants",
          "key": 713
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
    },
    {
      "name": "Reimbursements",
      "href": "/#/reimbursements",
      "icon": "reimbursements",
      "key": 10
    },
    {
      "name": "Skills",
      "href": "/#/skills",
      "icon": "skills",
      "key": 11
    }
]' WHERE role = 'ROLE_ADMIN';

-- Update menu for ROLE_MANAGER_HR (add education-levels to Settings submenu)
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
        "name": "Benefits",
        "href": "/#/benefits",
        "key": 701
      },
      {
        "name": "Companies",
        "href": "/#/companies",
        "key": 702
      },
      {
        "name": "Countries",
        "href": "/#/countries",
        "key": 703
      },
      {
        "name": "Education Levels",
        "href": "/#/education-levels",
        "key": 704
      },
      {
        "name": "Holidays",
        "href": "/#/holidays",
        "key": 705
      },
      {
        "name": "Leave Types",
        "href": "/#/leave-types",
        "key": 706
      },
      {
        "name": "Projects",
        "href": "/#/projects",
        "key": 707
      },
      {
        "name": "Project types",
        "href": "/#/project-types",
        "key": 708
      },
      {
        "name": "Reimbursement Categories",
        "href": "/#/reimbursement-categories",
        "key": 709
      },
      {
        "name": "Roles",
        "href": "/#/roles",
        "key": 710
      },
      {
        "name": "Seniorities",
        "href": "/#/seniorities",
        "key": 711
      },
      {
        "name": "Technologies",
        "href": "/#/technologies",
        "key": 712
      },
      {
        "name": "End Reasons",
        "href": "/#/end-reasons",
        "key": 713
      },
      {
        "name": "Tenants",
        "href": "/#/tenants",
        "key": 714
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
  },
  {
    "name": "Skills",
    "href": "/#/skills",
    "icon": "skills",
    "key": 10
  }
]' WHERE role = 'ROLE_MANAGER_HR';
