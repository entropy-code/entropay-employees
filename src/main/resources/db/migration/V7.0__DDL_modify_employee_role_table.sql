alter table "position"
    rename to "role";

CREATE TABLE employee_role(
    employee_id  UUID NOT NULL,
    role_id UUID NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employee(id),
    FOREIGN KEY (role_id) REFERENCES "role"(id)
);

ALTER TABLE employee
    RENAME COLUMN email TO personal_email;

ALTER TABLE employee
    add emergency_contact_full_name VARCHAR(255);

ALTER TABLE employee
    add emergency_contact_phone VARCHAR(255);

ALTER TABLE employee
    add tax_id VARCHAR(255) not null;

ALTER TABLE employee
DROP COLUMN company_id;

alter table employee
    add column personal_number VARCHAR(255) not null;

