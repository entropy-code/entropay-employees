-- V61.0__DLL_create_envers_audit_table.sql
CREATE SEQUENCE audit_envers_info_seq START WITH 1 INCREMENT BY 50;


CREATE TABLE audit_envers_info (
                                   id INTEGER NOT NULL,
                                   timestamp BIGINT NOT NULL,
                                   username VARCHAR(255),
                                   CONSTRAINT audit_envers_info_pkey PRIMARY KEY (id)
);

CREATE TABLE contract_aud (
                              id UUID NOT NULL,
                              rev INT NOT NULL,
                              revtype SMALLINT,
                              company_id UUID,
                              employee_id UUID,
                              role_id UUID,
                              seniority_id UUID,
                              start_date DATE,
                              end_date DATE,
                              hours_per_month INTEGER,
                              benefits VARCHAR(255),
                              notes VARCHAR(255),
                              contract_type VARCHAR(50),
                              active BOOLEAN,
                              end_reason_id UUID,
                              PRIMARY KEY (id, rev),
                              FOREIGN KEY (rev) REFERENCES audit_envers_info (id)
);