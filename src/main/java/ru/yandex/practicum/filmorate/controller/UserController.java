package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс UserController предоставляет ряд эндпоинтов для запросов
 * с клиентской части приложения к разделу с пользователями.
 */

@Slf4j
@RestController()
public class UserController {

    List<User> users = new ArrayList<>();
    List<Integer> ids = new ArrayList<>();


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

        if (ids.contains(user.getId())) {
            return null;
        }

        users.add(user);
        ids.add(user.getId());

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

        if (!ids.contains(user.getId())) {
            throw new ValidationException("Пользователь отсутствует в списке!");
        }

        for (User userFromList: users) {
            if (userFromList.getId() == user.getId()) {
                users.remove(userFromList);
                break;
            }
        }

        users.add(user);
        ids.add(user.getId());
        log.debug("Пользователь с логином {} обновлён!", user.getLogin());

        return user;
    }


    /**
     * Эндпоинт. Метод возвращает список пользователей.
     * @return
     */
    @GetMapping("/users")
    public List<User> getUsers() {

        return new ArrayList<>(users);

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
        else if (user.getEmail().isBlank())
            message = "Адрес электонной почты пользователя отсутствует!";
        else if (!user.getEmail().contains("@"))
            message = "В указанном адресе электронной почты пользователя отсутствует символ @!";
        else if (user.getLogin().isBlank())
            message = "Логин пользователя отсутствует!";
        else if (user.getLogin().contains(" "))
            message = "Логин содержит пробелы!";
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
