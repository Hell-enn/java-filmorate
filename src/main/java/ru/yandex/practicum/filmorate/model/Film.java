package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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
    @NotBlank
    private final String name;
    @Size(min = 1, max = 200)
    private final String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate releaseDate;
    @Min(1)
    private final int duration;

}
