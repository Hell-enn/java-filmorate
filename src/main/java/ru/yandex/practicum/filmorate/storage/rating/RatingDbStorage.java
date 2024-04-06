package ru.yandex.practicum.filmorate.storage.rating;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс RatingDbStorage - реализация интерфейса RatingStorage
 * которая взаимодействует с SQL базой данных для работы с сущностями-возрастными рейтингами.
 */
@Component("ratingDbStorage")
public class RatingDbStorage implements RatingStorage {

    private final Logger log = LoggerFactory.getLogger(RatingDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    public RatingDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }


    @Override
    public void addRating(MPA rating) {

        String insertRatingQuery = "INSERT INTO rating (name, description) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(insertRatingQuery, new String[]{"rating_id"});
            stmt.setString(1, rating.getName());
            stmt.setString(2, rating.getDescription());
            return stmt;
        }, keyHolder);

        rating.setId(keyHolder.getKey().longValue());

        log.info("Рейтинг добавлен {} {}!", rating.getId(), rating.getName());

    }


    @Override
    public void deleteRating(long id) {

        String sqlQuery = "DELETE FROM rating WHERE rating_id = ?";
        if (jdbcTemplate.update(sqlQuery, id) > 0)
            log.info("Удален рейтинг с id = {}", id);
        else
            log.info("Рейтинг с id = {} не удален или отсутствует в списке", id);

    }


    @Override
    public MPA getRating(long id) {
        SqlRowSet ratingRows = jdbcTemplate.queryForRowSet("SELECT * " +
                "FROM rating " +
                "WHERE rating_id = ?", id);

        if(ratingRows.next()) {
            return getRatingFromSqlRow(ratingRows);
        } else {
            log.info("Рейтинг с идентификатором {} не найден.", id);
            return null;
        }
    }


    @Override
    public boolean containsRating(long id) {
        return jdbcTemplate.queryForRowSet("SELECT rating_id FROM rating WHERE rating_id = ?", id).next();
    }


    @Override
    public List<MPA> getRatings() {

        List<MPA> ratings = new ArrayList<>();

        SqlRowSet ratingRows = jdbcTemplate.queryForRowSet("SELECT * FROM rating");

        while (ratingRows.next()) {
            ratings.add(getRatingFromSqlRow(ratingRows));
        }

        if (ratings.isEmpty())
            log.info("Рейтинги не найдены.");

        return ratings;
    }


    @Override
    public MPA getFilmRating(Long filmId) {

        SqlRowSet ratingRows = jdbcTemplate.queryForRowSet("SELECT * " +
                                                               "FROM rating " +
                                                               "WHERE rating_id = (SELECT rating_id FROM film WHERE film_id = ?)");

        if (ratingRows.next()) {
            MPA rating = getRatingFromSqlRow(ratingRows);
            log.info("Рейтинг фильма с id = {} найден: {}", filmId, rating.getName());
            return rating;
        }

        log.info("Рейтинг не найден.");
        return null;
    }


    /**
     * Закрытый служебный метод используется для получения объекта
     * возрастного рейтинга из строки, полученной из базы данных (таблица 'rating').
     * @param ratingRows
     * @return
     */
    private MPA getRatingFromSqlRow(SqlRowSet ratingRows) {

        int ratingId = ratingRows.getInt("rating_id");
        String name = ratingRows.getString("name");
        String description = ratingRows.getString("description");

        MPA rating = new MPA(ratingId, name, description);

        log.info("Найден рейтинг: {} {}", rating.getId(), rating.getName());

        return rating;
    }
}
