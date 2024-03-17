package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @NotNull(message = "Поле email отсутствует!")
    @Email(message = "Неверный формат электронной почты!")
    private final String email;
    @NotBlank(message = "Поле login отсутствует или задано с пробелами!")
    private final String login;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Поле birthday отсутствует!")
    private final LocalDate birthday;

}
