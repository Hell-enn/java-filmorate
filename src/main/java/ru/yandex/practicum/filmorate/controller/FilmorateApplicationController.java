package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Класс FilmorateApplicationController предоставляет эндпоинт для
 * пустого запроса с клиентской части приложения (к приветственной странице).
 */

@Slf4j
@RestController()
public class FilmorateApplicationController {

    /**
     * Эндпоинт. Метод возвращает строку с названием странице на
     * приветственной странице.
     * @return
     */
    @RequestMapping
    public String welcomePage() {
        return "FilmoRate";
    }

}
