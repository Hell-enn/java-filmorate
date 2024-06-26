package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * Класс UserController предоставляет ряд эндпоинтов для запросов
 * с клиентской части приложения к разделу с пользователями.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    /**
     * Эндпоинт. Метод добавляет нового пользователя в список с
     * помощью соответствующего метода интерфеса хранилища -
     * UserStorage. В случае успеха возвращает добавленный объект.
     *
     * @param user
     * @return
     */
    @PostMapping("/users")
    public User postUser(@Valid @RequestBody User user) {

        User addedUser = userService.postUser(user);

        if (addedUser == null) {
            log.debug("Пользователь \"" + user.getName() + "\" уже содержится в списке!");
            return null;
        }

        log.debug("Пользователь с логином {} добавлен!", user.getLogin());
        return user;
    }


    /**
     * Эндпоинт. Метод обновляет объект пользователя в списке в случае,
     * если он в нём присутствует. Иначе выбрасывает исключение
     * ValidateException с сообщением об ошибке.
     * В случае успеха возвращает обновлённый объект.
     *
     * @param user
     * @return
     */
    @PutMapping("/users")
    public User putUser(@Valid @RequestBody User user) {

        User addedUser = userService.putUser(user);

        log.debug("Фильм \"{}\" обновлён!", user.getName());
        return addedUser;
    }


    /**
     * Эндпоинт. Метод возвращает список пользователей.
     *
     * @return
     */
    @GetMapping("/users")
    public List<User> getUsers() {

        List<User> users = userService.getUsers();
        if (users != null)
            log.debug("Возвращаем список из {} пользователей!", users.size());
        return users;

    }


    /**
     * Эндпоинт. Удаляет пользователя с userId
     */
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable(name = "id") long userId) {
        log.info("Удаление фильма по id: {}", userId);
        userService.deletUser(userId);
    }


    /**
     * Эндпоинт. Метод возвращает объект пользователя по его id.
     *
     * @return
     */
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable long id) {

        User user = userService.getUser(id);
        if (user != null)
            log.debug("Возвращаем пользователя с id = {}!", id);
        return user;

    }


    /**
     * Эндпоинт. Метод добавляет в друзья пользователя с friendId
     * пользователю с id и наоборот.
     *
     * @return
     */
    @PutMapping("/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable long id,
                          @PathVariable long friendId) {

        return userService.addFriend(id, friendId);

    }


    /**
     * Эндпоинт. Метод удаляет из друзей пользователя с friendId
     * у пользователя с id и наоборот.
     *
     * @return
     */
    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id,
                             @PathVariable long friendId) {

        log.debug("Пользователь с id = " + id + " удаляет из друзей пользователя с id = " + friendId + "!");
        userService.deleteFriend(id, friendId);

    }


    /**
     * Эндпоинт. Метод возвращает список друзей пользователя с id.
     *
     * @return
     */
    @GetMapping("/users/{id}/friends")
    public List<User> getFriends(@PathVariable long id) {

        log.debug("Возвращаем список друзей пользователя с id = " + id + "!");
        return userService.getFriends(id);

    }


    /**
     * Эндпоинт. Метод возвращает список общих друзей двух
     * пользователей - с id и otherId.
     *
     * @return
     */
    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable long id,
                                       @PathVariable long otherId) {

        return userService.getCommonFriends(id, otherId);

    }


    /**
     * Эндпоинт. Метод возвращает ленту событий пользователя id .
     *
     * @return
     */
    @GetMapping("/users/{id}/feed")
    public List<Event> getUserFeed(@PathVariable long id) {
        return userService.getUserFeed(id);
    }


    /**
     * Эндпоинт. Метод возвращает список фильмов, рекомендованных
     * к просмотру пользователю с id, на основании оценок других
     * пользователей со схожими интересами.
     *
     * @return
     */
    @GetMapping("/users/{id}/recommendations")
    public List<Film> getRecommendations(@PathVariable long id) {

        return userService.getRecommendations(id);

    }
}
