package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private UserDbStorage userStorage;

    @BeforeEach
    public void createContextBefore() {
        userStorage = new UserDbStorage(jdbcTemplate);
    }

    @AfterEach
    public void createContextAfter() {
        jdbcTemplate.update("TRUNCATE TABLE friendship RESTART IDENTITY");
        jdbcTemplate.update("TRUNCATE TABLE users RESTART IDENTITY");
    }


    @Test
    public void findUserByIdTest() {
        User newUser = new User(1L, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1));
        userStorage.addUser(newUser);

        User savedUser = userStorage.getUser(1);

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }


    @Test
    public void deleteUserTest() {
        User newUser = new User(1L, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1));
        userStorage.addUser(newUser);

        userStorage.deleteUser(1);

        assertThat(userStorage.getUser(1))
                .isNull();
    }


    @Test
    public void containsUserTest() {
        User newUser = new User(6L, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1));
        userStorage.addUser(newUser);

        assertThat(userStorage.containsUser(6))
                .isTrue();
    }


    @Test
    public void getUsersTest() {
        User newUser1 = new User(1L, "user1@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1));
        User newUser2 = new User(2L, "user2@email.ru", "oksana99", "Oksana Samoylova", LocalDate.of(1991, 2, 2));
        User newUser3 = new User(3L, "user3@email.ru", "valera15", "Valera Siniy", LocalDate.of(1992, 3, 3));
        User newUser4 = new User(4L, "user4@email.ru", "artur67", "Artur Pirozhkov", LocalDate.of(1993, 4, 4));
        User newUser5 = new User(5L, "user5@email.ru", "viktoriya17", "Viktoriya Polyanskaya", LocalDate.of(1994, 5, 5));

        List<User> users = List.of(newUser1, newUser2, newUser3, newUser4, newUser5);

        userStorage.addUser(newUser1);
        userStorage.addUser(newUser2);
        userStorage.addUser(newUser3);
        userStorage.addUser(newUser4);
        userStorage.addUser(newUser5);

        List<User> savedUsers = userStorage.getUsers();

        assertThat(users)
                .isEqualTo(savedUsers);
    }


    @Test
    public void getFriendsTest() {
        User newUser1 = new User(1L, "user1@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1));
        User newUser2 = new User(2L, "user2@email.ru", "oksana99", "Oksana Samoylova", LocalDate.of(1991, 2, 2));
        User newUser3 = new User(3L, "user3@email.ru", "valera15", "Valera Siniy", LocalDate.of(1992, 3, 3));
        User newUser4 = new User(4L, "user4@email.ru", "artur67", "Artur Pirozhkov", LocalDate.of(1993, 4, 4));
        User newUser5 = new User(5L, "user5@email.ru", "viktoriya17", "Viktoriya Polyanskaya", LocalDate.of(1994, 5, 5));

        userStorage.addUser(newUser1);
        userStorage.addUser(newUser2);
        userStorage.addUser(newUser3);
        userStorage.addUser(newUser4);
        userStorage.addUser(newUser5);

        userStorage.addFriend(1L, 2L);
        userStorage.addFriend(1L, 3L);
        userStorage.addFriend(1L, 4L);
        userStorage.addFriend(1L, 5L);
        userStorage.addFriend(2L, 3L);
        userStorage.addFriend(3L, 5L);
        userStorage.addFriend(3L, 4L);
        userStorage.addFriend(5L, 2L);

        List<User> friendship = List.of(newUser2, newUser3, newUser4, newUser5);
        List<User> friendshipDB = userStorage.getFriends(1L);

        assertThat(friendship)
                .isEqualTo(friendshipDB);

        userStorage.deleteFriend(1L, 5L);
        List<User> newFriendship = List.of(newUser2, newUser3, newUser4);

        assertThat(userStorage.getFriends(1L))
                .isEqualTo(newFriendship);

    }


    @Test
    public void updateUserTest() {

        User newUser1 = new User(1L, "user1@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1));
        userStorage.addUser(newUser1);

        assertThat(userStorage.getUser(1L))
                .isEqualTo(newUser1);

        newUser1.setName("Иван Иванов");

        userStorage.updateUser(newUser1);

        assertThat(userStorage.getUser(1L))
                .isEqualTo(newUser1);

    }
}
