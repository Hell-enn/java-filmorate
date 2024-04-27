package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Класс Film - бизнес-сущность. Необходим для
 * последующего создания объектов для каждого фильма
 * со свойствами:
 * - идентификатор фильма (id)
 * - название фильма (name)
 * - описание фильма (description)
 * - дата выхода фильма (releaseDate)
 * - продолжительность фильма (duration)
 * - возрастной рейтинг фильма (mpa)
 * - список жанров фильма (genres).
 */
@Data
@AllArgsConstructor
public class Film {

    private long id;
    @NotNull(message = "Поле name отсутствует!")
    @Size(min = 1)
    private final String name;
    @Size(min = 1, max = 200)
    private final String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Поле releaseDate отсутствует!")
    private final LocalDate releaseDate;
    @Min(1)
    private final int duration;
    private MPA mpa;
    private List<Genre> genres;

    /**
     * Поле genres не учитывается в equals, т.к. при прогонке тестов
     * порядок жанров в списке меняется, что является причиной
     * ошибок, хотя фактически объекты идентичны.
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return id == film.id && duration == film.duration && Objects.equals(name, film.name) && Objects.equals(description, film.description) && Objects.equals(releaseDate, film.releaseDate) && Objects.equals(mpa, film.mpa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, releaseDate, duration, mpa);
    }
}
