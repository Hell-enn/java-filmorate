package ru.yandex.practicum.filmorate.utils;

/**
 * IdCounter - утилитарный класс. Необходим
 * для генерации id объектов типа User и Film
 * путём инкрементирования переменной в
 * случае, если при инициализации им не было
 * присвоено это поле.
 */

public class IdCounter {

    private int id = 0;

    public int getId() {
        return ++id;
    }

}
