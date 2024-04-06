package ru.yandex.practicum.filmorate.storage.rating;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

/**
 * Интерфейс RatingStorage является контрактом для конкретных
 * реализаций классов-хранилищ, содержащих в себе объекты
 * рейтингов фильмов приложения "java-filmorate".
 */
public interface RatingStorage {

    /**
     * Добавляет объект rating в список.
     * @param rating
     */
    void addRating(MPA rating);

    /**
     * Удаляет объект типа Rating с id из списка.
     * @param id
     */
    void deleteRating(long id);

    /**
     * Метод возвращает объект типа Rating с id из хранилища.
     * @param id
     * @return
     */
    MPA getRating(long id);

    /**
     * Метод отвечает на вопрос, содержится ли рейтинг с
     * данным id в списке.
     * @return
     */
    boolean containsRating(long id);

    /**
     * Метод возвращает список рейтингов.
     * @return
     */
    List<MPA> getRatings();

    /**
     * Метод возвращает рейтинг фильма с id = filmId.
     * @param filmId
     */
    MPA getFilmRating(Long filmId);
}
