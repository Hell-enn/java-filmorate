package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Класс GenreFilm - бизнес-сущность. Необходим для
 * последующего создания объектов-связок, каждая из
 * которых описывает пару - фильм и его жанр.
 * В каждой такой связке содержатся свойства:
 * - идентификатор фильма (filmId)
 * - идентификатор жанра (genreId)
 */
@Data
@AllArgsConstructor
public class GenreFilm {
    private final long filmId;
    private final long genreId;
}
