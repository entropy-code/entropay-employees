CREATE TABLE contract (
    id uuid NOT NULL,
    employee_id uuid NOT NULL,
    company_id uuid NOT NULL,
    start_date timestamp NULL,
    end_date timestamp NULL,
    "role" varchar(100) NULL,
    hours_per_week int2 NULL,
    cost_rate numeric NULL,
    vacations int2 NULL,
    seniority varchar(100) NULL,
    created_at timestamp NOT NULL,
    modified_at timestamp NOT NULL,
    deleted bool NOT NULL,
    CONSTRAINT contract_pk PRIMARY KEY (id),
    CONSTRAINT contract_company_fk FOREIGN KEY (company_id) REFERENCES company(id),
    CONSTRAINT contract_employee_fk FOREIGN KEY (employee_id) REFERENCES employee(id)
);
