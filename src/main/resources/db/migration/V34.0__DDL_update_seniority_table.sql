ALTER TABLE seniority
    ADD vacation_days INTEGER NULL;

UPDATE seniority
SET vacation_days = CASE
                        WHEN name IN ('Senior 1', 'Senior 2', 'Architect') THEN 15
                        ELSE 10
    END;
