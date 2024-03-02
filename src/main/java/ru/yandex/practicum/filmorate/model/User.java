package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
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
    @NonNull
    @Email
    private final String email;
    @NonNull
    private final String login;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate birthday;

}
