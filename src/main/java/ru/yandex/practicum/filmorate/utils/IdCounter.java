package ru.yandex.practicum.filmorate.utils;

/**
 * IdCounter - утилитарный класс. Необходим
 * для генерации id объектов типа User и Film
 * путём инкрементирования переменной в
 * случае, если при инициализации им не было
 * присвоено это поле.
 */

public class IdCounter {

    private static int id = 0;

    public static int getId() {
        return ++id;
    }

}
