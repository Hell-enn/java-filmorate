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
(name, description, release_date, duration, rating_id)
VALUES
('Фильм 1', 'Описание 1', '20240404', 200, 1),
('Фильм 2', 'Описание 2', '20230303', 190, 4),
('Фильм 3', 'Описание 3', '20220202', 180, 5),
('Фильм 4', 'Описание 4', '20210101', 170, 1),
('Фильм 5', 'Описание 5', '20201010', 160, 5),
('Фильм 6', 'Описание 6', '20240404', 200, 1),
('Фильм 7', 'Описание 7', '20230303', 190, 4),
('Фильм 8', 'Описание 8', '20220202', 180, 5),
('Фильм 9', 'Описание 9', '20210101', 170, 1),
('Фильм 10', 'Описание 10', '20201010', 160, 5),
('Фильм 11', 'Описание 11', '20240404', 200, 1),
('Фильм 12', 'Описание 12', '20230303', 190, 4),
('Фильм 13', 'Описание 13', '20220202', 180, 5),
('Фильм 14', 'Описание 14', '20210101', 170, 1),
('Фильм 15', 'Описание 15', '20201010', 160, 5);


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



INSERT INTO review
(content, is_positive, user_id, film_id)
VALUES
('Отзыв 1', TRUE, 1, 1),
('Отзыв 2', TRUE, 2, 4),
('Отзыв 3', FALSE, 3, 5),
('Отзыв 4', TRUE, 4, 1),
('Отзыв 5', FALSE, 5, 5),
('Отзыв 6', TRUE, 1, 1),
('Отзыв 7', TRUE, 2, 4),
('Отзыв 8', FALSE, 3, 5),
('Отзыв 9', TRUE, 4, 1),
('Отзыв 10', FALSE, 5, 5),
('Отзыв 11', TRUE, 1, 1),
('Отзыв 12', TRUE, 2, 4),
('Отзыв 13', FALSE, 3, 5),
('Отзыв 14', TRUE, 4, 1),
('Отзыв 15', FALSE, 5, 5);


INSERT INTO review_rate
(review_id, user_id, is_useful)
VALUES
(1, 1, TRUE),
(1, 3, TRUE),
(1, 4, FALSE),
(2, 1, TRUE),
(2, 2, FALSE),
(3, 5, FALSE),
(3, 4, TRUE),
(4, 1, TRUE),
(5, 2, TRUE),
(5, 3, FALSE);