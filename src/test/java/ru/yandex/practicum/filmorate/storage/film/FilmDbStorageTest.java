package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {

    private final JdbcTemplate jdbcTemplate;
    private FilmDbStorage filmStorage;
    private UserStorage userStorage;

    @BeforeEach
    public void createContextBefore() {
        filmStorage = new FilmDbStorage(jdbcTemplate);
        userStorage = new UserDbStorage(jdbcTemplate, filmStorage);

        jdbcTemplate.update("DELETE FROM directors");
        jdbcTemplate.update("INSERT INTO directors (director_id, name) VALUES (1, 'Режисcер 1'), (2, 'Режисер 2')");
    }

    @Test
    public void findFilmByIdTest() {

        MPA g = new MPA(1, "G", "Нет возрастных ограничений");
        List<Genre> genres = List.of(new Genre(1, "Комедия"), new Genre(2, "Драма"));
        List<Director> directors = List.of(new Director(1L, "Режисcер 1"));
        Film newFilm = new Film(6L, "Фильм 1", "Описание 1",
                LocalDate.of(1990, 1, 1), 200, g, genres, directors);

        newFilm.setId(filmStorage.addFilm(newFilm).getId());
        Film savedFilm = filmStorage.getFilm(newFilm.getId());

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm);

    }

    @Test
    public void deleteFilmTest() {

        MPA g = new MPA(1, "G", "Нет возрастных ограничений");
        List<Genre> genres = List.of(new Genre(1, "Комедия"), new Genre(1, "Драма"));
        List<Director> directors = List.of(new Director(1L, "Режисcер 1"));
        Film newFilm = new Film(6L, "Фильм 1", "Описание 1",
                LocalDate.of(1990, 1, 1), 200, g, genres, directors);

        filmStorage.addFilm(newFilm);
        filmStorage.deleteFilm(6L);

        assertThat(filmStorage.getFilm(6L))
                .isNull();

    }

    @Test
    public void containsFilmTest() {

        MPA g = new MPA(1, "G", "Нет возрастных ограничений");
        List<Genre> genres = List.of(new Genre(1, "Комедия"), new Genre(2, "Драма"));
        List<Director> directors = List.of(new Director(1L, "Режисcер 1"));
        Film newFilm = new Film(6L, "Фильм 1", "Описание 1",
                LocalDate.of(1990, 1, 1), 200, g, genres, directors);

        newFilm.setId(filmStorage.addFilm(newFilm).getId());

        assertThat(filmStorage.containsFilm(newFilm.getId()))
                .isTrue();

    }

    @Test
    public void getFilmsTest() {

        MPA g = new MPA(1, "G", "Нет возрастных ограничений");
        MPA pg = new MPA(2, "PG", "Рекомендуется присутствие родителей");
        MPA pg13 = new MPA(3, "PG-13", "Детям до 13 лет просмотр не желателен");
        MPA r = new MPA(4, "R", "Лицам до 17 лет обязательно присутствие взрослого");
        MPA nc17 = new MPA(5, "NC-17", "Лицам до 18 лет просмотр запрещен");

        Genre comedy = new Genre(1, "Комедия");
        Genre drama = new Genre(2, "Драма");
        Genre cartoon = new Genre(3, "Мультфильм");
        Genre thriller = new Genre(4, "Триллер");
        Genre documentary = new Genre(5, "Документальный");
        Genre action = new Genre(6, "Боевик");

        List<Genre> genres1 = new ArrayList<>();
        genres1.add(comedy);
        genres1.add(drama);

        List<Genre> genres2 = new ArrayList<>();
        genres2.add(thriller);
        genres2.add(action);

        List<Genre> genres3 = new ArrayList<>();
        genres3.add(drama);

        List<Genre> genres4 = new ArrayList<>();
        genres4.add(documentary);

        List<Genre> genres5 = new ArrayList<>();
        genres5.add(cartoon);
        genres5.add(comedy);

        jdbcTemplate.update("DELETE FROM FILM");

        List<Director> director1 = List.of(new Director(1L, "Режисcер 1"));
        List<Director> director2 = List.of(new Director(1L, "Режисcер 2"));
        List<Director> director3 = List.of(new Director(1L, "Режисcер 3"));
        List<Director> director4 = List.of(new Director(1L, "Режисcер 4"));
        List<Director> director5 = List.of(new Director(1L, "Режисcер 5"));

        Film newFilm1 = new Film(6L, "Фильм 6", "Описание 6",
                LocalDate.of(1990, 1, 1), 200, g, genres1, director1);
        Film newFilm2 = new Film(7L, "Фильм 7", "Описание 7",
                LocalDate.of(1991, 2, 5), 190, pg, genres2,  director2);
        Film newFilm3 = new Film(8L, "Фильм 8", "Описание 8",
                LocalDate.of(1992, 3, 4), 180, pg13, genres3,  director3);
        Film newFilm4 = new Film(9L, "Фильм 9", "Описание 9",
                LocalDate.of(1993, 4, 3), 170, r, genres4,  director4);
        Film newFilm5 = new Film(10L, "Фильм 10", "Описание 10",
                LocalDate.of(1994, 5, 2), 160, nc17, genres5,  director5);

        List<Film> films = List.of(newFilm1, newFilm2, newFilm3, newFilm4, newFilm5);

        newFilm1.setId(filmStorage.addFilm(newFilm1).getId());
        newFilm2.setId(filmStorage.addFilm(newFilm2).getId());
        newFilm3.setId(filmStorage.addFilm(newFilm3).getId());
        newFilm4.setId(filmStorage.addFilm(newFilm4).getId());
        newFilm5.setId(filmStorage.addFilm(newFilm5).getId());

        List<Film> savedFilms = filmStorage.getFilms();

        assertThat(films)
                .isEqualTo(savedFilms);

    }

    @Test
    public void getLikesTest() {

        MPA g = new MPA(1, "G", "Нет возрастных ограничений");
        MPA pg = new MPA(2, "G", "Рекомендуется присутствие родителей");
        MPA pg13 = new MPA(3, "G", "Детям до 13 лет просмотр не желателен");
        MPA r = new MPA(4, "G", "Лицам до 17 лет обязательно присутствие взрослого");
        MPA nc17 = new MPA(5, "G", "Лицам до 18 лет просмотр запрещен");

        Genre comedy = new Genre(1, "Комедия");
        Genre drama = new Genre(2, "Драма");
        Genre cartoon = new Genre(3, "Мультфильм");
        Genre thriller = new Genre(4, "Триллер");
        Genre documentary = new Genre(5, "Документальный");
        Genre action = new Genre(6, "Боевик");

        List<Genre> genres1 = List.of(comedy, drama);
        List<Genre> genres2 = List.of(thriller, action);
        List<Genre> genres3 = List.of(drama);
        List<Genre> genres4 = List.of(documentary);
        List<Genre> genres5 = List.of(cartoon, comedy);

        List<Director> director1 = List.of(new Director(1L, "Режисcер 1"));
        List<Director> director2 = List.of(new Director(1L, "Режисcер 2"));
        List<Director> director3 = List.of(new Director(1L, "Режисcер 3"));
        List<Director> director4 = List.of(new Director(1L, "Режисcер 4"));
        List<Director> director5 = List.of(new Director(1L, "Режисcер 5"));

        jdbcTemplate.update("DELETE FROM film");

        Film newFilm1 = new Film(1L, "Фильм 1", "Описание 1",
                LocalDate.of(1990, 1, 1), 200, g, genres1, director1);
        Film newFilm2 = new Film(2L, "Фильм 2", "Описание 2",
                LocalDate.of(1991, 2, 5), 190, pg, genres2, director2);
        Film newFilm3 = new Film(3L, "Фильм 3", "Описание 3",
                LocalDate.of(1992, 3, 4), 180, pg13, genres3, director3);
        Film newFilm4 = new Film(4L, "Фильм 4", "Описание 4",
                LocalDate.of(1993, 4, 3), 170, r, genres4, director4);
        Film newFilm5 = new Film(5L, "Фильм 5", "Описание 5",
                LocalDate.of(1994, 5, 2), 160, nc17, genres5, director5);

        newFilm1.setId(filmStorage.addFilm(newFilm1).getId());
        newFilm2.setId(filmStorage.addFilm(newFilm2).getId());
        newFilm3.setId(filmStorage.addFilm(newFilm3).getId());
        newFilm4.setId(filmStorage.addFilm(newFilm4).getId());
        newFilm5.setId(filmStorage.addFilm(newFilm5).getId());

        jdbcTemplate.update("DELETE FROM users");

        User newUser1 = new User(1L, "user1@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1));
        User newUser2 = new User(2L, "user2@email.ru", "oksana99", "Oksana Samoylova", LocalDate.of(1991, 2, 2));
        User newUser3 = new User(3L, "user3@email.ru", "valera15", "Valera Siniy", LocalDate.of(1992, 3, 3));
        User newUser4 = new User(4L, "user4@email.ru", "artur67", "Artur Pirozhkov", LocalDate.of(1993, 4, 4));
        User newUser5 = new User(5L, "user5@email.ru", "viktoriya17", "Viktoriya Polyanskaya", LocalDate.of(1994, 5, 5));

        newUser1.setId(userStorage.addUser(newUser1).getId());
        newUser2.setId(userStorage.addUser(newUser2).getId());
        newUser3.setId(userStorage.addUser(newUser3).getId());
        newUser4.setId(userStorage.addUser(newUser4).getId());
        newUser5.setId(userStorage.addUser(newUser5).getId());

        filmStorage.addLike(newFilm1.getId(), newUser1.getId());
        filmStorage.addLike(newFilm1.getId(), newUser3.getId());
        filmStorage.addLike(newFilm1.getId(), newUser4.getId());
        filmStorage.addLike(newFilm2.getId(), newUser1.getId());
        filmStorage.addLike(newFilm2.getId(), newUser2.getId());
        filmStorage.addLike(newFilm3.getId(), newUser5.getId());
        filmStorage.addLike(newFilm3.getId(), newUser4.getId());
        filmStorage.addLike(newFilm4.getId(), newUser1.getId());
        filmStorage.addLike(newFilm5.getId(), newUser2.getId());
        filmStorage.addLike(newFilm5.getId(), newUser3.getId());

        Set<Like> likes = Set.of(new Like(newFilm1.getId(), newUser1.getId()),
                new Like(newFilm1.getId(), newUser3.getId()),
                new Like(newFilm1.getId(), newUser4.getId()));

        Set<Like> likesDB = filmStorage.getLikes((int) newFilm1.getId());

        assertThat(likes)
                .isEqualTo(likesDB);

        filmStorage.deleteLike(newFilm1.getId(), newUser1.getId());
        Set<Like> newlikes = Set.of(
                new Like(newFilm1.getId(), newUser3.getId()),
                new Like(newFilm1.getId(), newUser4.getId()));

        assertThat(filmStorage.getLikes((int) newFilm1.getId()))
                .isEqualTo(newlikes);

    }

    @Test
    public void updateFilmTest() {

        MPA g = new MPA(1, "G", "Нет возрастных ограничений");
        MPA pg13 = new MPA(3, "PG-13", "Детям до 13 лет просмотр не желателен");

        Genre comedy = new Genre(1, "Комедия");
        Genre drama = new Genre(2, "Драма");
        Genre thriller = new Genre(4, "Триллер");
        Genre action = new Genre(6, "Боевик");

        List<Genre> genres1 = new ArrayList<>();
        genres1.add(comedy);
        genres1.add(drama);

        List<Genre> genres2 = new ArrayList<>();
        genres2.add(thriller);
        genres2.add(action);

        List<Director> directors1 = List.of(new Director(1L, "Режиссёр 1"));

        jdbcTemplate.update("DELETE FROM film");

        Film newFilm1 = new Film(1L, "Фильм 1", "Описание 1",
                LocalDate.of(1990, 1, 1), 200, g, genres1, directors1);

        newFilm1.setId(filmStorage.addFilm(newFilm1).getId());

        assertThat(filmStorage.getFilm(newFilm1.getId()))
                .isEqualTo(newFilm1);

        newFilm1.setMpa(pg13);
        newFilm1.setGenres(genres2);

        filmStorage.updateFilm(newFilm1);

        assertThat(filmStorage.getFilm(newFilm1.getId()))
                .isEqualTo(newFilm1);

    }
}
