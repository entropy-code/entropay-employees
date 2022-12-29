DROP TRIGGER IF EXISTS unique_contract_active ON public.contract;
create trigger unique_contract_active after
    insert
    or
update
    on
        contract for each row
    when ((new.active = true)) execute procedure on_active_status_validate_unique();

CREATE OR REPLACE FUNCTION public.on_active_status_validate_unique()
 RETURNS trigger
 LANGUAGE plpgsql
AS $function$
begin
    IF ((SELECT 1 FROM contract WHERE active = TRUE AND employee_id = NEW.employee_id) > 1) THEN
        RAISE EXCEPTION 'An employee can not have multiple active contracts';
end if;
return new;
END
$function$
;


CREATE TABLE config (
    id uuid NOT NULL,
    "role" varchar(50) NOT NULL,
    permissions json NULL,
    menu json NULL,
    created_at timestamp NOT NULL,
    modified_at timestamp NOT NULL,
    deleted bool NOT NULL,
    CONSTRAINT pk_config PRIMARY KEY (id)
);

INSERT INTO config (id, "role", permissions, menu, created_at, modified_at, deleted) VALUES('8fe02eb2-2d93-40c5-8ebe-1bbfa86c9456'::uuid, 'ROLE_ANALYST', '[
  {"entity": "employees", "actions": ["read"]},
  {"entity": "contracts", "actions": ["read"]},
  {"entity": "assignments", "actions": ["read"]},
  {"entity": "clients", "actions": ["read"]}
]'::json::json, '[
  {
    "name": "Dashboard",
    "href": "/#/",
    "icon": "dashboard",
    "key": 1
  },
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
]'::json::json, '2022-12-28 15:19:29.000', '2022-12-28 15:19:29.000', false);
INSERT INTO config (id, "role", permissions, menu, created_at, modified_at, deleted) VALUES('b1bf3a8c-ced5-443f-90a3-ee9d283a18c0'::uuid, 'ROLE_MANAGER_HR', '[
  {"entity": "employees", "actions": ["create", "read", "update", "delete"]},
  {"entity": "contracts", "actions": ["create", "read", "update", "delete"]},
  {"entity": "assignments", "actions": ["create", "read", "update", "delete"]},
  {"entity": "clients", "actions": ["create", "read", "update", "delete"]}
]'::json::json, '[
  {
    "name": "Dashboard",
    "href": "/#/",
    "icon": "dashboard",
    "key": 1
  },
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
]'::json::json, '2022-12-28 15:19:29.592', '2022-12-28 15:19:29.592', false);
INSERT INTO config (id, "role", permissions, menu, created_at, modified_at, deleted) VALUES('d172386e-46b3-4c6f-b030-14b9204ea059'::uuid, 'ROLE_ADMIN', '[
  {"entity": "employees", "actions": ["create", "read", "update", "delete"]},
  {"entity": "contracts", "actions": ["create", "read", "update", "delete"]},
  {"entity": "assignments", "actions": ["create", "read", "update", "delete"]},
  {"entity": "clients", "actions": ["create", "read", "update", "delete"]}
]'::json::json, '[
  {
    "name": "Dashboard",
    "href": "/#/",
    "icon": "dashboard",
    "key": 1
  },
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
]'::json::json, '2022-12-28 15:19:29.000', '2022-12-28 15:19:29.000', false);
INSERT INTO config (id, "role", permissions, menu, created_at, modified_at, deleted) VALUES('3c245af9-19fc-4e02-b9b0-a85e8ed06594'::uuid, 'ROLE_DEVELOPMENT', '[
  {"entity": "employees", "actions": ["create", "read", "update", "delete"]},
  {"entity": "contracts", "actions": ["create", "read", "update", "delete"]},
  {"entity": "assignments", "actions": ["create", "read", "update", "delete"]},
  {"entity": "clients", "actions": ["create", "read", "update", "delete"]}
]'::json::json, '[
  {
    "name": "Dashboard",
    "href": "/#/",
    "icon": "dashboard",
    "key": 1
  },
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
]'::json::json, '2022-12-28 15:19:29.000', '2022-12-28 15:19:29.000', false);
