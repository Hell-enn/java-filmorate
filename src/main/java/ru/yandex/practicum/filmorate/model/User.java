package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Класс User - бизнес-сущность. Необходим для
 * последующего создания объектов для каждого пользователя
 * со свойствами:
 * - идентификатор пользователя (id)
 * - электронная почта пользователя (email)
 * - логин пользователя (login)
 * - имя пользователя (name)
 * - день рождения пользователя (birthday)
 */
@Data
@AllArgsConstructor
public class User {

    private long id;
    @NotNull(message = "Поле email отсутствует!")
    @Email(message = "Неверный формат электронной почты!")
    private final String email;
    @NotBlank(message = "Поле login отсутствует или задано с пробелами!")
    private final String login;
    @NotNull(message = "Поле name отсутствует!")
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Поле birthday отсутствует!")
    private final LocalDate birthday;

}
