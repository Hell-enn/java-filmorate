package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.director.DirectorService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * Класс DirectorController предоставляет ряд эндпоинтов для запросов
 * с клиентской части приложения к разделу с режиссерами.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class DirectorController {

    private final DirectorService directorService;

    @PostMapping("/directors")
    public Director addDirector(@Valid @RequestBody Director director) {
        Director addedDirector = directorService.addDirector(director);
        if (addedDirector == null) {
            log.debug("Режиссёр {} уже содержится в списке!", director.getName());
            return null;
        }
        log.debug("Режиссёр {} добавлен!", director.getName());
        return addedDirector;
    }

    @PutMapping("/directors")
    public Director updateDirector(@Valid @RequestBody Director director, HttpServletResponse response) {
        try {
            Director updatedDirector = directorService.updateDirector(director);
            if (updatedDirector != null) {
                log.debug("Режиссёр \"{}\" обновлён!", director.getName());
                response.setStatus(HttpServletResponse.SC_OK); // 200 OK
                return updatedDirector;
            } else {
                log.debug("Режиссер с ID {} не найден.", director.getId());
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
                return null;
            }
        } catch (Exception e) {
            log.error("Ошибка при обновлении режиссёра: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 500 Internal Server Error
            return null;
        }
    }

    @GetMapping("/directors/{id}")
    public Director getDirector(@PathVariable long id, HttpServletResponse response) {
        Director director = directorService.getDirector(id);
        if (director != null) {
            log.debug("Возвращаем информацию о режиссере с ID {}.", id);
            return director;
        } else {
            log.debug("Режиссер с ID {} не найден.", id);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    @GetMapping("/directors")
    public List<Director> getDirectors() {
        log.debug("Запрос списка всех режиссеров.");
        List<Director> directors = directorService.getDirectors();
        if (directors != null && !directors.isEmpty()) {
            log.debug("Возвращаем список из {} режиссёров!", directors.size());
        } else {
            log.debug("Список режиссёров пуст.");
        }
        return directors;
    }

    @DeleteMapping("/directors/{id}")
    public void deleteDirector(@PathVariable long id) {
        log.debug("Удаляем режиссёра с ID {}!", id);
        directorService.deleteDirector(id);
    }
}

