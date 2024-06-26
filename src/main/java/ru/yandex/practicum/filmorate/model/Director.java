package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class Director {
    private long id;
    @NotBlank(message = "Имя не может быть пустым или содержать только пробелы")
    private String name;
}
