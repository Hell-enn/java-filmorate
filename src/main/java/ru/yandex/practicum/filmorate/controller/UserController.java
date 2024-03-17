package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс UserController предоставляет ряд эндпоинтов для запросов
 * с клиентской части приложения к разделу с пользователями.
 */

@Slf4j
@RestController()
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;


    /**
     * Эндпоинт. Метод добавляяет нового пользователя в список в случае,
     * если он в нём отсутствует. Иначе выбрасывает исключение
     * ValidateException с сообщением об ошибке.
     * В случае успеха возвращает добавленный объект.
     * @param user
     * @return
     */
    @PostMapping("/users")
    public User postUser(@Valid @RequestBody User user) {

        validateUser(user);

        if (user.getId() == 0) {
            user.setId(++id);
        }

        if (users.containsKey(user.getId())) {
            return null;
        }

        users.put(user.getId(), user);

        log.debug("Пользователь с логином {} добавлен!", user.getLogin());

        return user;
    }


    /**
     * Эндпоинт. Метод обновляет объект пользователя в списке в случае,
     * если он в нём присутствует. Иначе выбрасывает исключение
     * ValidateException с сообщением об ошибке.
     * В случае успеха возвращает обновлённый объект.
     * @param user
     * @return
     */
    @PutMapping("/users")
    public User putUser(@Valid @RequestBody User user) {

        validateUser(user);

        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь отсутствует в списке!");
        }

        users.put(user.getId(), user);

        log.debug("Пользователь с логином {} обновлён!", user.getLogin());

        return user;
    }


    /**
     * Эндпоинт. Метод возвращает список пользователей.
     * @return
     */
    @GetMapping("/users")
    public List<User> getUsers() {

        return new ArrayList<>(users.values());

    }


    /**
     * Закрытый служебный метод проверяет объект типа User
     * на соответствие ряду условий. Используется впоследствие
     * для валидации объекта типа User при попытке его добавления
     * или обновления в списке.
     * В случае неудачи выбрасывает исключение ValidationException
     * с сообщением об ошибке.
     * @param user
     */
    private void validateUser(User user) {

        String message = "";

        if (user == null)
            message = "Вы не передали информацию о пользователе!";
        else if (user.getBirthday().isAfter(LocalDate.now()))
            message = "Неверно указана дата рождения!";
        else if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());


        if (!message.isBlank()) {
            try {
                throw new ValidationException(message);
            } finally {
                log.debug(message);
            }
        }
    }
}
