package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.GenreController;
import ru.yandex.practicum.filmorate.controller.RatingController;
import ru.yandex.practicum.filmorate.controller.UserController;

import java.util.Map;

/**
 * Класс-обработчик ErrorHandler предназначен для
 * обработки ошибок на стороне сервера и отправки правильного
 * кода ответа на клиентскую сторону с детальным описанием
 * причин возникшей проблемы.
 */
@RestControllerAdvice(assignableTypes = {FilmController.class, UserController.class, GenreController.class, RatingController.class})
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBadRequest(final BadRequestException e) {
        return Map.of("Ошибка в запросе", e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(final NotFoundException e) {
        return Map.of("Объект не найден", e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidation(final ValidationException e) {
        return Map.of("Объект не прошел валидацию", e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String>  handleSpringValidation(MethodArgumentNotValidException e) {
        return Map.of("Объект-аргумент не отвечает заявленным требованиям", e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleAnyError(final Throwable e) {
        return Map.of("Ошибка на сервере", e.getMessage());
    }
}
