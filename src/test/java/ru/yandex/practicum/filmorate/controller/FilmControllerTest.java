package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmControllerTest {

    private static FilmController filmController;

    @BeforeAll
    static void createContext() {
        filmController = new FilmController();
    }

    @Test
    void postOrPutFilm() {

        Film film = new Film(1, "Название", "Описание", LocalDate.of(2022, 10, 22), 140);
        filmController.postFilm(film);
        assertEquals(1, filmController.getFilms().size());
        assertEquals(film, filmController.getFilms().get(0));

        filmController.postFilm(film);
        assertEquals(1, filmController.getFilms().size());

        Film updatedFilm = new Film(1, "Название", "Описание 2", LocalDate.of(2022, 10, 22), 140);
        filmController.postFilm(updatedFilm);
        assertEquals(1, filmController.getFilms().size());
        Assertions.assertNotEquals(updatedFilm, filmController.getFilms().get(0));

        filmController.putFilm(updatedFilm);
        assertEquals(1, filmController.getFilms().size());
        assertEquals(updatedFilm, filmController.getFilms().get(0));

        Film film2 = new Film(2, "Название 2", "Описание 2", LocalDate.of(2018, 1, 1), 115);
        filmController.postFilm(film2);
        assertEquals(2, filmController.getFilms().size());
        assertEquals(film2, filmController.getFilms().get(1));

        Film updatedFilm2 = new Film(2, "Название 2", "Обновленное описание 2", LocalDate.of(2018, 1, 1), 115);
        filmController.postFilm(updatedFilm2);
        assertEquals(2, filmController.getFilms().size());
        Assertions.assertNotEquals(updatedFilm2, filmController.getFilms().get(1));

        filmController.putFilm(updatedFilm2);
        assertEquals(2, filmController.getFilms().size());
        assertEquals(updatedFilm2, filmController.getFilms().get(1));

    }


    @Test
    void validateFilm() {

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.postFilm(null));

        assertEquals("Вы не передали информацию о фильме!", exception.getMessage());

        Film film1 = new Film(1, null, "vvv", LocalDate.of(2023, 12, 12), 122);
        Film film2 = new Film(1, "", "vvv", LocalDate.of(2023, 12, 12), 122);
        Film film3 = new Film(1, "Film", "", LocalDate.of(2023, 12, 12), 122);
        Film film4 = new Film(1, "Film", null, LocalDate.of(2023, 12, 12), 122);
        Film film5 = new Film(1, "Film", "vvv", LocalDate.of(1600, 12, 12), 122);
        Film film6 = new Film(1, "Film", "vvv", LocalDate.of(2023, 12, 12), -1);

        filmController.postFilm(film1);
        filmController.postFilm(film2);
        filmController.postFilm(film3);
        filmController.postFilm(film4);

        exception = assertThrows(
                ValidationException.class,
                () -> filmController.postFilm(film5));

        assertEquals("Фильм не мог быть выпущен до 28 декабря 1895!", exception.getMessage());

        filmController.postFilm(film6);
    }
}
