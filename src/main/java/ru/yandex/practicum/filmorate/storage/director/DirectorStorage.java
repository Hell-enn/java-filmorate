package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.Director;
import java.util.List;

public interface DirectorStorage {

    Director addDirector(Director director);

    void deleteDirector(long id);

    Director getDirector(long id);

    Director updateDirector(Director director);

    List<Director> getDirectors();

    boolean containsDirector(long id);
}
