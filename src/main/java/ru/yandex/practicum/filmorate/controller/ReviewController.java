package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.review.ReviewService;

import javax.validation.Valid;
import java.util.List;

/**
 * Класс ReviewController предоставляет ряд эндпоинтов для запросов
 * с клиентской части приложения к разделу приложения с отзывами к фильмам.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;


    /**
     * Эндпоинт. Метод добавляяет новый отзыв к фильму в список с
     * помощью соответствующего метода интерфеса хранилища -
     * ReviewStorage. В случае успеха возвращает добавленный объект.
     * @param review
     * @return
     */
    @PostMapping("/reviews")
    public Review postReview(@Valid @RequestBody Review review) {

        Review addedReview = reviewService.postReview(review);

        if (addedReview == null) {
            log.debug("Отзыв к фильму с id {} уже содержится в списке!", review.getFilmId());
            return null;
        }

        log.debug("Отзыв к фильму с id {} добавлен!", review.getFilmId());
        return addedReview;
    }


    /**
     * Эндпоинт. Метод обновляет отзыв к фильму в списке с
     * помощью соответствующего метода интерфеса хранилища -
     * ReviewStorage. В случае успеха возвращает обновлённый объект.
     * @param review
     * @return
     */
    @PutMapping("/reviews")
    public Review putReview(@Valid @RequestBody Review review) {

        Review addedReview = reviewService.putReview(review);

        log.debug("Отзыв к фильму с id {} обновлён!", review.getFilmId());
        return addedReview;
    }


    /**
     * Эндпоинт. Метод удаляет отзыв с id.
     * @param id
     * @return
     */
    @DeleteMapping("/reviews/{id}")
    public void deleteReview(@PathVariable long id) {

        reviewService.deleteReview(id);

    }


    /**
     * Эндпоинт. Метод возвращает список отзывов к фильму.
     * @return
     */
    @GetMapping("/reviews")
    public List<Review> getReviews(@RequestParam(defaultValue = "-1") long filmId,
                                   @RequestParam(defaultValue = "10") int count) {

        List<Review> reviews = reviewService.getReviews(filmId, count);
        if (reviews != null)
            log.debug("Возвращаем список из отзывов к фильму с id {}!", filmId);
        return reviews;

    }


    /**
     * Эндпоинт. Метод возвращает объект отзыва по его id.
     * @return
     */
    @GetMapping("/reviews/{id}")
    public Review getReview(@PathVariable long id) {

        Review review = reviewService.getReview(id);
        if (review != null)
            log.debug("Возвращаем отзыв к фильму с id {}!", id);
        return review;

    }


    /**
     * Эндпоинт. Добавляет лайк отзыву фильму пользователя с userId.
     */
    @PutMapping("/reviews/{id}/like/{userId}")
    public void addLikeForReview(@PathVariable long id,
                                 @PathVariable long userId) {

        log.debug("Пользователь с id = " + userId + " ставит лайк отзыву с id = " + id + "!");
        reviewService.addRateForReview(id, userId, true);

    }


    /**
     * Эндпоинт. Добавляет дизлайк отзыву фильму пользователя с userId.
     */
    @PutMapping("/reviews/{id}/dislike/{userId}")
    public void addDislikeForReview(@PathVariable long id,
                                    @PathVariable long userId) {

        log.debug("Пользователь с id = " + userId + " ставит дизлайк отзыву с id = " + id + "!");
        reviewService.addRateForReview(id, userId, false);

    }


    /**
     * Эндпоинт. Удаляет лайк с отзыва к фильму от пользователя с userId.
     */
    @DeleteMapping("/reviews/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id,
                           @PathVariable long userId) {

        log.debug("Пользователь с id = " + userId + " удаляет лайк с отзыва с id = " + id + "!");
        reviewService.deleteLikeFromReview(id, userId);

    }


    /**
     * Эндпоинт. Удаляет дизлайк с отзыва к фильму от пользователя с userId.
     */
    @DeleteMapping("/reviews/{id}/dislike/{userId}")
    public void deleteDislike(@PathVariable long id,
                           @PathVariable long userId) {

        log.debug("Пользователь с id = " + userId + " удаляет дизлайк с отзыва с id = " + id + "!");
        reviewService.deleteDislikeFromReview(id, userId);

    }
}
