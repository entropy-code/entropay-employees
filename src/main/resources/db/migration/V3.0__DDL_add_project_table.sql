CREATE TABLE project_type (
    id uuid
        CONSTRAINT project_type_pk
        PRIMARY KEY,
    name        varchar(100) NOT NULL,
    created_at  timestamp    NOT NULL,
    modified_at timestamp    NOT NULL,
    deleted     boolean      NOT NULL
)
;

CREATE TABLE project (
    id uuid
        CONSTRAINT project_pk
        PRIMARY KEY,
    client_id uuid NOT NULL
        CONSTRAINT project_client_id_fk
        REFERENCES client,
    project_type_id uuid NOT NULL
        CONSTRAINT project_project_type_id_fk
        REFERENCES project_type,
    name         varchar(100) NOT NULL,
    start_date   timestamp,
    end_date     timestamp,
    notes        varchar(255),
    created_at   timestamp NOT NULL,
    modified_at  timestamp NOT NULL,
    deleted      boolean   NOT NULL
)
;
