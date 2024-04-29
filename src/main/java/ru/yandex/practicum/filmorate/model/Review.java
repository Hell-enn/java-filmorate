package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Класс Review - бизнес-сущность. Необходим для
 * последующего создания объектов для каждого отзыва к фильмам
 * со свойствами:
 * - идентификатор отзыва (reviewId)
 * - содержание отзыва (content)
 * - тип отзыва (isPositive)
 * - идентификатор пользователя, оставившего отзыв (userId)
 * - идентификатор фильма, которому пользователь оставил отзыв (filmId)
 * - рейтинг отзыва (useful).
 */
@Data
@AllArgsConstructor
public class Review {

    private long reviewId;
    @Size(min = 1)
    private final String content;
    private Boolean isPositive;
    @NotNull(message = "Поле userId отсутствует!")
    private long userId;
    @NotNull(message = "Поле filmId отсутствует!")
    private long filmId;
    private int useful;

}
