-- Create reimbursement_category table
CREATE TABLE reimbursement_category (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    maximum_amount NUMERIC(10,2),
    period_in_months INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- Create reimbursement table
CREATE TABLE reimbursement (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    employee_id UUID NOT NULL,
    category_id UUID NOT NULL,
    amount NUMERIC(10,2) NOT NULL,
    date DATE NOT NULL,
    comment VARCHAR(1000),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_reimbursement_employee FOREIGN KEY (employee_id) REFERENCES employee (id),
    CONSTRAINT fk_reimbursement_category FOREIGN KEY (category_id) REFERENCES reimbursement_category (id)
);

-- Create indexes for better performance
CREATE INDEX idx_reimbursement_employee_id ON reimbursement(employee_id);
CREATE INDEX idx_reimbursement_category_id ON reimbursement(category_id);
CREATE INDEX idx_reimbursement_date ON reimbursement(date);
CREATE INDEX idx_reimbursement_deleted ON reimbursement(deleted);
CREATE INDEX idx_reimbursement_category_deleted ON reimbursement_category(deleted);