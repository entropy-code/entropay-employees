CREATE TABLE end_reason(
	id			    UUID			    NOT NULL,
	name 		    VARCHAR(100)	NOT NULL,
	created_at	TIMESTAMP		  NOT NULL,
	modified_at	TIMESTAMP		  NOT NULL,
	deleted 	  BOOL			      NOT NULL,
	PRIMARY KEY	(id));

ALTER TABLE contract ADD COLUMN end_reason_id UUID;

ALTER TABLE contract
  ADD CONSTRAINT end_reason_id
  FOREIGN KEY (end_reason_id)
  REFERENCES end_reason(id);