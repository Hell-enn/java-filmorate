package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import java.util.List;

/**
 * Интерфейс UserStorage является контрактом для конкретных
 * реализаций классов-хранилищ, содержащих в себе объекты
 * профилей пользователей приложения "java-filmorate".
 */
public interface UserStorage {

    /**
     * Добавляет объект user в список.
     * @param user
     */
    User addUser(User user);


    /**
     * Обновляет объект user в списке.
     * @param user
     */
    User updateUser(User user);


    /**
     * Удаляет объект типа User с id из списка.
     * @param id
     */
    void deleteUser(long id);


    /**
     * Метод возвращает из хранилища объект пользователя по его id.
     * @param id
     * @return
     */
    User getUser(long id);


    /**
     * Метод отвечает на вопрос, содержится ли пользователь с
     * данным id в списке.
     * @return
     */
    boolean containsUser(long id);


    /**
     * Метод возвращает список пользователей.
     * @return
     */
    List<User> getUsers();


    /**
     * Метод добавляет в список друзей пользователя с id = followingFriendId
     * пользователя с id = followedFriendId.
     * @param followingFriendId
     * @param followedFriendId
     */
    User addFriend(Long followingFriendId, Long followedFriendId);


    /**
     * Метод удаляет из списка друзей пользователя с id = followingFriendId
     * пользователя с id = followedFriendId.
     * @param followingFriendId
     * @param followedFriendId
     */
    void deleteFriend(Long followingFriendId, Long followedFriendId);


    /**
     * Метод возвращает спикок id друзей пользователя с id = userId.
     * @param userId
     */
    List<User> getFriends(Long userId);


    /**
     * Метод возвращает спикок общих друзей для пользователей с id user1Id и user2Id.
     * @param user1Id
     * @param user2Id
     */
    List<User> getCommonFriends(long user1Id, long user2Id);


    /**
     * Метод возвращает список фильмов, рекомендованных
     * к просмотру пользователю с id, на основании оценок других
     * пользователей со схожими интересами.
     * @param userId
     */
    List<Film> getRecommendations(long userId);

}
