CREATE TABLE manufacturer (
  id SERIAL PRIMARY KEY,
  name TEXT,
  founded_year INTEGER);
  
CREATE TABLE car (
  id SERIAL PRIMARY KEY,
  name TEXT,
  manufacturer_id INTEGER REFERENCES manufacturer (id)
  );