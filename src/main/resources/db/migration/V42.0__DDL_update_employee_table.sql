ALTER TABLE employee ALTER COLUMN country SET NOT NULL;
UPDATE employee SET country = 'Argentina' WHERE country IS NULL;