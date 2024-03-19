package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.*;

/**
 * Класс FilmService предоставляет функциональность по
 * взаимодействию со списком лайков у объектов типа Film
 * (добавление, удаление, вывод набора 10 наиболее популярных
 * фильмов).
 */
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;


    /**
     * Метод добавляет фильм в список в случае, если он там отсутствует.
     * В противном случае возвращает null.
     * @param film
     * @return
     */
    public Film postFilm(Film film) {

        validateFilm(film);

        if (filmStorage.containsFilm(film.getId())) {
            return null;
        }

        //film.setLikes(new HashSet<>());
        filmStorage.addFilm(film);
        return film;
    }


    /**
     * Метод обновляет фильм в списке в случае, если он там присутствует.
     * В противном случае выбрасывает исключение типа ValidationException.
     * @param film
     * @return
     */
    public Film putFilm(Film film) {

        validateFilm(film);

        if (!filmStorage.containsFilm(film.getId())) {
            throw new FilmNotFoundException("Фильм отсутствует в списке!");
        }

        filmStorage.addFilm(film);
        return film;
    }


    /**
     * Метод возвращает список фильмов из хранилища.
     * @return
     */
    public List<Film> getFilms() {

        return filmStorage.getFilms();

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

        Film film = filmStorage.getFilm(filmId);

        if (film == null)
            throw new FilmNotFoundException("Фильм отсутствует в списке!");

        film.addLike(userId);
    }


    /**
     * Метод удаляет из списка лайков объекта film
     * id объекта user.
     * @param userId
     * @param filmId
     */
    public void deleteLike(long userId, long filmId) {

        Film film = filmStorage.getFilm(filmId);

        if (film == null)
            throw new FilmNotFoundException("Фильм отсутствует в списке!");

        if (film.getLikes().contains(userId))
            film.deleteLike(userId);
    }


    /**
     * Метод возвращает набор id 10 самых популярных
     * фильмов.
     * @return
     */
    public List<Film> getPopularFilms(long count) {

        List<Film> sortedFilms = filmStorage.getFilms();
        sortedFilms.sort(Comparator.comparingInt(film -> -film.getLikes().size()));
        return sortedFilms.subList(0, (int) count);

    }


    /**
     * Метод возвращает объект фильма по его id из хранилища.
     * @param id
     * @return
     */
    public Film getFilm(long id) {

        Film film = filmStorage.getFilm(id);
        if (film == null)
            throw new FilmNotFoundException("Фильм отсутствует в списке!");

        return filmStorage.getFilm(id);

    }
}
