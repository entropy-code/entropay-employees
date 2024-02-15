ALTER TABLE client ADD internal_id varchar(50) NULL;

UPDATE client SET internal_id = 'c001' WHERE name = 'Entropy Team';
UPDATE client SET internal_id = 'c002' WHERE name = 'Dash Solutions';
UPDATE client SET internal_id = 'c003' WHERE name = 'Guidewheel';
UPDATE client SET internal_id = 'c004' WHERE name = 'Rocketrip';
UPDATE client SET internal_id = 'c005' WHERE name = 'Little Otter Health';
UPDATE client SET internal_id = 'c006' WHERE name = 'Caroo';
UPDATE client SET internal_id = 'c007' WHERE name = 'Vehlo';
UPDATE client SET internal_id = 'c008' WHERE name = 'Halo';
UPDATE client SET internal_id = 'c009' WHERE name = 'Kinema';
UPDATE client SET internal_id = 'c0010' WHERE name = 'My Justice Portal';
UPDATE client SET internal_id = 'c0011' WHERE name = 'ESG Flo';