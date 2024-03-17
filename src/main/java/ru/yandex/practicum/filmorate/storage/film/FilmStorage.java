package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

/**
 * Интерфейс FilmStorage является контрактом для конкретных
 * реализаций классов-хранилищ, содержащих в себе объекты
 * фильмов приложения "java-filmorate".
 */
public interface FilmStorage {

    /**
     * Метод генерирует и возвращает идентификатор фильма.
     * @return
     */
    int getId();

    /**
     * Добавляет объект film в список.
     * @param film
     */
    void addFilm(Film film);


    /**
     * Удаляет объект типа Film с id из списка.
     * @param id
     */
    void deleteFilm(int id);


    Film getFilm(int id);

    /**
     * Метод отвечает на вопрос, содержится ли фильм с
     * данным id в списке.
     * @return
     */
    boolean containsFilm(int id);

    /**
     * Метод возвращает список фильмов.
     * @return
     */
    List<Film> getFilms();

}
