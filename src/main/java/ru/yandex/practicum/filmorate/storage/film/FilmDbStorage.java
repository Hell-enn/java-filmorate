package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.*;

/**
 * Класс FilmDbStorage - реализация интерфейса FilmStorage
 * которая взаимодействует с SQL базой данных для работы с сущностями-фильмами.
 */
@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Film addFilm(Film film) {

        String insertFilmQuery =
                "INSERT INTO film (name, description, release_date, duration) VALUES (?, ?, ?, ?)";
        String insertRatingQuery =
                "INSERT INTO rating_film (film_id, rating_id) VALUES (?, ?)";
        StringBuilder insertGenreQuery = new StringBuilder(
                "INSERT INTO genre_film (film_id, genre_id) VALUES ");

        long mpaId = film.getMpa() == null ? -1 : film.getMpa().getId();

        if (mpaId != -1) {
            String ratingQuery = "SELECT * FROM rating WHERE rating_id = ?";

            SqlRowSet ratingRows = jdbcTemplate.queryForRowSet(ratingQuery, mpaId);
            if (!ratingRows.next()) {
                throw new BadRequestException("Ошибка рейтинга в запросе!");
            }

        }

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(insertFilmQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            return stmt;
        }, keyHolder);

        long filmId = keyHolder.getKey().longValue();
        film.setId(filmId);

        jdbcTemplate.update(insertRatingQuery, filmId, mpaId);

        List<Genre> genres = film.getGenres();
        List<Long> genresId = new ArrayList<>();
        if (genres != null) {
            for (Genre genre : genres) {

                SqlRowSet genresRows = jdbcTemplate.queryForRowSet("SELECT * FROM genre WHERE genre_id = ?", genre.getId());
                if (!genresRows.next()) {
                    jdbcTemplate.update("DELETE FROM film WHERE film_id = ?", filmId);
                    jdbcTemplate.update("DELETE FROM rating_film WHERE film_id = ?", filmId);

                    throw new BadRequestException("Ошибка жанра в запросе!");
                }

                Long genreId = genre.getId();
                if (!genresId.contains(genreId)) {
                    genresId.add(genreId);
                    insertGenreQuery.append("(").append(film.getId()).append(",").append(genreId).append("),");
                }
            }
            jdbcTemplate.update(insertGenreQuery.substring(0, insertGenreQuery.length() - 1));
        }

        log.info("Фильм добавлен {} {}!", film.getId(), film.getName());
        return film;
    }


    @Override
    public Film updateFilm(Film film) {

        String findFilmQuery = "SELECT * FROM film WHERE film_id = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(findFilmQuery, film.getId());
        if (!filmRows.next()) {
            throw new NotFoundException("Фильм не найден!");
        } else {

            Film addedFilm = getFilm(filmRows.getLong("film_id"));
            if (!film.getName().equals(addedFilm.getName())) {
                String nameQuery = "UPDATE film " +
                                   "SET name = ? " +
                                   "WHERE film_id = ?;";
                jdbcTemplate.update(nameQuery, film.getName(), film.getId());
            }

            String descriptionQuery;
            if (film.getDescription() != null) {
                if (!film.getDescription().equals(addedFilm.getDescription())) {
                    descriptionQuery = "UPDATE film " +
                            "SET description = ? " +
                            "WHERE film_id = ?;";
                    jdbcTemplate.update(descriptionQuery, film.getDescription(), film.getId());
                }
            } else {
                descriptionQuery = "UPDATE film " +
                        "SET description = NULL " +
                        "WHERE film_id = ?;";
                jdbcTemplate.update(descriptionQuery, film.getDescription(), film.getId());
            }

            if (!film.getReleaseDate().equals(addedFilm.getReleaseDate())) {
                String dateQuery = "UPDATE film " +
                                   "SET release_date = ? " +
                                   "WHERE film_id = ?;";
                jdbcTemplate.update(dateQuery, film.getReleaseDate(), film.getId());
            }

            if (film.getDuration() != addedFilm.getDuration()) {
                String durationQuery = "UPDATE film " +
                            "SET duration = ? " +
                            "WHERE film_id = ?;";
                jdbcTemplate.update(durationQuery, film.getDuration(), film.getId());
            }


            if (film.getMpa() != null) {
                if (!film.getMpa().equals(addedFilm.getMpa())) {
                    String deleteRatingQuery = "DELETE rating_film WHERE film_id = ?";
                    jdbcTemplate.update(deleteRatingQuery, film.getId());

                    String insertRatingQuery =
                            "INSERT INTO rating_film (film_id, rating_id) VALUES (?, ?)";
                    jdbcTemplate.update(insertRatingQuery, film.getId(), film.getMpa().getId());
                }
            } else if (addedFilm.getMpa() != null) {
                String deleteRatingQuery = "DELETE rating_film WHERE film_id = ?";
                jdbcTemplate.update(deleteRatingQuery, film.getId());
            }


            if (film.getGenres() != null) {
                if (!film.getGenres().equals(addedFilm.getGenres())) {
                    String deleteGenresQuery = "DELETE genre_film WHERE film_id = ?";
                    jdbcTemplate.update(deleteGenresQuery, film.getId());

                    StringBuilder insertGenreQuery = new StringBuilder("INSERT INTO genre_film (film_id, genre_id) VALUES ");
                    for (Genre genre : film.getGenres())
                        insertGenreQuery.append("(").append(film.getId()).append(", ").append(genre.getId()).append("),");
                    jdbcTemplate.update(insertGenreQuery.substring(0, insertGenreQuery.length() - 1));
                }
            } else if (addedFilm.getGenres() != null) {
                String deleteGenresQuery = "DELETE genre_film WHERE film_id = ?";
                jdbcTemplate.update(deleteGenresQuery, film.getId());
            }
        }
        return film;
    }


    @Override
    public void deleteFilm(long id) {

        String sqlQuery = "DELETE FROM film WHERE film_id = ?";
        if (jdbcTemplate.update(sqlQuery, id) > 0)
            log.info("Удален фильм с id = {}", id);
        else
            log.info("Фильм с id = {} не удален или отсутствует в списке", id);

    }


    @Override
    public Film getFilm(long id) {

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * " +
                "FROM film " +
                "WHERE film_id = ?", id);

        if (filmRows.next()) {
            return getFilmFromSqlRow(filmRows);
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            return null;
        }

    }


    @Override
    public boolean containsFilm(long id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM film WHERE film_id = ?", id);
        return filmRows.next();
    }


    @Override
    public List<Film> getFilms() {

        List<Film> films = new ArrayList<>();

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM film");

        while (filmRows.next()) {
            films.add(getFilmFromSqlRow(filmRows));
        }

        if (films.isEmpty())
            log.info("Фильмы не найдены.");

        return films;
    }


    /**
     * Закрытый служебный метод используется для получения объекта
     * фильма из строки, полученной из базы данных (таблица 'film').
     * @param filmRows
     * @return
     */
    private Film getFilmFromSqlRow(SqlRowSet filmRows) {

        int filmId = filmRows.getInt("film_id");
        String filmName = filmRows.getString("name");
        String description = filmRows.getString("description");
        LocalDate releaseDate;

        try {
            releaseDate = filmRows.getDate("release_date").toLocalDate();
        } catch (NullPointerException exception) {
            releaseDate = null;
        }
        int duration = filmRows.getInt("duration");
        MPA rating = null;
        Set<Genre> genres = new HashSet<>();

        String ratingQuery = "SELECT r.* FROM " +
                             "rating_film rf " +
                             "JOIN rating r ON rf.rating_id = r.rating_id " +
                             "WHERE rf.film_id = ?";

        SqlRowSet ratingRows = jdbcTemplate.queryForRowSet(ratingQuery, filmId);
        if (ratingRows.next()) {
            rating = new MPA(
                    ratingRows.getLong("rating_id"),
                    ratingRows.getString("name"),
                    ratingRows.getString("description"));
        }

        String genresQuery = "SELECT g.* " +
                             "FROM genre_film gf " +
                             "JOIN genre g ON gf.genre_id = g.genre_id " +
                             "WHERE gf.film_id = ?";

        SqlRowSet genresRows = jdbcTemplate.queryForRowSet(genresQuery, filmId);

        while (genresRows.next()) {
            genres.add(new Genre(
                            genresRows.getLong("genre_id"),
                            genresRows.getString("name")));
        }

        Film film = new Film(filmId, filmName, description, releaseDate, duration, rating, new ArrayList<>(genres));

        log.info("Найден фильм: {} {}", film.getId(), film.getName());

        return film;
    }


    @Override
    public Long addLike(Long filmId, Long userId) {

        String userQuery = "SELECT * FROM users WHERE user_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(userQuery, userId);
        if (!userRows.next())
            throw new NotFoundException("Пользователь не найден!");

        String filmQuery = "SELECT * FROM film WHERE film_id = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(filmQuery, filmId);
        if (!filmRows.next())
            throw new NotFoundException("Фильм не найден!");

        String insertLikeQuery = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        try {
            jdbcTemplate.update(insertLikeQuery, filmId, userId);
            log.info("Лайк от пользователя с id {} добавлен фильму с id {}!", userId, filmId);
        } catch (DataIntegrityViolationException exception) {
            throw new DataIntegrityViolationException("Лайк от данного пользователя уже поставлен этому фильму!");
        }

        return filmId;

    }


    @Override
    public Long deleteLike(Long filmId, Long userId) {

        String deleteLikeQuery = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        int amount = jdbcTemplate.update(deleteLikeQuery, filmId, userId);

        if (amount > 0)
            log.info("Лайк от пользователя с id {} удален с фильма с id {}!", userId, filmId);
        else
            log.info("Лайк отсутствует в списке!");

        return filmId;
    }


    @Override
    public Set<Like> getLikes(int filmId) {

        Set<Like> likes = new HashSet<>();
        SqlRowSet likesRows = jdbcTemplate.queryForRowSet("SELECT f.film_id, user_id " +
                                                              "FROM film f " +
                                                              "LEFT JOIN likes l on f.film_id = l.film_id " +
                                                              "WHERE f.film_id = ?", filmId);

        while (likesRows.next()) {
            Like like = new Like(
                    likesRows.getInt("film_id"),
                    likesRows.getInt("user_id"));
            log.info("Найден лайк для фильма с id = {} от пользователя с id = {}!", like.getFilmId(), like.getUserId());
            likes.add(like);
        }

        if (likes.isEmpty())
            log.info("Информация о лайках для фильма с id = {} отсутсвует!", filmId);

        return likes;

    }


    @Override
    public Set<Genre> getFilmGenres(int filmId) {

        Set<Genre> genres = new HashSet<>();
        SqlRowSet genresRows = jdbcTemplate.queryForRowSet("SELECT g.genre_id " +
                "FROM genre_film gf " +
                "LEFT JOIN genre g ON gf.genre_id = g.genre_id " +
                "WHERE film_id = ?", filmId);

        while (genresRows.next()) {
            Genre genre = new Genre(
                    genresRows.getInt("genre_id"),
                    genresRows.getString("name"));
            log.info("Найден жанр для фильма с id = {}: {}!", filmId, genre.getName());
            genres.add(genre);
        }

        if (genres.isEmpty())
            log.info("Информация о жанрах для фильма с id = {} отсутсвует!", filmId);

        return genres;

    }
}


