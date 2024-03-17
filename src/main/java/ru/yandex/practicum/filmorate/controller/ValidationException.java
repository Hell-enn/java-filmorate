package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;

/**
 * Класс ValidationException необходим для создания
 * соответствующих объектов исключения в приложении
 * и их последующего выбрасывания в определённых ситуациях.
 */

@Getter
public class ValidationException extends RuntimeException {

    String message;

    public ValidationException(String message) {
        this.message = message;
    }

}
