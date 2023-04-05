DROP FUNCTION on_modify_update_modified_at();

DROP TRIGGER IF EXISTS unique_contract_active ON public.contract;

DROP FUNCTION on_active_status_validate_unique();

