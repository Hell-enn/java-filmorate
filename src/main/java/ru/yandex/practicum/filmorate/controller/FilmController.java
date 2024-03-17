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
@RestController()
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
        return film;
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

        return filmService.getFilms();

    }


    /**
     * Эндпоинт. Метод возвращает объект фильма по его id.
     * @return
     */
    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable int id) {

        return filmService.getFilm(id);

    }


    /**
     * Эндпоинт. Добавляет лайк пользователя с userId
     * фильму с id.
     */
    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable int id,
                        @PathVariable int userId) {

        filmService.addLike(userId, id);

    }


    /**
     * Эндпоинт. Удаляет лайк пользователя с userId
     * фильму с id.
     */
    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id,
                        @PathVariable int userId) {

        filmService.deleteLike(userId, id);

    }


    /**
     * Эндпоинт. Возвращает список самых популярных фильмов в
     * количестве amount. Если в строке запроса данный параметр
     * не был передан, возвращает список из 10 самых популярных
     * фильмов.
     */
    @GetMapping("/films/popular?count={count}")
    public List<Film> getPopularFilms(@RequestParam(required = false) int count) {

        int amount = count < 1 ? 10 : count;
        return filmService.getPopularFilms(amount);

    }
}
