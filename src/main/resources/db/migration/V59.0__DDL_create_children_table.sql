CREATE TABLE children(
                                    id  UUID NOT NULL,
                                    first_name   VARCHAR(100) NOT NULL,
                                    last_name varchar(100) NOT NULL,
                                    gender varchar(10) NOT NULL,
                                    birth_date  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                    deleted  BOOLEAN NOT NULL,
                                    created_at  timestamp    NOT NULL,
                                    modified_at timestamp    NOT NULL,
                                    CONSTRAINT children_pk PRIMARY KEY (id)
);