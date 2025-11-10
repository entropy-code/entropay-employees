CREATE TABLE IF NOT EXISTS employee_summary (
    id               UUID                        NOT NULL,
    employee_id      UUID                        NOT NULL,
    prompt           VARCHAR(5000),
    summary_text     VARCHAR(5000),

    created_by       VARCHAR(255),
    created_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modified_by      VARCHAR(255),
    modified_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    deleted          BOOLEAN                     NOT NULL DEFAULT FALSE,

    CONSTRAINT pk_employee_summary PRIMARY KEY (id),
    CONSTRAINT fk_employee_summary_employee FOREIGN KEY (employee_id) REFERENCES employee(id)
);

CREATE INDEX idx_employee_summary_employee_id ON employee_summary (employee_id);

