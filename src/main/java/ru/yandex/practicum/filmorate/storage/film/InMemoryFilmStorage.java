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
public class InMemoryFilmStorage implements FilmStorage{
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 0;

    public int getId() {
        return ++id;
    }


    public void addFilm(Film film) {
        films.put(film.getId(), film);
    }


    public void deleteFilm(int id) {
        films.remove(id);
    }


    public Film getFilm(int id) {
        return films.get(id);
    }


    public boolean containsFilm(int id) {
        return films.containsKey(id);
    }


    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }
}
