DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS friendship;
DROP TABLE IF EXISTS genre_film;
DROP TABLE IF EXISTS rating_film;
DROP TABLE IF EXISTS film;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS genre;
DROP TABLE IF EXISTS rating;



CREATE TABLE IF NOT EXISTS genre (
    genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(200)
);



CREATE TABLE IF NOT EXISTS rating (
    rating_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(15) NOT NULL,
    description VARCHAR(200)
);


CREATE TABLE IF NOT EXISTS film (
    film_id  INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(200),
    description VARCHAR(1500),
    release_date DATE,
    duration INTEGER,
    rating_id INTEGER REFERENCES rating(rating_id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email VARCHAR(200),
    login VARCHAR(200),
    name VARCHAR(200),
    birthday DATE,
    CONSTRAINT chk_not_blank CHECK (email <> '' AND name <> '')
);


ALTER TABLE users
ADD UNIQUE (login);



CREATE TABLE IF NOT EXISTS genre_film (
    film_id INTEGER REFERENCES film(film_id) ON DELETE CASCADE,
    genre_id INTEGER REFERENCES genre(genre_id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, genre_id)
);



CREATE TABLE IF NOT EXISTS likes (
    film_id INTEGER REFERENCES film(film_id) ON DELETE CASCADE,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE
);



CREATE TABLE IF NOT EXISTS friendship (
    following_user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    followed_user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    accept BOOLEAN DEFAULT FALSE
);
