ALTER TABLE employee
    RENAME COLUMN phone TO phone_number;

ALTER TABLE employee
    add mobile_number VARCHAR(255);

ALTER TABLE employee
    add notes VARCHAR(255);

ALTER TABLE employee
    add health_insurance VARCHAR(255);

CREATE TABLE payment_information(
                                    id  UUID NOT NULL,
                                    employee_id  UUID NOT NULL,
                                    platform    VARCHAR(255),
                                    country     VARCHAR(255),
                                    cbu         VARCHAR(255),
                                    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                    modified_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                    deleted  BOOLEAN NOT NULL,
                                    CONSTRAINT payment_information_pk PRIMARY KEY (id),
                                    CONSTRAINT payment_information_employee_fk FOREIGN KEY (employee_id) REFERENCES employee(id)
);

