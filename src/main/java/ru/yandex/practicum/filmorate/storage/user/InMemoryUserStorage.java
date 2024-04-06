package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

/**
 * Класс InMemoryUserStorage является реализацией интерфейса
 * UserStorage и хранит в операционной памяти информацию об объектах
 * типа User.
 */
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private int id = 0;

    /**
     * Метод генерирует и возвращает идентификатор пользователя.
     * @return
     */
    private long getId() {
        return ++id;
    }


    @Override
    public User addUser(User user) {
        if (user.getId() <= 0)
            user.setId(getId());
        users.put(user.getId(), user);
        return user;
    }


    @Override
    public User updateUser(User user) {
        return null;
    }


    @Override
    public void deleteUser(long id) {
        users.remove(id);
    }


    @Override
    public User getUser(long id) {
        return users.get(id);
    }


    @Override
    public boolean containsUser(long id) {
        return users.containsKey(id);
    }


    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }


    @Override
    public User addFriend(Long followingFriendId, Long followedFriendId) {
        return null;
    }


    @Override
    public void deleteFriend(Long followingFriendId, Long followedFriendId) {

    }


    @Override
    public List<User> getFriends(Long userId) {
        return null;
    }


    @Override
    public List<User> getCommonFriends(long user1Id, long user2Id) {
        return null;
    }
}
