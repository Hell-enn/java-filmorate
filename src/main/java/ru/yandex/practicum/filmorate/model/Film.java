package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс Film - бизнес-сущность. Необходм для
 * последующего создания объектов типа Film с
 * обозначенным рядом свойств.
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
    /**
     * Попыталась по аналогии с User убрать инициализацию и
     * перезаписывать в переменную likes объект типа HashSet
     * при добавлении фильма, но Postman почему-то не пропускает
     * такую логику
     */
    private final Set<Long> likes = new HashSet<>();

    /**
     * Метод добавляет id пользователя в набор, если он оценил данный фильм.
     * @param userId
     * @return
     */
    public Long addLike(Long userId) {

        if (!likes.contains(userId)) {
            likes.add(userId);
            return userId;
        }

        return null;
    }

    /**
     * Метод удаляет id пользователя из набора.
     * @param userId
     * @return
     */
    public Long deleteLike(Long userId) {

        if (!likes.contains(userId)) {
            likes.remove(userId);
            return userId;
        }

        return null;

    }

}
