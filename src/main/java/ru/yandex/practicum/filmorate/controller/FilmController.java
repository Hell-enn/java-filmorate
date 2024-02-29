package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс FilmController предоставляет ряд эндпоинтов для запросов
 * с клиентской части приложения к разделу приложения с фильмами.
 */

@Slf4j
@RestController()
public class FilmController {

    List<Film> films = new ArrayList<>();
    List<Integer> ids = new ArrayList<>();


    /**
     * Эндпоинт. Метод добавляяет новый фильм в список в случае,
     * если он в нём отсутствует. Иначе выбрасывает исключение
     * ValidateException с сообщением об ошибке.
     * В случае успеха возвращает добавленный объект.
     * @param film
     * @return
     */
    @PostMapping("/films")
    public Film postFilm(@Valid @RequestBody Film film) {

        validateFilm(film);

        if (ids.contains(film.getId())) {
            throw new ValidationException("Фильм уже есть в списке!");
        }

        films.add(film);
        ids.add(film.getId());

        log.debug("Фильм \"{}\" добавлен!", film.getName());

        return film;
    }


    /**
     * Эндпоинт. Метод обновляет фильм в списке в случае,
     * если он в нём присутствует. Иначе выбрасывает исключение
     * ValidateException с сообщением об ошибке.
     * В случае успеха возвращает обновлённый объект.
     * @param film
     * @return
     */
    @PutMapping("/films")
    public Film putFilm(@Valid @RequestBody Film film) {

        validateFilm(film);

        if (!ids.contains(film.getId())) {
            throw new ValidationException("Фильм отсутствует в списке!");
        }

        for (Film filmFromList: films) {
            if (filmFromList.getId() == film.getId()) {
                films.remove(filmFromList);
                break;
            }
        }

        films.add(film);
        ids.add(film.getId());
        log.debug("Фильм \"{}\" обновлён!", film.getName());

        return film;
    }


    /**
     * Эндпоинт. Метод возвращает список фильмов.
     * @return
     */
    @GetMapping("/films")
    public List<Film> getFilms() {

        return new ArrayList<>(films);

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
        else if (film.getName().isBlank())
            message = "Имя фильма отсутствует!";
        else if (film.getDescription().length() > 200)
            message = "Длина описания превышает 200 символов!";
        else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))
            message = "Фильм не мог быть выпущен до 28 декабря 1895!";
        else if (film.getDuration() <= 0)
            message = "Задана отрицательная продолжительность фильма!";

        if (!message.isBlank()) {
            try {
                throw new ValidationException(message);
            } finally {
                log.debug(message);
            }
        }
    }
}
