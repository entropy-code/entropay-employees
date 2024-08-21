CREATE TABLE employee_children (
    employee_id UUID NOT NULL,
    children_id UUID NOT NULL,
    PRIMARY KEY (employee_id, children_id),
    FOREIGN KEY (employee_id) REFERENCES employee(id),
    FOREIGN KEY (children_id) REFERENCES children(id)
);