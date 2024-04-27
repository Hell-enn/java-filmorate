package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.rating.RatingService;

import javax.validation.Valid;
import java.util.List;

/**
 * Класс RatingController предоставляет ряд эндпоинтов для запросов
 * с клиентской части приложения к разделу с рейтингами.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;


    /**
     * Эндпоинт. Метод добавляяет новый рейтинг в список  с
     * помощью соответствующего метода интерфеса хранилища -
     * RatingStorage. В случае успеха возвращает добавленный объект.
     * @param rating
     * @return
     */
    @PostMapping("/mpa")
    public MPA postRating(@Valid @RequestBody MPA rating) {

        MPA addedRating = ratingService.postRating(rating);

        if (addedRating == null) {
            log.debug("Рейтинг " + rating.getName() + " уже содержится в списке!");
            return null;
        }

        log.debug("Рейтинг {} добавлен!", rating.getName());
        return rating;
    }


    /**
     * Эндпоинт. Метод обновляет объект рейтинга в списке в случае,
     * если он в нём присутствует. В случае успеха возвращает
     * обновлённый объект.
     * @param rating
     * @return
     */
    @PutMapping("/mpa")
    public MPA putRating(@Valid @RequestBody MPA rating) {

        MPA addedRating = ratingService.putRating(rating);

        log.debug("Рейтинг \"{}\" обновлён!", rating.getName());
        return addedRating;
    }


    /**
     * Эндпоинт. Метод возвращает список рейтингов.
     * @return
     */
    @GetMapping("/mpa")
    public List<MPA> getRating() {

        List<MPA> ratings = ratingService.getRatings();
        if (ratings != null)
            log.debug("Возвращаем список из {} жанров!", ratings.size());
        return ratings;

    }


    /**
     * Эндпоинт. Метод возвращает объект жанра по его id.
     * @return
     */
    @GetMapping("/mpa/{id}")
    public MPA getRating(@PathVariable long id) {

        MPA rating = ratingService.getRating(id);
        if (rating != null)
            log.debug("Возвращаем рейтинг с id = {}!", id);
        return rating;

    }


    /**
     * Эндпоинт. Метод удаляет из списка рейтинг и возвращает его.
     * @return
     */
    @DeleteMapping("/mpa/{id}")
    public MPA deleteRating(@PathVariable long id) {

        log.debug("Удаляем рейтинг с id = " + id + "!");
        return ratingService.deleteRating(id);

    }
}
