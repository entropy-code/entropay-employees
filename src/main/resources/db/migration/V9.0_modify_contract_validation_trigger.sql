CREATE OR REPLACE FUNCTION on_active_status_validate_unique()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF (NEW.active = TRUE) AND ((SELECT 1 FROM contract WHERE active = TRUE AND employee_id = NEW.employee_id) > 0) THEN
        RAISE EXCEPTION 'An employee can not have multiple active contracts';
    end if;
    return new;
END
$$;