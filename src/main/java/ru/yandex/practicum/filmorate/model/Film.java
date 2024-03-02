package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import java.time.LocalDate;

/**
 * Класс Film - бизнес-сущность. Необходм для
 * последующего создания объектов типа Film с
 * обозначенным рядом свойств.
 */
@Data
@AllArgsConstructor
public class Film {

    private int id;
    @NonNull
    private final String name;
    private final String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate releaseDate;
    private final int duration;

}
