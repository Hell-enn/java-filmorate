package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Класс User - бизнес-сущность. Необходм для
 * последующего создания объектов типа User с
 * обозначенным рядом свойств.
 */

@Data
@NonNull
public class User {

    private int id;
    @NonNull
    @NotBlank
    @Email
    private final String email;
    @NonNull
    @NotBlank
    private final String login;
    private String name;
    private final LocalDate birthday;
    private final DateTimeFormatter birthdateFormatter;


    public User(int id, @NonNull String email, @NonNull String login, String name, String birthday) {

        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;

        birthdateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.birthday = LocalDate.parse(birthday, birthdateFormatter);

    }

}
