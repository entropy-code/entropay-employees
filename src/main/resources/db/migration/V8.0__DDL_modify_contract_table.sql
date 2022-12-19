ALTER TABLE contract ADD COLUMN active BOOLEAN DEFAULT FALSE;

CREATE OR REPLACE FUNCTION on_modify_update_modified_at()
    RETURNS TRIGGER
    LANGUAGE plpgsql
    AS
$$
BEGIN
    UPDATE contract SET modified_at = CURRENT_TIMESTAMP WHERE id = NEW.id;
    RETURN NEW;
END
$$;

CREATE OR REPLACE FUNCTION on_active_status_validate_unique()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
     IF (NEW.active = TRUE) AND ((SELECT 1 FROM contract WHERE active = 1 AND employee_id = NEW.employee_id) > 0) THEN
         RAISE EXCEPTION 'An employee can not have multiple active contracts';
     end if;
END
$$;

CREATE TRIGGER unique_contract_active
    BEFORE INSERT OR UPDATE
    ON contract
    FOR EACH ROW
    WHEN (NEW.active = TRUE)
    EXECUTE PROCEDURE on_active_status_validate_unique();