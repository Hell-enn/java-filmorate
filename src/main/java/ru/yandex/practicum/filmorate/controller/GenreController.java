package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.genre.GenreService;

import javax.validation.Valid;
import java.util.List;

/**
 * Класс GenreController предоставляет ряд эндпоинтов для запросов
 * с клиентской части приложения к разделу с жанрами.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;


    /**
     * Эндпоинт. Метод добавляяет новый жанр в список  с
     * помощью соответствующего метода интерфеса хранилища -
     * GenreStorage. В случае успеха возвращает добавленный объект.
     * @param genre
     * @return
     */
    @PostMapping("/genres")
    public Genre postGenre(@Valid @RequestBody Genre genre) {

        Genre addedGenre = genreService.postGenre(genre);

        if (addedGenre == null) {
            log.debug("Жанр " + genre.getName() + " уже содержится в списке!");
            return null;
        }

        log.debug("Жанр {} добавлен!", genre.getName());
        return genre;
    }


    /**
     * Эндпоинт. Метод обновляет объект жанра в списке в случае,
     * если он в нём присутствует. В случае успеха возвращает
     * обновлённый объект.
     * @param genre
     * @return
     */
    @PutMapping("/genres")
    public Genre putGenre(@Valid @RequestBody Genre genre) {

        Genre addedGenre = genreService.putGenre(genre);

        log.debug("Жанр \"{}\" обновлён!", genre.getName());
        return addedGenre;
    }


    /**
     * Эндпоинт. Метод возвращает список жанров.
     * @return
     */
    @GetMapping("/genres")
    public List<Genre> getGenre() {

        List<Genre> genres = genreService.getGenres();
        if (genres != null)
            log.debug("Возвращаем список из {} жанров!", genres.size());
        return genres;

    }


    /**
     * Эндпоинт. Метод возвращает объект жанра по его id.
     * @return
     */
    @GetMapping("/genres/{id}")
    public Genre getGenre(@PathVariable long id) {

        Genre genre = genreService.getGenre(id);
        if (genre != null)
            log.debug("Возвращаем жанр с id = {}!", id);
        return genre;

    }


    /**
     * Эндпоинт. Метод возвращает список жанров фильма с filmId.
     * @return
     */
    @GetMapping("/films/{filmId}/genres")
    public List<Genre> getFilmGenres(@PathVariable long filmId) {

        log.debug("Возвращаем список жанров фильма с id = " + filmId + "!");
        return genreService.getFilmGenres(filmId);

    }


    /**
     * Эндпоинт. Метод удаляет из списка жанр и возвращает его.
     * @return
     */
    @DeleteMapping("/genres/{id}")
    public Genre deleteGenre(@PathVariable long id) {

        log.debug("Удаляем жанр с id = " + id + "!");
        return genreService.deleteGenre(id);

    }
}
