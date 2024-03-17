package ru.yandex.practicum.filmorate.exception;

/**
 * Класс FilmNotFoundException необходим для создания
 * соответствующих объектов исключения в приложении
 * и их последующего выбрасывания в ситуациях, когда
 * объект пользователя не был найден.
 */
public class UserNotFoundException  extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

}
