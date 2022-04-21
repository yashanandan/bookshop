truncate table orders;
ALTER TABLE Orders ADD COLUMN payment_status varchar NOT NULL;
ALTER TABLE Orders ADD COLUMN payment_mode varchar NOT NULL;