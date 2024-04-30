package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

/**
 * Интерфейс ReviewStorage является контрактом для конкретных
 * реализаций классов-хранилищ, содержащих в себе объекты
 * отзывов к фильмам от пользователей приложения "java-filmorate".
 */
public interface ReviewStorage {

    /**
     * Добавляет объект review в хранилище.
     * @param review
     */
    Review addReview(Review review);


    /**
     * Обновляет объект review в хранилище.
     * @param review
     */
    Review updateReview(Review review);


    /**
     * Удаляет объект типа Review с id из хранилища.
     * @param id
     */
    void deleteReview(long id);


    /**
     * Метод возвращает из хранилища объект отзыва.
     * @param id
     * @return
     */
    Review getReview(long id);


    /**
     * Метод отвечает на вопрос, содержится ли отзыв с
     * данным id в хранилище.
     * @return
     */
    boolean containsReview(long id);


    /**
     * Метод возвращает список отзывов по идентификатору фильма.
     * Если фильм (filmId) не указан, то все.
     * Если количество (count) не указано, то 10.
     * * @param filmId
     * * @param count
     * @return
     */
    List<Review> getReviews(Long filmId, int count);


    /**
     * Метод добавляет id пользователя в перечень оценок отзыва.
     * @param reviewId
     * @param userId
     * @param isUseful
     * @return
     */
    Long addRateForReview(Long reviewId, Long userId, boolean isUseful);


    /**
     * Метод удаляет id пользователя из перечня оценок отзыва.
     * @param reviewId
     * @param userId
     * @return
     */
    Long deleteRateFromReview(Long reviewId, Long userId, boolean isUseful);
}
