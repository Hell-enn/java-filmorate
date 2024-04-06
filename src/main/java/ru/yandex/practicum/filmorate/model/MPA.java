package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Класс MPA - бизнес-сущность. Необходим для
 * последующего создания объектов для каждого возрастного рейтинга.
 * Класс содержит свойства:
 * - идентификатор возрастного рейтинга (id)
 * - название возрастного рейтинга (name)
 * - описание возрастного рейтинга (description)
 */
@Data
@AllArgsConstructor
public class MPA {
    private long id;
    private final String name;
    private String description;
}
