INSERT INTO genre (name)
VALUES
('Комедия'),
('Драма'),
('Мультфильм'),
('Триллер'),
('Документальный'),
('Боевик');


INSERT INTO rating (name, description)
VALUES
('G', 'Нет возрастных ограничений'),
('PG', 'Рекомендуется присутствие родителей'),
('PG-13', 'Детям до 13 лет просмотр не желателен'),
('R', 'Лицам до 17 лет обязательно присутствие взрослого'),
('NC-17', 'Лицам до 18 лет просмотр запрещен');


INSERT INTO film
(name, description, release_date, duration)
VALUES
('Фильм 1', 'Описание 1', '20240404', 200),
('Фильм 2', 'Описание 2', '20230303', 190),
('Фильм 3', 'Описание 3', '20220202', 180),
('Фильм 4', 'Описание 4', '20210101', 170),
('Фильм 5', 'Описание 55', '20201010', 160);


INSERT INTO users
(email, login, name, birthday)
VALUES
('user1@gmail.com', 'user1', 'Ivan Ivanov', '19990909'),
('user2@gmail.com', 'user2', 'Petr Petrov', '20021212'),
('user3@gmail.com', 'user3', 'Alexey Alexeev', '19800908'),
('user4@gmail.com', 'user4', 'Alexandr Alexandrov', '19920202'),
('user5@gmail.com', 'user5', 'Egor Egorov', '19930303');


INSERT INTO likes
(film_id, user_id)
VALUES
(1, 1),
(1, 3),
(1, 4),
(2, 1),
(2, 2),
(3, 5),
(3, 4),
(4, 1),
(5, 2),
(5, 3);


INSERT INTO
genre_film(film_id, genre_id)
VALUES
(1, 1),
(1, 2),
(2, 4),
(3, 5),
(3, 4),
(3, 6),
(4, 1),
(5, 2),
(5, 5);



INSERT INTO
rating_film(film_id, rating_id)
VALUES
(1, 1),
(2, 4),
(3, 5),
(4, 1),
(5, 5);



INSERT INTO
friendship(following_user_id, followed_user_id, accept)
VALUES
(1, 2, false),
(1, 3, true),
(1, 4, false),
(1, 5, true),
(2, 3, true),
(3, 5, false),
(3, 4, true),
(5, 2, false);
