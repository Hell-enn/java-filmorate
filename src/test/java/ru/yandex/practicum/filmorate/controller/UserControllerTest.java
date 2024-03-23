package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserControllerTest {

    private static UserController userController;

    @BeforeAll
    static void createContext() {
        userController = new UserController();
    }

    @Test
    void postOrPutFilm() {

        User user = new User(1, "zayatcsusami@zayatc.com", "zayatcsusami", "Заяц Усатый", LocalDate.of(2020, 12, 12));

        userController.postUser(user);
        assertEquals(1, userController.getUsers().size());
        assertEquals(user, userController.getUsers().get(0));

        userController.postUser(user);
        assertEquals(1, userController.getUsers().size());

        User updatedUser = new User(1, "zayatcsusami2@zayatc.com", "zayatcsusami2", "Заяц Усатый", LocalDate.of(2020, 12, 12));
        userController.postUser(updatedUser);
        assertEquals(1, userController.getUsers().size());
        assertNotEquals(updatedUser, userController.getUsers().get(0));

        userController.putUser(updatedUser);
        assertEquals(1, userController.getUsers().size());
        assertEquals(updatedUser, userController.getUsers().get(0));

        User user2 = new User(2, "lisiymedved@zayatc.com", "lisiymedved", "Лысый Медведь", LocalDate.of(2018, 5, 20));
        userController.postUser(user2);
        assertEquals(2, userController.getUsers().size());
        assertEquals(user2, userController.getUsers().get(1));

        User updatedUser2 = new User(2, "lisiymedved2@zayatc.com", "lisiymedved2", "Лысый Медведь", LocalDate.of(2018, 5, 20));
        userController.postUser(updatedUser2);
        assertEquals(2, userController.getUsers().size());
        assertNotEquals(updatedUser2, userController.getUsers().get(1));

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

        User user6 = new User(1, "zayatcsusami@zayatc.com", "zayatcsusami", "Заяц Усатый", LocalDate.of(2025, 12, 12));
        exception = assertThrows(
                ValidationException.class,
                () -> userController.postUser(user6));

        assertEquals("Неверно указана дата рождения!", exception.getMessage());

    }

}