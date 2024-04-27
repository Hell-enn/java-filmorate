package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
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


    @Test
    public void findUserByIdTest() {
        User newUser = new User(1L, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1));
        newUser.setId(userStorage.addUser(newUser).getId());

        User savedUser = userStorage.getUser(newUser.getId());

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
        newUser.setId(userStorage.addUser(newUser).getId());
        assertThat(userStorage.containsUser(newUser.getId()))
                .isTrue();
    }


    @Test
    public void getUsersTest() {
        User newUser1 = new User(1L, "user1@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1));
        User newUser2 = new User(2L, "user2@email.ru", "oksana99", "Oksana Samoylova", LocalDate.of(1991, 2, 2));
        User newUser3 = new User(3L, "user3@email.ru", "valera15", "Valera Siniy", LocalDate.of(1992, 3, 3));
        User newUser4 = new User(4L, "user4@email.ru", "artur67", "Artur Pirozhkov", LocalDate.of(1993, 4, 4));
        User newUser5 = new User(5L, "user5@email.ru", "viktoriya17", "Viktoriya Polyanskaya", LocalDate.of(1994, 5, 5));

        jdbcTemplate.update("DELETE FROM users");

        newUser1.setId(userStorage.addUser(newUser1).getId());
        newUser2.setId(userStorage.addUser(newUser2).getId());
        newUser3.setId(userStorage.addUser(newUser3).getId());
        newUser4.setId(userStorage.addUser(newUser4).getId());
        newUser5.setId(userStorage.addUser(newUser5).getId());

        List<User> users = List.of(newUser1, newUser2, newUser3, newUser4, newUser5);

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

        newUser1.setId(userStorage.addUser(newUser1).getId());
        newUser2.setId(userStorage.addUser(newUser2).getId());
        newUser3.setId(userStorage.addUser(newUser3).getId());
        newUser4.setId(userStorage.addUser(newUser4).getId());
        newUser5.setId(userStorage.addUser(newUser5).getId());

        jdbcTemplate.update("DELETE FROM friendship");

        userStorage.addFriend(newUser1.getId(), newUser2.getId());
        userStorage.addFriend(newUser1.getId(), newUser3.getId());
        userStorage.addFriend(newUser1.getId(), newUser4.getId());
        userStorage.addFriend(newUser1.getId(), newUser5.getId());
        userStorage.addFriend(newUser2.getId(), newUser3.getId());
        userStorage.addFriend(newUser3.getId(), newUser5.getId());
        userStorage.addFriend(newUser3.getId(), newUser4.getId());
        userStorage.addFriend(newUser5.getId(), newUser2.getId());

        List<User> friendship = List.of(newUser2, newUser3, newUser4, newUser5);
        List<User> friendshipDB = userStorage.getFriends(newUser1.getId());

        assertThat(friendship)
                .isEqualTo(friendshipDB);

        userStorage.deleteFriend(newUser1.getId(), newUser5.getId());
        List<User> newFriendship = List.of(newUser2, newUser3, newUser4);

        assertThat(userStorage.getFriends(newUser1.getId()))
                .isEqualTo(newFriendship);

    }


    @Test
    public void updateUserTest() {

        User newUser1 = new User(1L, "user1@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1));
        newUser1.setId(userStorage.addUser(newUser1).getId());

        assertThat(userStorage.getUser(newUser1.getId()))
                .isEqualTo(newUser1);

        newUser1.setName("Иван Иванов");

        userStorage.updateUser(newUser1);

        assertThat(userStorage.getUser(newUser1.getId()))
                .isEqualTo(newUser1);

    }
}
