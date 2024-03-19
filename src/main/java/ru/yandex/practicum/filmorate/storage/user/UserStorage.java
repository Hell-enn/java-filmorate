package ru.yandex.practicum.filmorate.storage.user;

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
    void addUser(User user);


    /**
     * Удаляет объект типа User с id из списка.
     * @param id
     */
    void deleteUser(long id);


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

}
