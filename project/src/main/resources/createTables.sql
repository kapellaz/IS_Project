CREATE TABLE IF NOT EXISTS owner (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    telephone_number TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS pet (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    species TEXT NOT NULL,
    birthdate DATE NOT NULL,
    weight INTEGER NOT NULL,
    owner_id INTEGER,
    FOREIGN KEY (owner_id) REFERENCES owner (id)
);

