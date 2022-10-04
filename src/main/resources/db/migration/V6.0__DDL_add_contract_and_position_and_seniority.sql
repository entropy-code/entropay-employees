CREATE TABLE seniority (
    id uuid NOT NULL,
    "name" varchar(100) NOT NULL,
    created_at timestamp NOT NULL,
    modified_at timestamp NOT NULL,
    deleted bool NOT NULL,
    CONSTRAINT pk_seniority PRIMARY KEY (id),
    CONSTRAINT uc_seniority_name UNIQUE (name)
);

CREATE TABLE "position" (
    id uuid NOT NULL,
    "name" varchar(100) NOT NULL,
    created_at timestamp NOT NULL,
    modified_at timestamp NOT NULL,
    deleted bool NOT NULL,
    CONSTRAINT pk_position PRIMARY KEY (id),
    CONSTRAINT uc_position_name UNIQUE (name)
);


CREATE TABLE contract (
    id uuid NOT NULL,
    employee_id uuid NOT NULL,
    company_id uuid NOT NULL,
    start_date timestamp NULL,
    end_date timestamp NULL,
    position_id uuid NOT NULL,
    hours_per_week int2 NULL,
    cost_rate numeric NULL,
    vacations int2 NULL,
    seniority_id uuid NOT NULL,
    created_at timestamp NOT NULL,
    modified_at timestamp NOT NULL,
    deleted bool NOT NULL,
    CONSTRAINT contract_pk PRIMARY KEY (id),
    CONSTRAINT contract_company_fk FOREIGN KEY (company_id) REFERENCES company(id),
    CONSTRAINT contract_employee_fk FOREIGN KEY (employee_id) REFERENCES employee(id),
    CONSTRAINT contract_position_fk FOREIGN KEY (position_id) REFERENCES "position"(id),
    CONSTRAINT contract_seniority_fk FOREIGN KEY (seniority_id) REFERENCES seniority(id)
);

