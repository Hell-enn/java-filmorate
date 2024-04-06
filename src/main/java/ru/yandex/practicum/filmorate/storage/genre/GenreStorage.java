package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

/**
 * Интерфейс GenreStorage является контрактом для конкретных
 * реализаций классов-хранилищ, содержащих в себе объекты
 * жанров фильмов приложения "java-filmorate".
 */
public interface GenreStorage {

    /**
     * Добавляет объект genre в список.
     * @param genre
     */
    void addGenre(Genre genre);


    /**
     * Удаляет объект типа Genre с id из списка.
     * @param id
     */
    void deleteGenre(long id);


    /**
     * Возвращает объект типа Genre с id из списка.
     * @param id
     */
    Genre getGenre(long id);

    /**
     * Метод отвечает на вопрос, содержится ли жанр с
     * данным id в списке.
     * @return
     */
    boolean containsGenre(long id);

    /**
     * Метод возвращает список жанров.
     * @return
     */
    List<Genre> getGenres();

    /**
     * Метод возвращает список жанров фильма с filmId.
     * @param filmId
     * @return
     */
    List<Genre> getFilmGenres(long filmId);

}
