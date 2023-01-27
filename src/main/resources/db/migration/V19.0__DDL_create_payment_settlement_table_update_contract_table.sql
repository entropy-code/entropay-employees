ALTER TABLE contract
DROP COLUMN currency;

ALTER TABLE contract
DROP COLUMN monthly_salary;

ALTER TABLE contract
DROP COLUMN cost_rate;

CREATE TABLE payment_settlement(
                                    id  UUID NOT NULL,
                                    contract_id  UUID NOT NULL,
                                    currency    VARCHAR(50),
                                    modality varchar(100) NOT NULL,
                                    salary numeric NULL,
                                    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                    modified_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                    deleted  BOOLEAN NOT NULL,
                                    CONSTRAINT payment_settlement_pk PRIMARY KEY (id),
                                    CONSTRAINT payment_settlement_contract_fk FOREIGN KEY (contract_id) REFERENCES contract(id)
);