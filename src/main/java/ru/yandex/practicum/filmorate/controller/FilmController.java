package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
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
     *
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
     *
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
     *
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
     *
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
     * Эндпоинт. Удаляет Фильм с filmId
     */
    @DeleteMapping("/films/{id}")
    public void deleteFilm(@PathVariable(name = "id") long filmId) {
        log.info("Удаление фильма по id: {}", filmId);
        filmService.deleteFilm(filmId);
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
    public List<Film> getPopularFilms(@RequestParam(value = "count", required = false, defaultValue = "10") long limit,
                                      @RequestParam(value = "genreId", required = false) Long genreId,
                                      @RequestParam(value = "year", required = false) Integer year) {
        return filmService.getPopularFilms(limit, genreId != null ? genreId : 0, year != null ? year : 0);
    }

    /**
     * Метод возвращает спикок общих фильмов для пользователей с id user1Id и user2Id.
     * @param userId
     * @param friendId
     */
    @GetMapping("/films/common")
    public List<Film> getCommonFilms(@RequestParam long userId,
                                     @RequestParam long friendId) {

        return filmService.getCommonFilms(userId, friendId);

    }

    @GetMapping("/films/director/{directorId}")
    public List<Film> getDirectorFilms(@PathVariable long directorId,
                                       @RequestParam(required = false, defaultValue = "likes") String sortBy) {
        return filmService.getDirectorFilms(directorId, sortBy);
    }

    /**
     * Метод возвращает спикок фильмов содержащих подстроку с сортировкой по популярности.
     * @param query текст для поиска
     * @param by параметры поиска - по названию или режиссеру, или вместе
     */
    @GetMapping("/films/search")
    public List<Film> getFilmsBySubstring(@RequestParam String query,
                                          @RequestParam List<String> by) {

        if (query.isBlank() | by.isEmpty()) {
            log.debug("Не корректные параметры запроса");
            throw new BadRequestException("Не корректные параметры запроса");
        }

        return filmService.getFilmsBySubstring(query, by);

    }


}
