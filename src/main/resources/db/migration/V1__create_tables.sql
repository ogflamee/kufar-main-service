CREATE DATABASE kufar_main_db;

CREATE TABLE users (
   id SERIAL PRIMARY KEY,
   email VARCHAR(255) UNIQUE NOT NULL,
   username VARCHAR(255) UNIQUE NOT NULL,
   password VARCHAR(255) NOT NULL,
   phone_number VARCHAR (15),
   rating DOUBLE PRECISION DEFAULT 0,
   created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE categories (
   id SERIAL PRIMARY KEY,
   name VARCHAR(255) NOT NULL
);

CREATE TABLE ads (
   id SERIAL PRIMARY KEY,
   user_id INTEGER REFERENCES users(id),
   category_id INTEGER REFERENCES categories(id),
   title VARCHAR(255),
   description VARCHAR(2000),
   price NUMERIC,
   city VARCHAR(255),
   views_count BIGINT,
   status VARCHAR(20),
   created_at TIMESTAMP
);

CREATE TABLE favorites (
   id SERIAL PRIMARY KEY,
   user_id INTEGER REFERENCES users(id),
   ad_id INTEGER REFERENCES ads(id),
   added_at TIMESTAMP
);
