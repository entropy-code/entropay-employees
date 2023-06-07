CREATE TABLE vacation
(
    id          uuid
        CONSTRAINT vacation_pk
            PRIMARY KEY,
    year        varchar(100) NOT NULL,
    credit      numeric      NULL,
    debit      numeric      NULL,
    employee_id  UUID NOT NULL,
    created_at  timestamp    NOT NULL,
    modified_at timestamp    NOT NULL,
    deleted     boolean      NOT NULL,
    CONSTRAINT vacation_fk FOREIGN KEY (employee_id) REFERENCES employee(id)
)
;