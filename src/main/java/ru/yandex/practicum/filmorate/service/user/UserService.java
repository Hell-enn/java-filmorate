package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.event.EventStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

/**
 * Класс UserService предоставляет функциональность по
 * взаимодействию со списком пользователей и их друзей -
 * объекты типа User
 * (добавление, удаление, вывод списка пользователей и их общих друзей).
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userDbStorage;
    private final EventStorage eventDbStorage;


    /**
     * Метод добавляет нового пользователя в список в случае,
     * если он в нём отсутствует. Иначе выбрасывает исключение
     * ValidateException с сообщением об ошибке.
     * В случае успеха возвращает добавленный объект.
     *
     * @param user
     * @return
     */
    public User postUser(User user) {

        validateUser(user);

        userDbStorage.addUser(user);
        return user;
    }


    /**
     * Метод обновляет объект пользователя в списке в случае,
     * если он в нём присутствует. Иначе выбрасывает исключение
     * ValidateException с сообщением об ошибке.
     * В случае успеха возвращает обновлённый объект.
     *
     * @param user
     * @return
     */
    public User putUser(User user) {
        return userDbStorage.updateUser(user);
    }


    /**
     * Метод возвращает список пользователей из хранилища.
     *
     * @return
     */
    public List<User> getUsers() {

        return userDbStorage.getUsers();

    }


    /**
     * Метод удаляет пользователя с userId из хранилеща
     */
    public void deletUser(long userId) {
        userDbStorage.deleteUser(userId);
    }


    /**
     * Закрытый служебный метод проверяет объект типа User
     * на соответствие ряду условий. Используется впоследствие
     * для валидации объекта типа User при попытке его добавления
     * или обновления в списке.
     * В случае неудачи выбрасывает исключение ValidationException
     * с сообщением об ошибке.
     *
     * @param user
     */
    private void validateUser(User user) {

        String message = "";

        if (user == null)
            message = "Вы не передали информацию о пользователе!";
        else if (user.getName() == null)
            message = "Имя пользователя отсутствует!";
        else if (!user.getEmail().contains("@"))
            message = "Неправильный формат электронной почты!";
        else if (user.getBirthday().isAfter(LocalDate.now()))
            message = "Неверно указана дата рождения!";

        if (!message.isBlank()) {
            throw new ValidationException(message);
        }

    }


    /**
     * Метод добавляет в список друзей объекта userFrom
     * id объекта userTo и наоборот.
     *
     * @param userFromId
     * @param userToId
     */
    public User addFriend(long userFromId, long userToId) {

        if (!userDbStorage.containsUser(userFromId))
            throw new NotFoundException("Пользователь " + userFromId + " отсутствует в списке!");

        if (!userDbStorage.containsUser(userToId))
            throw new NotFoundException("Пользователь " + userToId + " отсутствует в списке!");

        User user = userDbStorage.addFriend(userFromId, userToId);
        eventDbStorage.addEvent(new Event(System.currentTimeMillis(), userFromId, "FRIEND", "ADD", userToId));
        return user;
    }


    /**
     * Метод удаляет из списка друзей объекта userFrom
     * id объекта userTo и наоборот.
     *
     * @param followingUserId
     * @param followedUserId
     */
    public void deleteFriend(long followingUserId, long followedUserId) {
        userDbStorage.deleteFriend(followingUserId, followedUserId);
        eventDbStorage.addEvent(new Event(System.currentTimeMillis(), followingUserId, "FRIEND", "REMOVE", followedUserId));
    }


    /**
     * Метод возвращает список друзей пользователя с id.
     *
     * @param userId
     * @return
     */
    public List<User> getFriends(long userId) {

        if (!userDbStorage.containsUser(userId))
            throw new NotFoundException("Пользователь " + userId + " отсутствует в списке!");

        return userDbStorage.getFriends(userId);

    }


    /**
     * Метод возвращает набор id общих друзей для двух
     * объектов типа User, переданных в качестве аргументов.
     *
     * @param user1Id
     * @param user2Id
     * @return
     */
    public List<User> getCommonFriends(long user1Id, long user2Id) {
        return userDbStorage.getCommonFriends(user1Id, user2Id);
    }


    /**
     * Метод возвращает объект пользователя по его id из хранилища.
     *
     * @param id
     * @return
     */
    public User getUser(long id) {

        User user = userDbStorage.getUser(id);
        if (user == null)
            throw new NotFoundException("Пользователь " + id + " отсутствует в списке!");

        return user;

    }


    /**
     * Метод возвращает ленту событий пользователя по его id из хранилища.
     *
     * @param id
     * @return
     */
    public List<Event> getUserFeed(long id) {
        getUser(id);
        return eventDbStorage.getUserFeed(id);
    }


    /**
     * Эндпоинт. Метод возвращает список фильмов, рекомендованных
     * к просмотру пользователю с id, на основании оценок других
     * пользователей со схожими интересами.
     *
     * @return
     */
    public List<Film> getRecommendations(long id) {

        return userDbStorage.getRecommendations(id);

    }
}
