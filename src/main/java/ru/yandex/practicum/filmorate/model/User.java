package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * Класс User - бизнес-сущность. Необходм для
 * последующего создания объектов типа User с
 * обозначенным рядом свойств.
 */

@Data
@AllArgsConstructor
public class User {

    private int id;
    @NotBlank
    @Email
    private final String email;
    @NotBlank
    private final String login;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate birthday;

}
