package ru.yandex.practicum.filmorate.service.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

/**
 * Класс GenreService предоставляет функциональность по
 * взаимодействию со списком жанров
 * (добавление, удаление, вывод списка жанров и т.д.).
 */
@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreDbStorage;


    /**
     * Метод добавляяет новый жанр в список в случае,
     * если он в нём отсутствует. Иначе выбрасывает исключение
     * ValidateException с сообщением об ошибке.
     * В случае успеха возвращает добавленный объект.
     * @param genre
     * @return
     */
    public Genre postGenre(Genre genre) {

        if (genreDbStorage.containsGenre(genre.getId())) {
            return null;
        }

        genreDbStorage.addGenre(genre);
        return genre;
    }


    /**
     * Метод обновляет объект жанра в списке в случае,
     * если он в нём присутствует. Иначе выбрасывает исключение
     * ValidateException с сообщением об ошибке.
     * В случае успеха возвращает обновлённый объект.
     * @param genre
     * @return
     */
    public Genre putGenre(Genre genre) {

        if (!genreDbStorage.containsGenre(genre.getId())) {
            throw new NotFoundException("Жанр отсутствует в списке!");
        }

        genreDbStorage.addGenre(genre);
        return genre;
    }


    /**
     * Метод возвращает список жанров из хранилища.
     * @return
     */
    public List<Genre> getGenres() {

        return genreDbStorage.getGenres();

    }


    /**
     * Метод возвращает объект жанра по его id из хранилища.
     * @param id
     * @return
     */
    public Genre getGenre(long id) {

        Genre genre = genreDbStorage.getGenre(id);
        if (genre == null)
            throw new NotFoundException("Жанр " + id + " отсутствует в списке!");

        return genre;

    }


    /**
     * Метод удаляет объект жанра по его id из хранилища.
     * @param id
     * @return
     */
    public Genre deleteGenre(long id) {

        Genre genre = genreDbStorage.getGenre(id);
        if (genre == null)
            throw new NotFoundException("Жанр " + id + " отсутствует в списке!");

        genreDbStorage.deleteGenre(id);
        return genre;

    }


    /**
     * Метод возвращает список объектов жанров фильма с id из хранилища.
     * @param filmId
     * @return
     */
    public List<Genre> getFilmGenres(long filmId) {

        return genreDbStorage.getFilmGenres(filmId);

    }
}
