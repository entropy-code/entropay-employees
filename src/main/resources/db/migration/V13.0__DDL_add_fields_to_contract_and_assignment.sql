ALTER TABLE contract ADD monthly_salary numeric NULL;
ALTER TABLE contract ADD currency varchar(50) NULL;

update contract set currency = 'USD';

ALTER TABLE "assignment" ADD currency varchar(50) NULL;
ALTER TABLE "assignment" ADD labour_hours varchar(50) NULL;

UPDATE "assignment" SET currency = 'USD';

