ALTER TABLE books ADD COLUMN count_available BIGINT NOT NULL DEFAULT 0;
ALTER TABLE books ADD COLUMN isbn VARCHAR(16) DEFAULT '';
ALTER TABLE books ADD COLUMN isbn13 BIGINT;
ALTER TABLE books ADD COLUMN image VARCHAR(128);
ALTER TABLE books ADD COLUMN avg_rating NUMERIC DEFAULT 0.0;
ALTER TABLE books ADD COLUMN publication_year NUMERIC;