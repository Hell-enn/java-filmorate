# java-filmorate
Приложение JAVA FILMORATE - социальная сеть для любителей кино. 

Функциональность: 
- хранение информации о фильмах и пользователях (пока in memory, позже - в РБД).
- формирование рейтинга отдельных фильмов путем добавления лайков пользователем.
- взаимодействие между отдельными пользователями приложения путем добавления друг друга в друзья.

Новый функционал приложения:
- Функциональность «Отзывы».
- Функциональность «Поиск».
- Функциональность «Общие фильмы».
- Функциональность «Рекомендации».
- Функциональность «Лента событий».
- Удаление пользователей и фильмов.
- Вывод популярных фильмов по жанру и годам.
- Добавление режиссеров в фильмы.

Новые таблицы:
- review.
- review_rate.
- events.
- directors.
- film_directors.

На текущем этапе приложение хранит данные в течение времени работы сервера. Такой способ хранения 
ставит под угрозу сохранность информации, связанной с бизнес-логикой, и отрицает возможность 
дальнейшего расширения приложения. 
Ниже представлена диаграмма, описывающая структуру реляционной базы данных, которая будет 
лежать в основе механизма хранения данных приложения JAVA FILMORATE и её краткое описание.

![Диаграмма базы данных, содержащей информацию о фильмах и пользователях приложения JAVA FILMORATE](java-filmorate.png)

`film`

Содержит данные об отдельных фильмах.
Таблица включает такие поля:
  - первичный ключ `film_id` - идентификатор фильма;
  - `name` — название фильма;
  - `description` - описание фильма;
  - `release_date` - дата выпуска;
  - `duration` - продолжительность в минутах;
  - внешний ключ `mpa_id` (ссылается на таблицу `mpa`) - идентификатор возрастного рейтинга.


`likes`

Таблица-связка содержит информацию о фильмах и лайках к ним от разных пользователей.
Таблица включает такие поля:
  - `film_id` - часть составного первичного ключа (ссылается на таблицу `films`), идентификатор фильма;
  - `user_id` - часть составного первичного ключа (ссылается на таблицу `users`), идентификатор пользователя.


`rating`

Содержит данные о возрастных рейтингах фильмов.
Таблица включает такие поля:
  - первичный ключ `rating_id` - идентификатор возрастного рейтинга;
  - `name` — наименование отметки о возрастном рейтинге;
  - `description` - описание отметки о возрастном рейтинге.


`genre`

Содержит данные о жанрах фильмов приложения.
Таблица включает такие поля:
  - первичный ключ `genre_id` - идентификатор жанра;
  - `name` — наименование жанра.


`genre_film`

Таблица-связка содержит информацию о фильмах и их жанрах.
Таблица включает такие поля:
  - `film_id` - часть составного первичного ключа (ссылается на таблицу `films`), идентификатор фильма;
  - `genre_id` - часть составного первичного ключа (ссылается на таблицу `genres`), идентификатор жанра фильма.


`users`

Содержит данные об отдельных пользователях.
Таблица включает такие поля:
  - первичный ключ `user_id` - идентификатор пользователя;
  - `email` — адрес электронной почты пользователя;
  - `login` - логин пользователя;
  - `name` - имя пользователя;
  - `birthday` - день рождения пользователя.


`friendship`

Содержит данные о статусе для связи «дружба» между двумя пользователями.
Таблица включает такие поля:
- `following_user_id` - часть составного первичного ключа (ссылается на таблицу `users`), идентификатор пользователя, 
отправившего заявку на добавление в друзья;
- `followed_user_id` - часть составного первичного ключа (ссылается на таблицу `users`), идентификатор пользователя, 
которому была отправлена заявка на добавление в друзья;
- `accept` - отношение пользователя followed_user к пользователю following_user (в случае, если followed_user
  примет заявку following_user, поле примет значение 1. Если решит отклонить заявку, проигнорирует её или удалит following_user из
  списка друзей - 0).

`review`

Содержит данные об отзывах пользователей. Таблица включает такие поля:
- первичный ключ `review_id` - идентификатор отзыва;
- `content` - содержание отзыва;
- `is_positive` - тип отзыва;
- `user_id` - идентификатор пользователя. (Ссылается на таблицу `users`); 
- `film_id` - идентификатор фильма. (Ссылается на таблицу `films`);

`review_rate`

Содержит данные о полезности отзыва. Таблица включает такие поля:

- `review_id` - идентификатор отзыва. (Ссылается на таблицу `review`);
- `user_id` - идентификатор пользователя. (Ссылается на таблицу `users`);
- `is_useful` - полезность отзыва;

`events`

Содержит данные о действиях пользователя. Таблица включает такие поля:
- первичный ключ `event_id` - идентификатор операции;
- `timestamp` - временная ветка операции;
- `user_id` - идентификатор пользователя. (Ссылается на таблицу `users`);
- `event_type` - сущность над которой проводится операция;
- `operation` - тип проводимой операции;
- `entity_id` - идентификатор сущности, над которой проводится операция;

`directors`

Содержит данные режиссера. Таблица включает такие поля:
- первичный ключ `director_id` - идентификатор режиссера;
- `name` - ФИО режиссера;

`film_directors`

Таблица-связка содержит информацию о фильмах и их режиссерах. Таблица включает такие поля:
- `film_id` - часть составного первичного ключа (ссылается на таблицу `films`), идентификатор фильма;
- `director_id` - часть составного первичного ключа (ссылается на таблицу `directors`), идентификатор режиссера;

Примеры запросов к описанной базе данных: 
- Получение перечня всех фильмов
```agsl
SELECT * 
FROM films;
```

- Получение фильма с id = 3
```agsl
SELECT * 
FROM films 
WHERE film_id = 3;
```

- Получение 10 самых популярных фильмов
```agsl
SELECT f.film_id, COUNT(l.user_id) as likes_amount 
FROM films as f 
RIGHT JOIN likes as l ON f.film_id = l.film_id 
GROUP BY f.film_id 
ORDER BY likes_amount 
LIMIT 10;
```

- Получение 10 самых популярных комедий за 2022 год
```agsl
SELECT f.* 
FROM film AS f 
LEFT JOIN likes AS l ON f.film_id = l.film_id
LEFT JOIN genre_film AS gf ON f.film_id = gf.film_id
WHERE gf.genre_id = 1 AND YEAR(f.release_date) = 2022
GROUP BY f.film_id 
ORDER BY COUNT(l.user_id) DESC 
LIMIT 10;
```

- Добавление нового фильма(в поле `film_id` ничего не добавляем, поскольку оно будет IDENTITY)
```agsl
INSERT INTO films 
(name, description, release_date, duration, mpa_id) 
VALUES 
('Любовная любовь', 'Они смогли полюбить, но не смогли разлюбить...', '2022-02-02', 200, 3);
```

- Обновление информации о фильме с id = 3
```agsl
UPDATE films 
SET release_date = '2001-01-01'
WHERE film_id = 3;
```

- Удаление фильма с id = 3
```agsl
DELETE FROM film 
WHERE film_id = 3;
```

- Добавление пользователем c id = 1 лайка фильму c id = 2
```agsl
INSERT INTO likes 
VALUES 
(1,2);
```

- Удаление пользователем c id = 1 лайка с фильма c id = 2
```agsl
DELETE 
FROM likes 
WHERE film_id = 2 AND user_id = 1;
```

- Добавление нового пользователя(в поле `user_id` ничего не добавляем, поскольку оно будет IDENTITY)
```agsl
INSERT INTO users 
(email, login, name, birthday) 
VALUES 
('ivanov@gmail.com', 'ivan_ivanov24', 'Иван Иванов', '1992-02-02');
```

- Обновление информации о пользователе с id = 2
```agsl
UPDATE users 
SET birthday = '2005-11-11'
WHERE user_id = 2;
```

- Получение перечня с информацией обо всех пользователях
```agsl
SELECT * 
FROM users;
```

- Получение информации о пользователе с id = 1
```agsl
SELECT * 
FROM users 
WHERE user_id = 1;
```

- Отправка пользователем c id = 1 заявки на "дружбу" пользователю c id = 2
```agsl
INSERT INTO friendship  
VALUES 
(1,2,true,false);
```

- Одобрение пользователем c id = 2 заявки на "дружбу" пользователя c id = 1
```agsl
UPDATE friendship 
SET followed_user_accept = true
WHERE following_user_id = 1 AND followed_user_id = 2;
```

- Удаление пользователем с id = 1 пользователя с id = 2 из друзей (подписка пользователя с id = 2 на пользователя с id = 1 
остается)
```agsl
UPDATE friendship 
SET following_user_accept = false
WHERE following_user_id = 1 AND followed_user_id = 2;
```

- Получение информации о друзьях пользователя с id = 3
```agsl
SELECT u.* 
FROM 
((SELECT followed_user_id 
 FROM friendship 
 WHERE following_user_id = 3)
	UNION
(SELECT following_user_id 
 FROM friendship 
 WHERE followed_user_id = 3)) AS friends_ids 
 LEFT JOIN users AS u ON u.user_id = friends_ids.followed_user_id;
```

- Получение информации об общих друзьях пользователей с id = 1 и 3 
```agsl
SELECT u2.* 
FROM 
((SELECT u.user_id 
FROM 
((SELECT followed_user_id 
 FROM friendship 
 WHERE following_user_id = 3 AND following_user_accept = true AND followed_user_accept = true)
	UNION
(SELECT following_user_id 
 FROM friendship 
 WHERE followed_user_id = 3 AND following_user_accept = true AND followed_user_accept = true)) AS friends_ids 
 LEFT JOIN users AS u ON u.user_id = friends_ids.followed_user_id)
 
 INTERSECT 
 
 (SELECT u.user_id 
FROM 
((SELECT followed_user_id 
 FROM friendship 
 WHERE following_user_id = 1 AND following_user_accept = true AND followed_user_accept = true)
	UNION
(SELECT following_user_id 
 FROM friendship 
 WHERE followed_user_id = 1 AND following_user_accept = true AND followed_user_accept = true)) AS friends_ids 
 LEFT JOIN users AS u ON u.user_id = friends_ids.followed_user_id)) AS common_friends
 LEFT JOIN users as u2 ON common_friends.user_id = u2.user_id;
```

- Получение общих с другом фильмов с сортировкой по их популярности
```agsl
SELECT  f.* , count(l.FILM_ID) AS top 
FROM likes l 
JOIN film f ON l.film_id = f.film_id 
WHERE l.USER_ID = 1 OR l.USER_ID  = 2 
GROUP BY f.FILM_ID
having count(distinct l.USER_ID) = 2 
ORDER BY top desc;
```

- Удаление пользователя с id = 1
```agsl
DELETE FROM users WHERE user_id = 1;
```

- Добавление нового отзыва от пользователя с id = 1 к фильму с id = 2
```agsl
INSERT INTO review (content, is_positive, user_id, film_id) 
VALUES ('Текст', true, 1, 2);
```

- Обновление отзыва с id = 1
```agsl
UPDATE review 
SET content = 'Новый текст'
WHERE review_id = 1;
```

- Удаление отзыва с id = 1
```agsl
DELETE FROM review 
WHERE review_id = 1;
```

- Получение всех отзывов к фильму с id = 1
```agsl
SELECT *
FROM review
WHERE film_id = 1;
```

- Получение отзыва с id = 1
```agsl
SELECT * 
FROM review
WHERE review_id = 1;
```

- Добавление пользователем c id = 1 лайка отзыву c id = 2
```agsl
INSERT INTO review_rate (review_id, user_id, is_useful) 
VALUES (2, 1, true);
```

- Удаление пользователем c id = 1 лайка с отзыва c id = 2
```agsl
DELETE FROM review_rate 
WHERE review_id = 2 AND user_id = 1;
```

- Получение ленты событий пользователя с id = 1
```agsl
SELECT * 
FROM events 
WHERE user_id = 1;
```

- Добавление нового режиссера 
```agsl
INSERT INTO directors (name) 
VALUES ('стивен спилберг');
```

- Обновление имени режиссера с id = 1
```agsl
UPDATE directors SET name = 'Стивен Спилберг' 
WHERE director_id = 1;
```

- Получение режиссера c id = 1 
```agsl
SELECT * 
FROM directors 
WHERE director_id = 1;
```

- Получение всех режиссеров
```agsl
SELECT * 
FROM directors;
```

- Удаление режиссера с id = 1 
```agsl
DELETE FROM directors 
WHERE director_id = 1;
```

- Получение фильмов и режиссеров имеющих в имени 'Крад' (Поиск по названию)
```agsl
SELECT * 
FROM FILM f  
LEFT JOIN FILM_DIRECTORS fd ON f.FILM_ID = fd.FILM_ID
LEFT JOIN DIRECTORS d ON fd.DIRECTOR_ID = d.DIRECTOR_ID
WHERE (f.NAME ILIKE 'Крад' OR d.NAME ILIKE 'Крад')
ORDER BY (SELECT COUNT(film_id) FROM likes WHERE film_id = f.film_id) DESC;
```

- Получение списка рекомендации по фильмам для пользователя с id = 1
```agsl
SELECT l2.user_id, COUNT(l2.film_id) AS amount
FROM likes l1
JOIN likes l2 ON l1.film_id = l2.film_id AND l1.user_id <> l2.user_id
WHERE l1.user_id = 1
GROUP BY l2.user_id
ORDER BY amount DESC
LIMIT 1; //находим N пользователя;

SELECT f.*
FROM likes l
JOIN film f ON l.film_id = f.film_id
LEFT JOIN likes l2 ON l.film_id = l2.film_id AND l2.user_id = 1
WHERE l2.film_id IS NULL AND l.user_id = N;
```
