-- Create skill table to track employee proficiency levels in different technologies
CREATE TABLE IF NOT EXISTS skill (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    employee_id UUID NOT NULL,
    technology_id UUID NOT NULL,
    proficiency_level VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_skill_employee FOREIGN KEY (employee_id) REFERENCES employee(id),
    CONSTRAINT fk_skill_technology FOREIGN KEY (technology_id) REFERENCES technology(id),
    CONSTRAINT uk_skill_employee_technology UNIQUE (employee_id, technology_id, deleted)
);

-- Create indexes for foreign keys to improve query performance
CREATE INDEX IF NOT EXISTS idx_skill_employee_id ON skill(employee_id);
CREATE INDEX IF NOT EXISTS idx_skill_technology_id ON skill(technology_id);
CREATE INDEX IF NOT EXISTS idx_skill_deleted ON skill(deleted);
