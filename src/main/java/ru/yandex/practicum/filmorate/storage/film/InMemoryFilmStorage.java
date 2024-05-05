package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;

import java.util.*;

/**
 * Класс InMemoryFilmStorage является реализацией интерфейса
 * FilmStorage и хранит в операционной памяти информацию об объектах
 * типа Film.
 */
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private int id = 0;

    /**
     * Метод генерирует и возвращает идентификатор фильма.
     * @return
     */
    private long getId() {
        return ++id;
    }


    @Override
    public Film addFilm(Film film) {
        if (film.getId() <= 0)
            film.setId(getId());
        films.put(film.getId(), film);
        return null;
    }


    @Override
    public Film updateFilm(Film film) {
        return null;
    }


    @Override
    public void deleteFilm(long id) {
        films.remove(id);
    }


    @Override
    public Film getFilm(long id) {
        return films.get(id);
    }


    @Override
    public boolean containsFilm(long id) {
        return films.containsKey(id);
    }


    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }


    @Override
    public List<Film> getPopularFilms(long limit, long genreId, int year) {
        return new ArrayList<>(films.values());
    }


    @Override
    public Long addLike(Long filmId, Long userId) {
        return null;
    }


    @Override
    public Long deleteLike(Long filmId, Long userId) {
        return null;
    }


    @Override
    public Set<Like> getLikes(int filmId) {
        return null;
    }


    @Override
    public Set<Genre> getFilmGenres(int filmId) {
        return null;
    }

    @Override
    public List<Film> getCommonFilms(long id, long otherId)  {
        return null;
    }

    @Override
    public List<Film> getFilmsBySubstring(String query, List<String> by) {
        return null;
    }

    @Override
    public List<Film> getDirectorFilms(long directorId, String sortBy) {
        return new ArrayList<>();
    }
}
