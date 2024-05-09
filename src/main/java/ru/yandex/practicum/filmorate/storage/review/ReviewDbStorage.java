package ru.yandex.practicum.filmorate.storage.review;

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

import java.sql.PreparedStatement;
import java.util.*;

/**
 * Класс ReviewDbStorage - реализация интерфейса ReviewStorage
 * которая взаимодействует с SQL базой данных для работы с сущностями-отзывами.
 */
@Component("reviewDbStorage")
public class ReviewDbStorage implements ReviewStorage {

    private final Logger log = LoggerFactory.getLogger(ReviewDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    public ReviewDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Review addReview(Review review) {

        if (review.getUserId() == null)
            throw new BadRequestException("Не передан идентификатор пользователя!");
        else if (review.getFilmId() == null)
            throw new BadRequestException("Не передан идентификатор фильма!");
        else if (review.getIsPositive() == null)
            throw new BadRequestException("Не передан тип отзыва!");

        SqlRowSet userRow = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE user_id = ?", review.getUserId());
        SqlRowSet filmRow = jdbcTemplate.queryForRowSet("SELECT * FROM film WHERE film_id = ?", review.getFilmId());

        if (!userRow.next())
            throw new NotFoundException("Передан неверный идентификатор пользователя: " + review.getUserId());

        if (!filmRow.next())
            throw new NotFoundException("Передан неверный идентификатор фильма: " + review.getFilmId());

        String insertReviewQuery =
                "INSERT INTO review (content, is_positive, user_id, film_id) VALUES (?, ?, ?, ?)";
        if (review.getIsPositive() == null)
            review.setIsPositive(true);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(insertReviewQuery, new String[]{"review_id"});
                stmt.setString(1, review.getContent());
                stmt.setBoolean(2, review.getIsPositive());
                stmt.setLong(3, review.getUserId());
                stmt.setLong(4, review.getFilmId());
                return stmt;
            }, keyHolder);
        } catch (DataIntegrityViolationException exception) {
            throw new DataIntegrityViolationException("Ошибка при создании объекта типа Review!");
        }

        long reviewId = keyHolder.getKey().longValue();
        review.setReviewId(reviewId);
        review.setUseful(0);

        log.info("Отзыв добавлен {} {}!", review.getReviewId(), review.getContent());
        return review;
    }


    @Override
    public Review updateReview(Review review) {

        String findReviewQuery = "SELECT * FROM review WHERE review_id = ?";
        SqlRowSet reviewRows = jdbcTemplate.queryForRowSet(findReviewQuery, review.getReviewId());
        if (!reviewRows.next()) {
            throw new NotFoundException("Отзыв не найден!");
        } else {

            Review addedReview = getReview(reviewRows.getLong("review_id"));
            if (!review.getContent().equals(addedReview.getContent())) {
                String contentQuery = "UPDATE review " +
                                      "SET content = ? " +
                                      "WHERE review_id = ?;";
                jdbcTemplate.update(contentQuery, review.getContent(), review.getReviewId());
            }

            if (review.getIsPositive() != addedReview.getIsPositive()) {
                String isPositiveQuery = "UPDATE review " +
                                         "SET is_positive = ? " +
                                         "WHERE review_id = ?;";
                jdbcTemplate.update(isPositiveQuery, review.getIsPositive(), review.getReviewId());
            }

            if (!review.getUserId().equals(addedReview.getUserId())) {
                review.setUserId(addedReview.getUserId());
            }

            if (!review.getFilmId().equals(addedReview.getFilmId())) {
                review.setFilmId(addedReview.getFilmId());
            }

            String usefulReviewQuery = "SELECT COUNT(is_useful) as usefulness FROM review_rate WHERE review_id = ? AND is_useful = true;";
            String unusefulReviewQuery = "SELECT COUNT(is_useful) as usefulness FROM review_rate WHERE review_id = ? AND is_useful = false;";

            SqlRowSet usefulRow = jdbcTemplate.queryForRowSet(usefulReviewQuery, review.getReviewId());
            SqlRowSet unusefulRow = jdbcTemplate.queryForRowSet(unusefulReviewQuery, review.getReviewId());

            int useful = 0;
            int unuseful = 0;
            if (usefulRow.next())
                useful = usefulRow.getInt("usefulness");
            if (unusefulRow.next())
                unuseful = unusefulRow.getInt("usefulness");

            review.setUseful(useful - unuseful);
        }
        return review;
    }


    @Override
    public void deleteReview(long id) {

        String sqlQuery = "DELETE FROM review WHERE review_id = ?";
        if (jdbcTemplate.update(sqlQuery, id) > 0)
            log.info("Удален отзыв с id = {}", id);
        else {
            log.info("Отзыв с id = {} не удален или отсутствует в списке", id);
            throw new NotFoundException("Отзыв с id = " + id + " не найден!");
        }

    }


    @Override
    public Review getReview(long id) {

        SqlRowSet reviewRows = jdbcTemplate.queryForRowSet("SELECT * FROM review WHERE review_id = ?", id);

        if (reviewRows.next()) {
            return getReviewFromSqlRow(reviewRows);
        } else {
            log.info("Отзыв с идентификатором {} не найден.", id);
            throw new NotFoundException("Отзыв не найден!");
        }

    }


    @Override
    public boolean containsReview(long id) {
        SqlRowSet reviewRows = jdbcTemplate.queryForRowSet("SELECT * FROM review WHERE review_id = ?", id);
        return reviewRows.next();
    }


    @SuppressWarnings("checkstyle:Regexp")
    @Override
    public List<Review> getReviews(Long filmId, int count) {

        List<Review> reviews = new ArrayList<>();

        String filmQuery = "SELECT * FROM film WHERE film_id = ?";

        if (filmId != -1) {
            SqlRowSet filmRow = jdbcTemplate.queryForRowSet(filmQuery, filmId);
            if (!filmRow.next())
                throw new NotFoundException("Фильм с id = " + filmId + " не найден!");
        }

        //Я не знаю, каким образом можно перекроить этот запрос с использованием JOIN.
        //Разве что разбить его на 3 отдельных запроса к БД, но это ведь ещё сильнее сожрет производительность...
        StringBuilder reviewsQuery = new StringBuilder("SELECT r.*, " +
                "  (SELECT COUNT(is_useful) " +
                "  FROM review_rate " +
                "  WHERE review_id = r.review_id AND is_useful = TRUE) " +
                "  - " +
                "  (SELECT COUNT(is_useful) " +
                "  FROM review_rate " +
                "  WHERE review_id = r.review_id AND is_useful = FALSE) as usefulness " +
                "  FROM review r ");

        if (filmId != -1) {
            reviewsQuery.append("WHERE film_id = ? ");
        }

        reviewsQuery.append("ORDER BY usefulness DESC LIMIT ?");

        SqlRowSet reviewRows;
        if (filmId != -1)
            reviewRows = jdbcTemplate.queryForRowSet(reviewsQuery.toString(), filmId, count);
        else
            reviewRows = jdbcTemplate.queryForRowSet(reviewsQuery.toString(), count);

        while (reviewRows.next()) {
            reviews.add(getReviewFromSqlRow(reviewRows));
        }

        if (reviews.isEmpty())
            log.info("Отзывы не найдены.");

        return reviews;
    }


    /**
     * Закрытый служебный метод используется для получения объекта
     * отзыва из строки, полученной из базы данных (таблица 'review').
     * @param reviewRows
     * @return
     */
    @SuppressWarnings("checkstyle:Regexp")
    private Review getReviewFromSqlRow(SqlRowSet reviewRows) {

        int reviewId = reviewRows.getInt("review_id");
        String content = reviewRows.getString("content");
        boolean isPositive = reviewRows.getBoolean("is_positive");
        long userId = reviewRows.getInt("user_id");
        long filmId = reviewRows.getInt("film_id");

        String usefulReviewQuery = "SELECT COUNT(is_useful) as usefulness FROM review_rate WHERE review_id = ? AND is_useful = true;";
        String unusefulReviewQuery = "SELECT COUNT(is_useful) as usefulness FROM review_rate WHERE review_id = ? AND is_useful = false;";

        SqlRowSet usefulRow = jdbcTemplate.queryForRowSet(usefulReviewQuery, reviewId);
        SqlRowSet unusefulRow = jdbcTemplate.queryForRowSet(unusefulReviewQuery, reviewId);

        int useful = 0;
        int unuseful = 0;
        if (usefulRow.next())
            useful = usefulRow.getInt("usefulness");
        if (unusefulRow.next())
            unuseful = unusefulRow.getInt("usefulness");

        Review review = new Review(reviewId, content, isPositive, userId, filmId, useful - unuseful);

        log.info("Найден отзыв на фильм с id {} от пользователя с id {}", filmId, userId);

        return review;
    }


    @Override
    public Long addRateForReview(Long reviewId, Long userId, boolean isUseful) {

        String reviewQuery = "SELECT * FROM review WHERE review_id = ?";
        SqlRowSet reviewRows = jdbcTemplate.queryForRowSet(reviewQuery, reviewId);
        if (!reviewRows.next())
            throw new NotFoundException("Отзыв не найден!");

        String userQuery = "SELECT * FROM users WHERE user_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(userQuery, userId);
        if (!userRows.next())
            throw new NotFoundException("Пользователь не найден!");

        String insertRateQuery = "INSERT INTO review_rate (review_id, user_id, is_useful) VALUES (?, ?, ?)";
        try {
            jdbcTemplate.update(insertRateQuery, reviewId, userId, isUseful);
            if (isUseful)
                log.info("Лайк от пользователя с id {} добавлен отзыву с id {}!", userId, reviewQuery);
            else
                log.info("Дизлайк от пользователя с id {} добавлен отзыву с id {}!", userId, reviewQuery);

        } catch (DataIntegrityViolationException exception) {
            throw new DataIntegrityViolationException("Оценка от данного пользователя уже поставлена этому отзыву!");
        }

        return reviewId;

    }


    @Override
    public Long deleteRateFromReview(Long reviewId, Long userId, boolean isUseful) {

        String findRateQuery = "SELECT * FROM review_rate WHERE review_id = ? AND user_id = ? AND is_useful = ?";
        SqlRowSet findRateRow = jdbcTemplate.queryForRowSet(findRateQuery, reviewId, userId, isUseful);
        if (!findRateRow.next())
            throw new NotFoundException("Оценка отзыва отсутствует!");

        String deleteRateQuery = "DELETE FROM review_rate WHERE review_id = ? AND user_id = ?";
        int amount = jdbcTemplate.update(deleteRateQuery, reviewId, userId);

        if (amount > 0)
            log.info("Оценка от пользователя с id {} удалена с отзыва с id {}!", userId, reviewId);
        else
            log.info("Оценка отсутствует в списке!");

        return reviewId;
    }
}
