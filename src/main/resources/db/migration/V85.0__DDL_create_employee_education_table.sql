-- Create employee_education table
CREATE TABLE IF NOT EXISTS employee_education (
    id UUID NOT NULL PRIMARY KEY,
    employee_id UUID NOT NULL,
    education_level VARCHAR(255) NOT NULL,
    education_level_other VARCHAR(255),
    educational_institution VARCHAR(255) NOT NULL,
    degree VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_education_employee FOREIGN KEY (employee_id) REFERENCES employee(id)
);

-- Create index for foreign keys to improve query performance
CREATE INDEX IF NOT EXISTS idx_education_employee_id ON employee_education(employee_id);