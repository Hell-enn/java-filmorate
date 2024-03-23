package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

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
    public void addFilm(Film film) {
        if (film.getId() <= 0)
            film.setId(getId());
        films.put(film.getId(), film);
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
}
