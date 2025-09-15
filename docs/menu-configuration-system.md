# Menu Configuration System Documentation

## Overview

The Entroteam application uses a role-based menu configuration system that dynamically renders navigation menus based on user roles. The system is implemented through a `config` table that stores JSON-formatted menu structures and permissions for each role.

## Database Structure

### Config Table Schema
- **Table Name**: `config`
- **Created In**: `V14.0__DDL_fix_trigger_add config_entity.sql`

```sql
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
```

## Supported Roles

The system supports the following roles (from `V14.0__DDL_fix_trigger_add config_entity.sql`):
- `ROLE_ADMIN` - Full access to all features
- `ROLE_DEVELOPMENT` - Development team access
- `ROLE_MANAGER_HR` - HR Manager access
- `ROLE_ANALYST` - Read-only analyst access

Additional roles added over time:
- `ROLE_HR_DIRECTOR` (added in `V36.0__DDL_update_role_hr_director.sql`)

## Menu Structure

### JSON Schema
Menus are stored as JSON arrays with the following structure:

```json
[
  {
    "name": "Menu Item Name",
    "href": "/#/route-path",
    "icon": "icon-name",
    "key": 1,
    "submenu": [
      {
        "name": "Sub Item",
        "href": "/#/sub-route",
        "key": 11
      }
    ]
  }
]
```

### Menu Item Properties
- **name**: Display name of the menu item
- **href**: Route path (React Router format with `/#/` prefix)
- **icon**: Icon identifier for UI rendering
- **key**: Unique numeric identifier for the menu item
- **submenu**: Optional array of sub-menu items (for Settings, Reports, etc.)

### Key Numbering Convention
- Main menu items: Single digits (1, 2, 3, ...)
- Sub-menu items: Parent key + incremental number (61, 62, 63, ... for Settings submenu with key 6)

## Current Menu Structure (as of V67.0)

### ROLE_ADMIN Menu (Latest)
Based on the latest migration (`V67.0__DDL_update_menu_billing_report.sql`):

1. **Employees** (key: 1) - `/#/employees`
2. **Contracts** (key: 2) - `/#/contracts`  
3. **Assignments** (key: 3) - `/#/assignments`
4. **Clients** (key: 4) - `/#/clients`
5. **PTOs** (key: 5) - `/#/ptos`
6. **Settings** (key: 6) - Submenu:
   - Companies (61) - `/#/companies`
   - Countries (62) - `/#/countries`
   - Holidays (63) - `/#/holidays`
   - Leave Types (64) - `/#/leave-types`
   - Projects (65) - `/#/projects`
   - Project Types (66) - `/#/project-types`
   - Roles (67) - `/#/roles`
   - Seniorities (68) - `/#/seniorities`
   - Technologies (69) - `/#/technologies`
   - Tenants (611) - `/#/tenants`
7. **Reports** (key: 7) - Submenu:
   - Employees (71) - `/#/reports/employees`
   - PTOs (72) - `/#/reports/ptos/employees`
   - Salaries (73) - `/#/reports/salaries`
   - Billing (74) - `/#/reports/billing`
   - Margin (75) - `/#/reports/margin`
8. **Overtimes** (key: 8) - `/#/overtimes`

## Permissions System

Permissions are stored as JSON arrays with the following structure:

```json
[
  {
    "entity": "entity-name",
    "actions": ["create", "read", "update", "delete"]
  }
]
```

### Supported Entities (as of V67.0)
- `employees`, `contracts`, `assignments`, `clients`
- `companies`, `projects`, `project-types`, `roles`, `seniorities`, `technologies`, `tenants`
- `leave-types`, `holidays`, `countries`, `vacations`, `ptos`, `overtimes`
- `reports/employees`, `reports/ptos/employees`, `reports/salaries`, `reports/billing`, `reports/margin`

### Actions
- `create` - Can create new records
- `read` - Can view records  
- `update` - Can modify existing records
- `delete` - Can delete records (soft delete)

## Implementation Details

### Java Model
```java
@Entity(name = "Config")
@Table(name = "config")
public class Config extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private AppRole role;
    
    @Column(name = "permissions")
    private String permissions; // JSON string
    
    @Column(name = "menu")  
    private String menu; // JSON string
}
```

### DTO Structure
```java
public record ConfigDto(UUID id,
                       String role,
                       List<PermissionDto> permissions,
                       List<MenuItemDto> menu) {
}

public record MenuItemDto(String name,
                         String href,
                         String icon,
                         Integer key,
                         List<MenuItemDto> submenu) {
}

public record PermissionDto(String entity,
                           List<String> actions) {
}
```

### Service Logic
- The `ConfigService` reads configurations based on the current user's role
- JSON strings are parsed into DTOs using Jackson ObjectMapper
- The service extends `BaseService` for standard CRUD operations
- Role-based filtering ensures users only see their authorized configuration

## How to Add New Menu Items

### Step 1: Create Migration File
Create a new Flyway migration file following the naming convention:
```
V{version}__DML_update_config_table.sql
```

### Step 2: Update Menu JSON
Add the new menu item to the appropriate role's menu JSON:

```sql
UPDATE public.config SET menu = e'[
  -- existing menu items...
  {
    "name": "New Feature",
    "href": "/#/new-feature",
    "icon": "new-icon",
    "key": 9
  }
]' WHERE role = 'ROLE_ADMIN';
```

### Step 3: Update Permissions (if needed)
Add corresponding permissions:

```sql
UPDATE public.config SET permissions = e'[
  -- existing permissions...
  {"entity": "new-feature", "actions": ["create", "read", "update", "delete"]}
]' WHERE role = 'ROLE_ADMIN';
```

### Step 4: Consider Other Roles
Update other roles (`ROLE_MANAGER_HR`, `ROLE_ANALYST`, etc.) as appropriate with limited permissions.

## Key Design Principles

1. **Role-Based Access**: Each role has its own menu configuration
2. **JSON Storage**: Flexible schema allows easy menu modifications via migrations
3. **Unique Keys**: Each menu item has a unique numeric key for frontend identification
4. **Hierarchical Menus**: Support for nested submenus (Settings, Reports)
5. **Permission Alignment**: Menu visibility should align with user permissions
6. **Soft Delete Support**: Config entities use soft delete pattern
7. **Migration-Driven**: All menu changes are versioned through Flyway migrations

## Migration History Evolution

- **V14.0**: Initial config table creation with basic CRUD roles
- **V15.0**: Removed Dashboard from all role menus
- **V16.0-V20.0**: Added various menu items (Contracts, Assignments, Technologies)
- **V27.0-V31.0**: Added HR-related features (Holidays, Vacations, PTOs)
- **V35.0-V39.0**: Menu restructuring and Settings submenu organization
- **V45.0-V63.0**: Reports section development and new entities
- **V65.0-V67.0**: Billing and Margin reports addition

This documentation should be updated whenever new menu items or roles are added to the system.