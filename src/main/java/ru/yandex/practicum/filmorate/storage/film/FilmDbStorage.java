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
import ru.yandex.practicum.filmorate.model.*;

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
                "INSERT INTO film (name, description, release_date, duration, rating_id) VALUES (?, ?, ?, ?, ?)";
        StringBuilder insertGenreQuery = new StringBuilder(
                "INSERT INTO genre_film (film_id, genre_id) VALUES ");
        StringBuilder insertDirectorsQuery = new StringBuilder(
                "INSERT INTO film_directors (film_id, director_id) VALUES ");

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
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        long filmId = keyHolder.getKey().longValue();
        film.setId(filmId);

        List<Genre> genres = film.getGenres();
        List<Long> genresId = new ArrayList<>();
        if (genres != null) {
            for (Genre genre : genres) {

                SqlRowSet genresRows = jdbcTemplate.queryForRowSet("SELECT * FROM genre WHERE genre_id = ?", genre.getId());
                if (!genresRows.next()) {

                    jdbcTemplate.update("DELETE FROM film WHERE film_id = ?", filmId);
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

        List<Director> directors = film.getDirectors();
        List<Long> directorsId = new ArrayList<>();
        if (directors != null) {
            for (Director director : directors) {

                SqlRowSet directorsRows = jdbcTemplate.queryForRowSet("SELECT * FROM directors WHERE director_id = ?", director.getId());
                if (!directorsRows.next()) {

                    jdbcTemplate.update("DELETE FROM film WHERE film_id = ?", filmId);
                    throw new BadRequestException("Ошибка жанра в запросе!");

                }

                Long directorId = director.getId();
                if (!directorsId.contains(directorId)) {
                    directorsId.add(directorId);
                    insertDirectorsQuery.append("(").append(film.getId()).append(",").append(directorId).append("),");
                }
            }
            jdbcTemplate.update(insertDirectorsQuery.substring(0, insertDirectorsQuery.length() - 1));
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
                    String updateMpa = "UPDATE film SET rating_id = ? WHERE film_id = ?;";
                    jdbcTemplate.update(updateMpa, film.getMpa().getId(), film.getId());
                }
            }

            if (film.getGenres() != null) {
                if (!film.getGenres().equals(addedFilm.getGenres())) {
                    String deleteGenresQuery = "DELETE genre_film WHERE film_id = ?";
                    jdbcTemplate.update(deleteGenresQuery, film.getId());

                    Map<Long, Genre> genres = new HashMap<>();
                    for (Genre genre : film.getGenres())
                        genres.put(genre.getId(), genre);

                    if (!genres.isEmpty()) {
                        StringBuilder insertGenreQuery = new StringBuilder("INSERT INTO genre_film (film_id, genre_id) VALUES ");
                        for (Genre genre : genres.values())
                            insertGenreQuery.append("(").append(film.getId()).append(", ").append(genre.getId()).append("),");
                        jdbcTemplate.update(insertGenreQuery.substring(0, insertGenreQuery.length() - 1));
                        film.setGenres(new ArrayList<>(genres.values()));
                    }
                }
            } else if (addedFilm.getGenres() != null) {
                String deleteGenresQuery = "DELETE genre_film WHERE film_id = ?";
                jdbcTemplate.update(deleteGenresQuery, film.getId());
                film.setGenres(new ArrayList<>());
            }

            if (film.getDirectors() != null) {
                if (!film.getDirectors().equals(addedFilm.getDirectors())) {
                    String deleteDirectorsQuery = "DELETE film_directors WHERE film_id = ?";
                    jdbcTemplate.update(deleteDirectorsQuery, film.getId());

                    Map<Long, Director> directors = new HashMap<>();
                    for (Director director : film.getDirectors()) {
                        directors.put(director.getId(), director);
                    }

                    if (!directors.isEmpty()) {
                        StringBuilder insertDirectorQuery = new StringBuilder("INSERT INTO film_directors (film_id, director_id) VALUES ");
                        for (Director director : directors.values())
                            insertDirectorQuery.append("(").append(film.getId()).append(", ").append(director.getId()).append("),");
                        jdbcTemplate.update(insertDirectorQuery.substring(0, insertDirectorQuery.length() - 1));
                        film.setDirectors(new ArrayList<>(directors.values()));
                    }
                }
            } else if (addedFilm.getDirectors() != null) {
                String deleteDirectorsQuery = "DELETE film_directors WHERE film_id = ?";
                jdbcTemplate.update(deleteDirectorsQuery, film.getId());
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

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM film WHERE film_id = ?", id);

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


    @Override
    public List<Film> getPopularFilms(long limit, long genreId, int year) {
        SqlRowSet rowSet;
        StringBuilder sql = new StringBuilder("SELECT f.* FROM film AS f LEFT JOIN likes AS l ON f.film_id = l.film_id ");
        List<Object> params = new ArrayList<>();
        if (genreId != 0) {
            sql.append("LEFT JOIN genre_film AS gf ON f.film_id = gf.film_id ");
            sql.append("WHERE gf.genre_id = ? ");
            params.add(genreId);
            if (year != 0) {
                sql.append("AND YEAR(f.release_date) = ? ");
                params.add(year);
            }
        } else {
            if (year != 0) {
                sql.append("WHERE YEAR(f.release_date) = ? ");
                params.add(year);
            }
        }
        sql.append("GROUP BY f.film_id ORDER BY COUNT(l.user_id) DESC LIMIT ?;");
        params.add(limit);
        rowSet = jdbcTemplate.queryForRowSet(sql.toString(), params.toArray());

        List<Film> popularFilms = new ArrayList<>();

        while (rowSet.next()) {
            popularFilms.add(getFilmFromSqlRow(rowSet));
        }
        if (popularFilms.isEmpty()) {
            log.info("Фильмы не найдены.");
        }
        return popularFilms;
    }


    /**
     * Закрытый служебный метод используется для получения объекта
     * фильма из строки, полученной из базы данных (таблица 'film').
     *
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
        int ratingId = filmRows.getInt("rating_id");
        MPA rating = null;
        Set<Genre> genres = new HashSet<>();

        String ratingQuery = "SELECT * FROM rating WHERE rating_id = ?";
        SqlRowSet ratingRows = jdbcTemplate.queryForRowSet(ratingQuery, ratingId);
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

        String directorsQuery = "SELECT d.* FROM film_directors fd JOIN directors d ON fd.director_id = d.director_id WHERE fd.film_id = ?";
        SqlRowSet directorRows = jdbcTemplate.queryForRowSet(directorsQuery, filmId);
        List<Director> directors = new ArrayList<>();

        while (directorRows.next()) {
            directors.add(new Director(
                    directorRows.getLong("director_id"),
                    directorRows.getString("name")));
        }

        Film film = new Film(filmId, filmName, description, releaseDate, duration, rating, new ArrayList<>(genres), directors);

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

        SqlRowSet filmRow = jdbcTemplate.queryForRowSet("SELECT * FROM film WHERE film_id = ?", filmId);
        if (!filmRow.next())
            throw new NotFoundException("Фильм с id = " + filmId + " не найден!");

        SqlRowSet userRow = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE user_id = ?", userId);
        if (!userRow.next())
            throw new NotFoundException("Пользователь с id = " + filmId + " не найден!");

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

    public List<Film> getCommonFilms(long id, long otherId) {

        String userQuery = "SELECT * FROM users WHERE user_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(userQuery, id);
        SqlRowSet user2Rows = jdbcTemplate.queryForRowSet(userQuery, otherId);
        if (!userRows.next() || !user2Rows.next()) {
            log.debug("Пользователь не найден!");
            throw new NotFoundException("Пользователь не найден!");
        }

        List<Film> commonFilms = new ArrayList<>();

        String filmQuery = "(SELECT f.* " +
                "FROM likes l " +
                "JOIN film f ON l.film_id = f.film_id " +
                "WHERE user_id = ?)" +
                "INTERSECT" +
                "(SELECT f.* " +
                "FROM likes l " +
                "JOIN film f ON l.film_id = f.film_id " +
                "WHERE user_id = ?)";

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(filmQuery, id, otherId);

        while (filmRows.next()) {
            commonFilms.add(getFilmFromSqlRow(filmRows));
        }

        log.info("Список общих фильмов");
        return commonFilms;

    }

    @Override
    public List<Film> getDirectorFilms(long directorId, String sortBy) {

        SqlRowSet directorQuery = jdbcTemplate.queryForRowSet("SELECT * FROM directors WHERE director_id = ?", directorId);
        if (!directorQuery.next())
            throw new NotFoundException("Такого режиссёра нет в списке!");

        String sql;
        if ("year".equals(sortBy)) {
            sql =   "SELECT f.* " +
                    "FROM film f " +
                    "LEFT JOIN film_directors fd ON f.film_id = fd.film_id " +
                    "WHERE fd.director_id = ? " +
                    "ORDER BY release_date ";
        } else if ("likes".equals(sortBy)) {
            sql = "SELECT f.*, (SELECT COUNT(*) FROM likes WHERE film_id = f.film_id) like_amount " +
                    "FROM film f " +
                    "LEFT JOIN film_directors fd ON f.film_id = fd.film_id " +
                    "WHERE fd.director_id = ? " +
                    "ORDER BY like_amount ";
        } else
            throw new IllegalArgumentException("Неверный параметр");

        List<Film> films = new ArrayList<>();
        SqlRowSet filmsRows = jdbcTemplate.queryForRowSet(sql, directorId);
        while (filmsRows.next())
            films.add(getFilmFromSqlRow(filmsRows));

        return films;
    }

    @Override
    public List<Film> getFilmsBySubstring(String query, List<String> by) {
        List<Film> filmList = new ArrayList<>();

        if (by.contains("director") & by.contains("title")) {

            String filmsQuery = "SELECT * " +
                    "FROM FILM f  " +
                    "LEFT JOIN FILM_DIRECTORS fd ON f.FILM_ID = fd.FILM_ID " +
                    "LEFT JOIN DIRECTORS d ON fd.DIRECTOR_ID = d.DIRECTOR_ID " +
                    "WHERE (f.NAME ILIKE '%" + query + "%' OR d.NAME ILIKE '%" + query + "%') " +
                    "ORDER BY (SELECT COUNT(film_id) FROM likes WHERE film_id = f.film_id) DESC";

            SqlRowSet filmRows = jdbcTemplate.queryForRowSet(filmsQuery);

            while (filmRows.next()) {
                filmList.add(getFilmFromSqlRow(filmRows));
            }
            return filmList;

        } else if (by.contains("director")) {

            String filmsQuery = "SELECT * " +
                    "FROM FILM f  " +
                    "JOIN FILM_DIRECTORS fd ON f.FILM_ID = fd.FILM_ID " +
                    "JOIN DIRECTORS d ON fd.DIRECTOR_ID = d.DIRECTOR_ID " +
                    "WHERE  d.NAME ILIKE '%" + query + "%' " +
                    "ORDER BY (SELECT COUNT(film_id) FROM likes WHERE film_id = f.film_id) DESC";

            SqlRowSet filmRows = jdbcTemplate.queryForRowSet(filmsQuery);

            while (filmRows.next()) {
                filmList.add(getFilmFromSqlRow(filmRows));
            }
            return filmList;

        } else if (by.contains("title")) {

            String filmsQuery = "SELECT * " +
                    "FROM FILM f " +
                    "WHERE NAME ILIKE '%" + query + "%' " +
                    "ORDER BY (SELECT COUNT(film_id) FROM likes WHERE film_id = f.film_id) DESC";

            SqlRowSet filmRows = jdbcTemplate.queryForRowSet(filmsQuery);

            while (filmRows.next()) {
                filmList.add(getFilmFromSqlRow(filmRows));
            }
            return filmList;

        } else {
            log.debug("Не корректные параметры запроса");
            throw new BadRequestException("Не корректные параметры запроса");
        }
    }
}


