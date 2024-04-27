package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.Valid;
import java.util.List;

/**
 * Класс FilmController предоставляет ряд эндпоинтов для запросов
 * с клиентской части приложения к разделу приложения с фильмами.
 */

@Slf4j
@RestController
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;


    /**
     * Эндпоинт. Метод добавляяет новый фильм в список с
     * помощью соответствующего метода интерфеса хранилища -
     * FilmStorage. В случае успеха возвращает добавленный объект.
     * @param film
     * @return
     */
    @PostMapping("/films")
    public Film postFilm(@Valid @RequestBody Film film) {

        Film addedFilm = filmService.postFilm(film);

        if (addedFilm == null) {
            log.debug("Фильм \"" + film.getName() + "\" уже содержится в списке!");
            return null;
        }

        log.debug("Фильм \"{}\" добавлен!", film.getName());
        return addedFilm;
    }


    /**
     * Эндпоинт. Метод обновляет фильм в списке с
     * помощью соответствующего метода интерфеса хранилища -
     * FilmStorage. В случае успеха возвращает обновлённый объект.
     * @param film
     * @return
     */
    @PutMapping("/films")
    public Film putFilm(@Valid @RequestBody Film film) {

        Film addedFilm = filmService.putFilm(film);

        log.debug("Фильм \"{}\" обновлён!", film.getName());
        return addedFilm;
    }


    /**
     * Эндпоинт. Метод возвращает список фильмов.
     * @return
     */
    @GetMapping("/films")
    public List<Film> getFilms() {

        List<Film> films = filmService.getFilms();
        if (films != null)
            log.debug("Возвращаем список из {} фильмов!", films.size());
        return films;

    }


    /**
     * Эндпоинт. Метод возвращает объект фильма по его id.
     * @return
     */
    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable long id) {

        Film film = filmService.getFilm(id);
        if (film != null)
            log.debug("Возвращаем фильм \"{}\"!", film.getName());
        return filmService.getFilm(id);

    }


    /**
     * Эндпоинт. Добавляет лайк пользователя с userId
     * фильму с id.
     */
    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable long id,
                        @PathVariable long userId) {

        log.debug("Пользователь с id = " + userId + " ставит лайк фильму с id = " + id + "!");
        filmService.addLike(userId, id);

    }


    /**
     * Эндпоинт. Удаляет лайк пользователя с userId
     * фильму с id.
     */
    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id,
                        @PathVariable long userId) {

        log.debug("Пользователь с id = " + userId + " удаляет лайк с фильма с id = " + id + "!");
        filmService.deleteLike(userId, id);

    }


    /**
     * Эндпоинт. Возвращает список самых популярных фильмов в
     * количестве amount. Если в строке запроса данный параметр
     * не был передан, возвращает список из 10 самых популярных
     * фильмов.
     */
    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {

        int filmsAmount = filmService.getFilms().size();
        if (count > filmsAmount) {
            log.debug("Возвращаем список " + filmsAmount + " самых популярных фильмов!");
            return filmService.getPopularFilms(filmsAmount);
        } else {
            log.debug("Возвращаем список " + count + " самых популярных фильмов!");
            return filmService.getPopularFilms(count);
        }
    }
}
