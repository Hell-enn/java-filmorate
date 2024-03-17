package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void addUser(User user) {
        if (user.getId() <= 0)
            user.setId(getId());
        users.put(user.getId(), user);
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
}
