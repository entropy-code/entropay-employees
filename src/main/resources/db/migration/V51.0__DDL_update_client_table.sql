ALTER TABLE client ADD active bool DEFAULT true NOT NULL;
UPDATE client SET active = false WHERE deleted IS true;