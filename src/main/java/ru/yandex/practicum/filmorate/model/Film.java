package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Класс Film - бизнес-сущность. Необходм для
 * последующего создания объектов типа Film с
 * обозначенным рядом свойств.
 */
@Data
@NonNull
public class Film {

    private int id;
    @NonNull
    @NotBlank
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final DateTimeFormatter releaseDateFormatter;
    private final int duration;

    public Film(int id, @NonNull String name, String description, String releaseDate, int duration) {

        this.id = id;
        this.name = name;
        this.description = description;

        releaseDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.releaseDate = LocalDate.parse(releaseDate, releaseDateFormatter);

        this.duration = duration;

    }

}
