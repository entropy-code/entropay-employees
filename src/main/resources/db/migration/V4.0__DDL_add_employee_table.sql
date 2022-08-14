CREATE TABLE employee (
    id          UUID                        NOT NULL,
    first_name  VARCHAR(255),
    last_name   VARCHAR(255),
    email       VARCHAR(255),
    phone       VARCHAR(255),
    address     VARCHAR(255),
    city        VARCHAR(255),
    state       VARCHAR(255),
    zip         VARCHAR(255),
    country     VARCHAR(255),
    birth_date  date,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modified_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    deleted     BOOLEAN                     NOT NULL,
    CONSTRAINT pk_employee PRIMARY KEY (id)
)
;
