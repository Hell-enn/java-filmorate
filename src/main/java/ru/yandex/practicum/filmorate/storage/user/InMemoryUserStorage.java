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
public class InMemoryUserStorage implements UserStorage{
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    public int getId() {
        return ++id;
    }


    public void addUser(User user) {
        users.put(user.getId(), user);
    }


    public void deleteUser(int id) {
        users.remove(id);
    }


    public User getUser(int id) {
        return users.get(id);
    }


    public boolean containsUser(int id) {
        return users.containsKey(id);
    }


    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }
}
