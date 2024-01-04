ALTER TABLE employee ADD country_id uuid;
ALTER TABLE employee ADD CONSTRAINT country_fk FOREIGN KEY (country_id) REFERENCES country(id);

--check that all countries are created in the db
INSERT INTO country (id, name, created_at, modified_at, deleted)
SELECT '89973699-9c55-4675-b90f-22e3c7cb12c3', 'Argentina', now(), now(), false
    WHERE NOT EXISTS (SELECT 1 FROM country WHERE country.name = 'Argentina');

INSERT INTO country (id, name, created_at, modified_at, deleted)
SELECT 'a6e9e994-ca00-488c-b24b-b1b4533a1366', 'España', now(), now(), false
    WHERE NOT EXISTS (SELECT 1 FROM country WHERE country.name = 'España');

INSERT INTO country (id, name, created_at, modified_at, deleted)
SELECT '737e092f-5b9d-4cf6-b7fe-b52a8a30c46f', 'Ecuador', now(), now(), false
    WHERE NOT EXISTS (SELECT 1 FROM country WHERE country.name = 'Ecuador');

INSERT INTO country (id, name, created_at, modified_at, deleted)
SELECT '0f21a1d9-22c5-405c-aea0-70d50f75b215', 'Portugal', now(), now(), false
    WHERE NOT EXISTS (SELECT 1 FROM country WHERE country.name = 'Portugal');

INSERT INTO country (id, name, created_at, modified_at, deleted)
SELECT '146f1780-0f63-420a-9eb9-0b902a950788', 'Colombia', now(), now(), false
    WHERE NOT EXISTS (SELECT 1 FROM country WHERE country.name = 'Colombia');

INSERT INTO country (id, name, created_at, modified_at, deleted)
SELECT '9de568ea-d862-4881-ab1c-d9816461f654', 'Italia', now(), now(), false
    WHERE NOT EXISTS (SELECT 1 FROM country WHERE country.name = 'Italia');

INSERT INTO country (id, name, created_at, modified_at, deleted)
SELECT '594ddf9b-4f9c-46ed-8503-0801e1ef02b1', 'Costa Rica', now(), now(), false
    WHERE NOT EXISTS (SELECT 1 FROM country WHERE country.name = 'Costa Rica');

INSERT INTO country (id, name, created_at, modified_at, deleted)
SELECT '2a4bb151-7ef8-4aac-93d6-ed8005ed086f', 'Uruguay', now(), now(), false
    WHERE NOT EXISTS (SELECT 1 FROM country WHERE country.name = 'Uruguay');

INSERT INTO country (id, name, created_at, modified_at, deleted)
SELECT '3ba350e6-b0be-4e5a-9b1c-6b62d444f188', 'Venezuela', now(), now(), false
    WHERE NOT EXISTS (SELECT 1 FROM country WHERE country.name = 'Venezuela');

UPDATE employee SET country_id = country.id FROM country WHERE employee.country IS NULL AND country.name = 'Argentina';

UPDATE employee SET country_id = country.id FROM country WHERE employee.country = country.name;
ALTER TABLE employee ALTER COLUMN country_id SET NOT NULL;
ALTER TABLE employee DROP COLUMN country;
