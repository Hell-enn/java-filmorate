package ru.yandex.practicum.filmorate.exception;

/**
 * Класс-исключение, объекты которого выбрасываются в случае, если
 * была осуществлена попытка добавить объект-дубль.
 */
public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}