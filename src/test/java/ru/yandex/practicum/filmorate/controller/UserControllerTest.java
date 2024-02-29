package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserControllerTest {

    private static UserController userController;

    @BeforeAll
    static void createContext() {
        userController = new UserController();
    }

    @Test
    void postOrPutFilm() {

        User user = new User(1, "zayatcsusami@zayatc.com", "zayatcsusami", "Заяц Усатый", "2020-12-12");

        userController.postUser(user);
        assertEquals(1, userController.getUsers().size());
        assertEquals(user, userController.getUsers().get(0));

        userController.postUser(user);
        assertEquals(1, userController.getUsers().size());

        User updatedUser = new User(1, "zayatcsusami2@zayatc.com", "zayatcsusami2", "Заяц Усатый", "2020-12-12");
        userController.postUser(updatedUser);
        assertEquals(1, userController.getUsers().size());
        Assertions.assertNotEquals(updatedUser, userController.getUsers().get(0));

        userController.putUser(updatedUser);
        assertEquals(1, userController.getUsers().size());
        assertEquals(updatedUser, userController.getUsers().get(0));

        User user2 = new User(2, "lisiymedved@zayatc.com", "lisiymedved", "Лысый Медведь", "2018-05-20");
        userController.postUser(user2);
        assertEquals(2, userController.getUsers().size());
        assertEquals(user2, userController.getUsers().get(1));

        User updatedUser2 = new User(2, "lisiymedved2@zayatc.com", "lisiymedved2", "Лысый Медведь", "2018-05-20");
        userController.postUser(updatedUser2);
        assertEquals(2, userController.getUsers().size());
        Assertions.assertNotEquals(updatedUser2, userController.getUsers().get(1));

        userController.putUser(updatedUser2);
        assertEquals(2, userController.getUsers().size());
        assertEquals(updatedUser2, userController.getUsers().get(1));

    }


    @Test
    void validateFilm() {

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.postUser(null));

        assertEquals("Вы не передали информацию о пользователе!", exception.getMessage());


        User user2 = new User(1, "", "zayatcsusami", "Заяц Усатый", "2020-12-12");
        exception = assertThrows(
                ValidationException.class,
                () -> userController.postUser(user2));

        assertEquals("Адрес электонной почты пользователя отсутствует!", exception.getMessage());


        NullPointerException nullPointerException = assertThrows(
                NullPointerException.class,
                () -> userController.postUser(new User(1, null, "zayatcsusami", "Заяц Усатый", "2020-12-12"))
        );

        assertEquals("email is marked non-null but is null", nullPointerException.getMessage());


        User user3 = new User(1, "zayatcsusamizayatc.com", "zayatcsusami", "Заяц Усатый", "2020-12-12");

        exception = assertThrows(
                ValidationException.class,
                () -> userController.postUser(user3));

        assertEquals("В указанном адресе электронной почты пользователя отсутствует символ @!", exception.getMessage());


        User user4 = new User(1, "zayatcsusami@zayatc.com", "", "Заяц Усатый", "2020-12-12");
        exception = assertThrows(
                ValidationException.class,
                () -> userController.postUser(user4));

        assertEquals("Логин пользователя отсутствует!", exception.getMessage());


        User user5 = new User(1, "zayatcsusami@zayatc.com", "z ayatc susa mi", "Заяц Усатый", "2020-12-12");
        exception = assertThrows(
                ValidationException.class,
                () -> userController.postUser(user5));

        assertEquals("Логин содержит пробелы!", exception.getMessage());


        User user6 = new User(1, "zayatcsusami@zayatc.com", "zayatcsusami", "Заяц Усатый", "2025-12-12");
        exception = assertThrows(
                ValidationException.class,
                () -> userController.postUser(user6));

        assertEquals("Неверно указана дата рождения!", exception.getMessage());

    }

}