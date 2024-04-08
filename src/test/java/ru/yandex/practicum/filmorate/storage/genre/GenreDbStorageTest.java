package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTest {

    private final JdbcTemplate jdbcTemplate;
    private GenreDbStorage genreStorage;

    @BeforeEach
    public void createContextBefore() {
        genreStorage = new GenreDbStorage(jdbcTemplate);
    }

    @Test
    public void findGenreByIdTest() {

        Genre newGenre = new Genre(7L, "Жанр 7");
        newGenre.setId(genreStorage.addGenre(newGenre).getId());

        Genre savedGenre = genreStorage.getGenre(newGenre.getId());

        assertThat(savedGenre)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newGenre);
    }


    @Test
    public void deleteGenreTest() {

        Genre newGenre = new Genre(7L, "Жанр 7");

        genreStorage.addGenre(newGenre);
        genreStorage.deleteGenre(7);

        assertThat(genreStorage.getGenre(7))
                .isNull();
    }


    @Test
    public void containsGenreTest() {

        Genre newGenre = new Genre(7L, "Жанр 7");
        newGenre.setId(genreStorage.addGenre(newGenre).getId());

        assertThat(genreStorage.containsGenre(newGenre.getId()))
                .isTrue();
    }


    @Test
    public void getGenresTest() {

        Genre oldGenre1 = new Genre(1L, "Комедия");
        Genre oldGenre2 = new Genre(2L, "Драма");
        Genre oldGenre3 = new Genre(3L, "Мультфильм");
        Genre oldGenre4 = new Genre(4L, "Триллер");
        Genre oldGenre5 = new Genre(5L, "Документальный");
        Genre oldGenre6 = new Genre(6L, "Боевик");

        Genre newGenre1 = new Genre(7L, "Жанр 1");
        Genre newGenre2 = new Genre(8L, "Жанр 2");
        Genre newGenre3 = new Genre(9L, "Жанр 3");
        Genre newGenre4 = new Genre(10L, "Жанр 4");
        Genre newGenre5 = new Genre(11L, "Жанр 5");

        List<Genre> genres = List.of(
                oldGenre1, oldGenre2, oldGenre3, oldGenre4, oldGenre5, oldGenre6,
                newGenre1, newGenre2, newGenre3, newGenre4, newGenre5);

        genreStorage.addGenre(newGenre1);
        genreStorage.addGenre(newGenre2);
        genreStorage.addGenre(newGenre3);
        genreStorage.addGenre(newGenre4);
        genreStorage.addGenre(newGenre5);

        List<Genre> savedGenres = genreStorage.getGenres();

        assertThat(genres)
                .isEqualTo(savedGenres);
    }
}
