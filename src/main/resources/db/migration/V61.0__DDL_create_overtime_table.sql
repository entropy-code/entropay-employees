CREATE TABLE overtime (
    id           UUID                        NOT NULL,
    created_at   TIMESTAMP					 NOT NULL,
    modified_at  TIMESTAMP 					 NOT NULL,
    deleted      BOOLEAN                     NOT NULL,
    date   TIMESTAMP 					 NOT NULL,
    hours FLOAT,
    description VARCHAR(100),
    employee_id  UUID,
    assignment_id     UUID,

    CONSTRAINT pk_overtime PRIMARY KEY (id),
    CONSTRAINT fk_overtime_on_employee FOREIGN KEY (employee_id) REFERENCES employee(id),
    CONSTRAINT fk_overtime_on_assignment_id FOREIGN KEY (assignment_id) REFERENCES assignment(id)
)
;