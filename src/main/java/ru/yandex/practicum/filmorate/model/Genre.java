package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Класс Genre - бизнес-сущность. Необходим для
 * последующего создания объектов для каждого жанра фильма
 * со свойствами:
 * - идентификатор жанра (id)
 * - название жанра (name)
 */
@Data
@AllArgsConstructor
public class Genre {
    private long id;
    private final String name;

}
