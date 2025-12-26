CREATE TABLE IF NOT EXISTS employee_feedback_summary (
    id UUID PRIMARY KEY,
    employee_id UUID NOT NULL,
    summary TEXT NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_employee_feedback_summary_employee FOREIGN KEY (employee_id) REFERENCES employee(id)
    );


