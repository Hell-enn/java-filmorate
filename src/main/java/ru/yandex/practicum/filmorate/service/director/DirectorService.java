package ru.yandex.practicum.filmorate.service.director;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorDbStorage;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorService {
    private final Logger log = LoggerFactory.getLogger(DirectorService.class);
    private final DirectorStorage directorStorage;
    private final DirectorDbStorage directorDbStorage;

    public Director addDirector(Director director) {
        validateDirector(director);
        Director addedDirector = directorDbStorage.addDirector(director);
        if (addedDirector != null) {
            log.debug("Режиссёр {} добавлен!", director.getName());
        } else {
            log.debug("Режиссёр {} уже содержится в списке!", director.getName());
        }
        return addedDirector;
    }

    public Director updateDirector(Director director) {
        log.debug("Режиссер обновлен: {}", director.getName());
        return directorDbStorage.updateDirector(director);
    }

    public Director getDirector(long directorId) {
        return directorDbStorage.getDirector(directorId);
    }

    public void deleteDirector(long id) {
        directorDbStorage.deleteDirector(id);
    }

    public boolean containsDirector(long id) {
        return !directorDbStorage.containsDirector(id);
    }

    public List<Director> getDirectors() {
        return directorStorage.getDirectors();
    }

    public void validateDirector(Director director) {
        if (director.getName() == null || director.getName().trim().isEmpty()) {
            log.error("Попытка добавления режиссера без указания имени.");
            throw new IllegalArgumentException("Имя не может быть пустым.");
        }
    }
}
