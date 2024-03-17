package ru.yandex.practicum.filmorate.exception;

/**
 * Класс FilmNotFoundException необходим для создания
 * соответствующих объектов исключения в приложении
 * и их последующего выбрасывания в ситуациях, когда
 * объект фильма не был найден.
 */
public class FilmNotFoundException  extends RuntimeException {

    public FilmNotFoundException(String message) {
        super(message);
    }

}