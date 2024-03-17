package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс UserService предоставляет функциональность по
 * взаимодействию со списком друзей у объектов типа User
 * (добавление, удаление, вывод списка общих друзей).
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;


    /**
     * Метод добавляяет нового пользователя в список в случае,
     * если он в нём отсутствует. Иначе выбрасывает исключение
     * ValidateException с сообщением об ошибке.
     * В случае успеха возвращает добавленный объект.
     * @param user
     * @return
     */
    public User postUser(@Valid User user) {

        validateUser(user);

        if (user.getId() == 0) {
            user.setId(userStorage.getId());
        }

        if (userStorage.containsUser(user.getId())) {
            return null;
        }

        userStorage.addUser(user);
        return user;
    }


    /**
     * Метод обновляет объект пользователя в списке в случае,
     * если он в нём присутствует. Иначе выбрасывает исключение
     * ValidateException с сообщением об ошибке.
     * В случае успеха возвращает обновлённый объект.
     * @param user
     * @return
     */
    public User putUser(@Valid User user) {

        validateUser(user);

        if (!userStorage.containsUser(user.getId())) {
            throw new UserNotFoundException("Пользователь отсутствует в списке!");
        }

        userStorage.addUser(user);
        return user;
    }


    /**
     * Метод возвращает список пользователей из хранилища.
     * @return
     */
    public List<User> getUsers() {

        return userStorage.getUsers();

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
        else if (user.getName().isBlank())
            message = "Имя пользователя отсутствует!";

        if (!message.isBlank()) {
            throw new ValidationException(message);
        }
    }


    /**
     * Метод добавляет в список друзей объекта userFrom
     * id объекта userTo и наоборот.
     * @param userFromId
     * @param userToId
     */
    public User addFriend(int userFromId, int userToId) {

        User userFrom = userStorage.getUser(userFromId);
        User userTo = userStorage.getUser(userToId);

        if (userFrom == null)
            throw new UserNotFoundException("Пользователь " + userFromId + " отсутствует в списке!");

        if (userTo == null)
            throw new UserNotFoundException("Пользователь " + userToId + " отсутствует в списке!");

        userFrom.addFriend((long) userToId);
        userTo.addFriend((long) userFromId);

        return userTo;

    }


    /**
     * Метод удаляет из списка друзей объекта userFrom
     * id объекта userTo и наоборот.
     * @param userFromId
     * @param userToId
     */
    public User deleteFriend(int userFromId, int userToId) {

        User userFrom = userStorage.getUser(userFromId);
        User userTo = userStorage.getUser(userToId);

        if (userFrom == null)
            throw new UserNotFoundException("Пользователь " + userFromId + " отсутствует в списке!");

        if (userTo == null)
            throw new UserNotFoundException("Пользователь " + userToId + " отсутствует в списке!");

        userFrom.deleteFriend((long) userToId);
        userTo.deleteFriend((long) userFromId);

        return userTo;
    }


    /**
     * Метод возвращает список друзей пользователя с id.
     * @param userId
     * @return
     */
    public List<User> getFriends(int userId) {

        User user = userStorage.getUser(userId);
        if (user == null)
            throw new UserNotFoundException("Пользователь " + userId + " отсутствует в списке!");

        List<User> friends = new ArrayList<>();
        for (Long id: user.getFriends()) {
            friends.add(userStorage.getUser(Math.toIntExact(id)));
        }

        return friends;

    }


    /**
     * Метод возвращает набор id общих друзей для двух
     * объектов типа User, переданных в качестве аргументов.
     * @param user1Id
     * @param user2Id
     * @return
     */
    public List<User> getCommonFriends(int user1Id, int user2Id) {

        List<Long> commonFriendsIds;
        List<User> commonFriends = new ArrayList<>();

        User user1 = userStorage.getUser(user1Id);
        User user2 = userStorage.getUser(user2Id);

        if (user1 == null)
            throw new UserNotFoundException("Пользователь " + user1Id + " отсутствует в списке!");

        if (user2 == null)
            throw new UserNotFoundException("Пользователь " + user2Id + " отсутствует в списке!");

        commonFriendsIds = new ArrayList<>(user1.getFriends());
        commonFriendsIds.retainAll(user2.getFriends());

        for(Long id: commonFriendsIds) {
            commonFriends.add(userStorage.getUser(Math.toIntExact(id)));
        }

        return commonFriends;
    }


    /**
     * Метод возвращает объект пользователя по его id из хранилища.
     * @param id
     * @return
     */
    public User getUser(int id) {

        User user = userStorage.getUser(id);
        if (user == null)
            throw new UserNotFoundException("Пользователь " + id + " отсутствует в списке!");

        return user;

    }
}
