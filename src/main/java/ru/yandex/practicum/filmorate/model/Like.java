package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Класс Like - бизнес-сущность. Необходим для
 * последующего создания объектов-связок, каждая из
 * которых описывает пару - фильм и пользователь, который его оценил.
 * В каждой такой связке содержатся свойства:
 * - идентификатор фильма (filmId)
 * - идентификатор пользователя, поставившего лайк (userId)
 */
@Data
@AllArgsConstructor
public class Like {
    private final long filmId;
    private final long userId;
}
