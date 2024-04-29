package ru.yandex.practicum.filmorate.service.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;

import java.util.*;

/**
 * Класс ReviewService предоставляет функциональность по
 * взаимодействию со списком отзывов к фильмам и их лайков у
 * объектов типа Review (добавление, удаление, вывод отзывов и
 * набора 10 наиболее популярных отзывов).
 */
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewStorage reviewDbStorage;

    /**
     * Метод добавляет отзыв в список в случае, если он там отсутствует.
     * В противном случае возвращает null.
     * @param review
     * @return
     */
    public Review postReview(Review review) {

        validateReview(review);

        if (reviewDbStorage.containsReview(review.getReviewId())) {
            return null;
        }

        return reviewDbStorage.addReview(review);
    }


    /**
     * Метод обновляет отзыв в списке в случае, если он там присутствует.
     * В противном случае выбрасывает исключение типа ValidationException.
     * @param review
     * @return
     */
    public Review putReview(Review review) {

        return reviewDbStorage.updateReview(review);

    }


    /**
     * Метод удаляет отзыв из хранилища.
     * @param id
     * @return
     */
    public void deleteReview(long id) {

        reviewDbStorage.deleteReview(id);

    }


    /**
     * Метод возвращает отзыв из списка в случае, если он там присутствует.
     * В противном случае возвращает null.
     * @param reviewId
     * @return
     */
    public Review getReview(long reviewId) {

        return reviewDbStorage.getReview(reviewId);

    }


    /**
     * Метод возвращает список всех отзывов из хранилища.
     * @return
     */
    public List<Review> getReviews(long reviewId, int count) {

        return reviewDbStorage.getReviews(reviewId, count);

    }


    /**
     * Закрытый служебный метод проверяет объект типа Review
     * на соответствие ряду условий. Используется впоследствие
     * для валидации объекта типа Review при попытке его добавления
     * или обновления в списке.
     * В случае неудачи выбрасывает исключение ValidationException
     * с сообщением об ошибке.
     * @param review
     */
    private void validateReview(Review review) {

        String message = "";

        if (review == null)
            message = "Вы не передали информацию об отзыве!";

        if (!message.isBlank()) {
            throw new ValidationException(message);
        }
    }


    /**
     * Метод добавляет в список оценок объекта review
     * id объекта user.
     * @param reviewId
     * @param userId
     * @param isUseful
     */
    public void addRateForReview(long reviewId, long userId, boolean isUseful) {

        Review review = reviewDbStorage.getReview(reviewId);

        if (review == null)
            throw new NotFoundException("Отзыв отсутствует в списке!");

        reviewDbStorage.addRateForReview(reviewId, userId, isUseful);
    }


    /**
     * Метод удаляет из списка оценок отзыва с reviewId
     * лайк от пользователя с userId.
     * @param reviewId
     * @param userId
     */
    public void deleteLikeFromReview(long reviewId, long userId) {

        Review review = reviewDbStorage.getReview(reviewId);

        if (review == null)
            throw new NotFoundException("Отзыв отсутствует в списке!");

        reviewDbStorage.deleteRateFromReview(reviewId, userId, true);
    }


    /**
     * Метод удаляет из списка оценок отзыва с reviewId
     * дизлайк от пользователя с userId.
     * @param reviewId
     * @param userId
     */
    public void deleteDislikeFromReview(long reviewId, long userId) {

        Review review = reviewDbStorage.getReview(reviewId);

        if (review == null)
            throw new NotFoundException("Отзыв отсутствует в списке!");

        reviewDbStorage.deleteRateFromReview(reviewId, userId, false);
    }
}
