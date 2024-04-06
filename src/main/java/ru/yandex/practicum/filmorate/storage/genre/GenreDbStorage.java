package ru.yandex.practicum.filmorate.storage.genre;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс GenreDbStorage - реализация интерфейса GenreStorage
 * которая взаимодействует с SQL базой данных для работы с сущностями-жанрами фильмов.
 */
@Component("genreDbStorage")
public class GenreDbStorage implements GenreStorage {
    private final Logger log = LoggerFactory.getLogger(GenreDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void addGenre(Genre genre) {

        String insertGenreQuery = "INSERT INTO genre (name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(insertGenreQuery, new String[]{"genre_id"});
            stmt.setString(1, genre.getName());
            return stmt;
        }, keyHolder);

        genre.setId(keyHolder.getKey().longValue());

        log.info("Жанр {} добавлен!", genre.getName());

    }


    @Override
    public void deleteGenre(long id) {

        String sqlQuery = "DELETE FROM genre WHERE genre_id = ?";
        if (jdbcTemplate.update(sqlQuery, id) > 0)
            log.info("Удален жанр с id = {}", id);
        else
            log.info("Жанр с id = {} не удален или отсутствует в списке", id);

    }


    @Override
    public Genre getGenre(long id) {

        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * " +
                "FROM genre " +
                "WHERE genre_id = ?", id);

        if (genreRows.next()) {
            log.info("Жанр с идентификатором {} найден.", id);
            return getGenreFromSqlRow(genreRows);
        } else {
            log.info("Жанр с идентификатором {} не найден.", id);
            return null;
        }

    }


    @Override
    public boolean containsGenre(long id) {
        return jdbcTemplate.queryForRowSet("SELECT genre_id FROM genre WHERE genre_id = ?", id).next();
    }


    @Override
    public List<Genre> getGenres() {

        List<Genre> genres = new ArrayList<>();

        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM genre");

        while (genreRows.next()) {
            genres.add(getGenreFromSqlRow(genreRows));
        }

        if (genres.isEmpty())
            log.info("Жанры не найдены.");

        return genres;

    }


    @Override
    public List<Genre> getFilmGenres(long filmId) {

        List<Genre> genres = new ArrayList<>();

        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * " +
                                                              "FROM genre " +
                                                              "WHERE genre_id IN (SELECT genre_id FROM genre_film WHERE film_id = ?)");

        while (genreRows.next()) {
            genres.add(getGenreFromSqlRow(genreRows));
        }

        if (genres.isEmpty())
            log.info("Жанры не найдены.");

        return genres;

    }


    /**
     * Закрытый служебный метод используется для получения объекта
     * жанра фильма из строки, полученной из базы данных (таблица 'genre').
     * @param genreRows
     * @return
     */
    private Genre getGenreFromSqlRow(SqlRowSet genreRows) {

        int genreId = genreRows.getInt("genre_id");
        String name = genreRows.getString("name");

        Genre genre = new Genre(genreId, name);

        log.info("Найден жанр: {} {}", genre.getId(), genre.getName());

        return genre;
    }
}
