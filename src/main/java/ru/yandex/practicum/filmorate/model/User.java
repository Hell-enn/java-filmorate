package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.utils.IdCounter;

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

    private final int id;
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
    private final static IdCounter idCounter = new IdCounter();


    public User(int id, @NonNull String email, @NonNull String login, String name, String birthday) {

        if (id == 0)
            this.id = idCounter.getId();
        else
            this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;

        birthdateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.birthday = LocalDate.parse(birthday, birthdateFormatter);

    }

}
