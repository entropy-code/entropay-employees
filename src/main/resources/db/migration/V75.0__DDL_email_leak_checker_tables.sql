CREATE TABLE IF NOT EXISTS public.email_leak_history
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted boolean NOT NULL,
    email character varying(255) COLLATE pg_catalog."default" NOT NULL,
    employee_id uuid NOT NULL,
    api_response text COLLATE pg_catalog."default",
    CONSTRAINT fk_email_leak_history_employee FOREIGN KEY (employee_id)
    REFERENCES public.employee (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.email_vulnerability
(
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL,
    modified_at timestamp without time zone NOT NULL,
    deleted boolean NOT NULL,
    email character varying(255) COLLATE pg_catalog."default" NOT NULL,
    employee_id uuid NOT NULL,
    source_name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    breach_date date,
    password character varying(255) COLLATE pg_catalog."default",
    origin text COLLATE pg_catalog."default",
    detected_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    status character varying(255) COLLATE pg_catalog."default" NOT NULL,
    leak_type character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT fk_email_vulnerability_employee FOREIGN KEY (employee_id)
    REFERENCES public.employee (id) MATCH SIMPLE
                          ON UPDATE NO ACTION
                          ON DELETE CASCADE
    );