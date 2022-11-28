CREATE TABLE "assignment" (
    id uuid NOT NULL,
    start_date timestamp NULL,
    end_date timestamp NULL,
    role_id uuid NOT NULL,
    seniority_id uuid NOT NULL,
    hours_per_week int2 NULL,
    billable_rate numeric NULL,
    project_id uuid NOT NULL,
    employee_id uuid NOT NULL,
    created_at timestamp NOT NULL,
    modified_at timestamp NOT NULL,
    deleted bool NOT NULL,
    CONSTRAINT assignment_pk PRIMARY KEY (id),
    CONSTRAINT assignment_employee_fk FOREIGN KEY (employee_id) REFERENCES employee(id),
    CONSTRAINT assignment_role_fk FOREIGN KEY (role_id) REFERENCES "role"(id),
    CONSTRAINT assignment_project_fk FOREIGN KEY (project_id) REFERENCES project(id),
    CONSTRAINT assignment_seniority_fk FOREIGN KEY (seniority_id) REFERENCES seniority(id)
);

ALTER TABLE contract RENAME COLUMN position_id TO role_id;

