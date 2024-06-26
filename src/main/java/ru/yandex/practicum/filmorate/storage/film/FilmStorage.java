package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;

import java.util.List;
import java.util.Set;

/**
 * Интерфейс FilmStorage является контрактом для конкретных
 * реализаций классов-хранилищ, содержащих в себе объекты
 * фильмов приложения "java-filmorate".
 */
public interface FilmStorage {

    /**
     * Добавляет объект film в список.
     *
     * @param film
     */
    Film addFilm(Film film);


    /**
     * Обновляет объект film в списке.
     *
     * @param film
     */
    Film updateFilm(Film film);


    /**
     * Удаляет объект типа Film с id из списка.
     *
     * @param id
     */
    void deleteFilm(long id);


    /**
     * Метод возвращает из хранилища объект фильма.
     *
     * @param id
     * @return
     */
    Film getFilm(long id);


    /**
     * Метод отвечает на вопрос, содержится ли фильм с
     * данным id в списке.
     *
     * @return
     */
    boolean containsFilm(long id);


    /**
     * Метод возвращает список фильмов.
     *
     * @return
     */
    List<Film> getFilms();


    /**
     * Метод возвращает список из count фильмов, отсортированных по популярности.
     *
     * @return
     */
    List<Film> getPopularFilms(long limit, long genreId, int year);


    /**
     * Метод добавляет id пользователя в перечень лайков, если он оценил данный фильм.
     *
     * @param userId
     * @return
     */
    Long addLike(Long filmId, Long userId);


    /**
     * Метод удаляет id пользователя из перечня лайков.
     *
     * @param userId
     * @return
     */
    Long deleteLike(Long filmId, Long userId);


    /**
     * Метод возвращает список список лайков фильма с filmId.
     *
     * @param filmId
     * @return
     */
    Set<Like> getLikes(int filmId);


    /**
     * Метод возвращает список объектов жанров фильма с filmId.
     *
     * @param filmId
     * @return
     */
    Set<Genre> getFilmGenres(int filmId);


    /**
     * Метод возвращает спикок общих фильмов для пользователей с id user1Id и user2Id.
     *
     * @param id
     * @param otherId
     */
    public List<Film> getCommonFilms(long id, long otherId);

    List<Film> getDirectorFilms(long directorId, String sortBy);


    /**
     * Cлужебный метод используется для получения объекта
     * фильма из строки, полученной из базы данных (таблица 'film').
     *
     * @param filmRows
     * @return
     */
    public Film getFilmFromSqlRow(SqlRowSet filmRows);

    List<Film> getFilmsBySubstring(String query, List<String> by);
}
