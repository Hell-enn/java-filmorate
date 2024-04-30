package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.*;

/**
 * Класс FilmService предоставляет функциональность по
 * взаимодействию со списком фильмов и их лайков у объектов типа Film
 * (добавление, удаление, вывод фильмов и набора 10 наиболее популярных
 * фильмов, лайков).
 */
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmDbStorage;


    /**
     * Метод добавляет фильм в список в случае, если он там отсутствует.
     * В противном случае возвращает null.
     * @param film
     * @return
     */
    public Film postFilm(Film film) {

        validateFilm(film);

        if (filmDbStorage.containsFilm(film.getId())) {
            return null;
        }

        return filmDbStorage.addFilm(film);
    }


    /**
     * Метод обновляет фильм в списке в случае, если он там присутствует.
     * В противном случае выбрасывает исключение типа ValidationException.
     * @param film
     * @return
     */
    public Film putFilm(Film film) {

        return filmDbStorage.updateFilm(film);

    }


    /**
     * Метод возвращает список фильмов из хранилища.
     * @return
     */
    public List<Film> getFilms() {

        return filmDbStorage.getFilms();

    }


    /**
     * Закрытый служебный метод проверяет объект типа Film
     * на соответствие ряду условий. Используется впоследствие
     * для валидации объекта типа Film при попытке его добавления
     * или обновления в списке.
     * В случае неудачи выбрасывает исключение ValidationException
     * с сообщением об ошибке.
     * @param film
     */
    private void validateFilm(Film film) {

        String message = "";

        if (film == null)
            message = "Вы не передали информацию о фильме!";
        else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))
            message = "Фильм не мог быть выпущен до 28 декабря 1895 года!";

        if (!message.isBlank()) {
            throw new ValidationException(message);
        }
    }


    /**
     * Метод добавляет в список лайков объекта film
     * id объекта user.
     * @param userId
     * @param filmId
     */
    public void addLike(long userId, long filmId) {

        Film film = filmDbStorage.getFilm(filmId);

        if (film == null)
            throw new NotFoundException("Фильм отсутствует в списке!");

        filmDbStorage.addLike(filmId, userId);
    }


    /**
     * Метод удаляет из списка лайков объекта film
     * id объекта user.
     * @param userId
     * @param filmId
     */
    public void deleteLike(long userId, long filmId) {

        Film film = filmDbStorage.getFilm(filmId);

        if (film == null)
            throw new NotFoundException("Фильм отсутствует в списке!");

        filmDbStorage.deleteLike(filmId, userId);
    }


    /**
     * Метод возвращает набор id 10 самых популярных
     * фильмов.
     * @return
     */
    public List<Film> getPopularFilms(int count) {

        return filmDbStorage.getPopularFilms(count);

    }


    /**
     * Метод возвращает объект фильма по его id из хранилища.
     * @param id
     * @return
     */
    public Film getFilm(long id) {

        Film film = filmDbStorage.getFilm(id);
        if (film == null)
            throw new NotFoundException("Фильм отсутствует в списке!");

        return film;

    }
}
