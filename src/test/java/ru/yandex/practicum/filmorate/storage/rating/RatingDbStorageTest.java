package ru.yandex.practicum.filmorate.storage.rating;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RatingDbStorageTest {

    private final JdbcTemplate jdbcTemplate;
    private RatingDbStorage ratingStorage;

    @BeforeEach
    public void createContextBefore() {
        ratingStorage = new RatingDbStorage(jdbcTemplate);
    }

    @Test
    public void findAndDeleteRatingByIdTest() {

        MPA newRating = new MPA(1L, "Рейтинг 1", "Описание 1");

        ratingStorage.addRating(newRating);
        MPA savedRating = ratingStorage.getRating(6);

        assertThat(savedRating)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newRating);

        ratingStorage.deleteRating(6);

        assertThat(ratingStorage.getRating(6))
                .isNull();
    }


    @Test
    public void containsRatingTest() {

        MPA newRating = new MPA(1L, "Рейтинг 1", "Описание 1");

        ratingStorage.addRating(newRating);

        assertThat(ratingStorage.containsRating(7))
                .isTrue();
    }


    @Test
    public void getRatingTest() {

        MPA oldRating1 = new MPA(1L, "G", "Нет возрастных ограничений");
        MPA oldRating2 = new MPA(2L, "PG", "Рекомендуется присутствие родителей");
        MPA oldRating3 = new MPA(3L, "PG-13", "Детям до 13 лет просмотр не желателен");
        MPA oldRating4 = new MPA(4L, "R", "Лицам до 17 лет обязательно присутствие взрослого");
        MPA oldRating5 = new MPA(5L, "NC-17", "Лицам до 18 лет просмотр запрещен");

        MPA newRating1 = new MPA(6L, "Жанр 1", "Описание 1");
        MPA newRating2 = new MPA(7L, "Жанр 2", "Описание 2");
        MPA newRating3 = new MPA(8L, "Жанр 3", "Описание 3");
        MPA newRating4 = new MPA(9L, "Жанр 4", "Описание 4");
        MPA newRating5 = new MPA(10L, "Жанр 5", "Описание 5");

        List<MPA> ratings = List.of(
                oldRating1, oldRating2, oldRating3, oldRating4, oldRating5,
                newRating1, newRating2, newRating3, newRating4, newRating5);

        ratingStorage.addRating(newRating1);
        ratingStorage.addRating(newRating2);
        ratingStorage.addRating(newRating3);
        ratingStorage.addRating(newRating4);
        ratingStorage.addRating(newRating5);

        List<MPA> savedRatings = ratingStorage.getRatings();

        assertThat(ratings)
                .isEqualTo(savedRatings);
    }
}
