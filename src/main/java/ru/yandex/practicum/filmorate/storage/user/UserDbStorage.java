package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.*;

/**
 * Класс UserDbStorage - реализация интерфейса UserStorage
 * которая взаимодействует с SQL базой данных для работы с сущностями-пользователями.
 */
@Component("userDbStorage")
public class UserDbStorage implements UserStorage {

    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public User addUser(User user) {

        if (containsUser(user.getId())) {
            throw new AlreadyExistsException("Пользователь уже существует!");
        }

        String insertUserQuery =
                "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";

         KeyHolder keyHolder = new GeneratedKeyHolder();

         jdbcTemplate.update(connection -> {
         PreparedStatement stmt = connection.prepareStatement(insertUserQuery, new String[]{"user_id"});
         stmt.setString(1, user.getEmail());
         stmt.setString(2, user.getLogin());
         stmt.setString(3, user.getName());
         stmt.setDate(4, Date.valueOf(user.getBirthday()));
         return stmt;
         }, keyHolder);

         user.setId(keyHolder.getKey().longValue());

        log.debug("Добавлен новый пользователь: {}!", user.getLogin());

        return getUser(user.getId());
    }


    @Override
    public User updateUser(User user) {

        String findUserQuery = "SELECT * FROM users WHERE user_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(findUserQuery, user.getId());
        if (!userRows.next()) {
            throw new NotFoundException("Пользователь не найден!");
        } else {

            User addedUser = getUser(userRows.getLong("user_id"));
            if (!user.getEmail().equals(addedUser.getEmail())) {
                String emailQuery = "UPDATE users " +
                        "SET email = ? " +
                        "WHERE user_id = ?;";
                jdbcTemplate.update(emailQuery, user.getEmail(), user.getId());
            }

            String loginQuery;
            if (user.getLogin() != null) {
                if (!user.getLogin().equals(addedUser.getLogin())) {
                    loginQuery = "UPDATE users " +
                            "SET login = ? " +
                            "WHERE user_id = ?;";
                    jdbcTemplate.update(loginQuery, user.getLogin(), user.getId());
                }
            } else {
                loginQuery = "UPDATE users " +
                        "SET login = NULL " +
                        "WHERE user_id = ?;";
                jdbcTemplate.update(loginQuery, user.getId());
            }

            if (!user.getName().equals(addedUser.getName())) {
                String nameQuery = "UPDATE users " +
                        "SET name = ? " +
                        "WHERE user_id = ?;";
                jdbcTemplate.update(nameQuery, user.getName(), user.getId());
            }

            if (user.getBirthday() != addedUser.getBirthday()) {
                String birthdayQuery = "UPDATE users " +
                        "SET birthday = ? " +
                        "WHERE user_id = ?;";
                jdbcTemplate.update(birthdayQuery, user.getBirthday(), user.getId());
            }
            return user;
        }
    }


    @Override
    public void deleteUser(long id) {

        String sqlQuery = "DELETE FROM users WHERE user_id = ?";
        if (jdbcTemplate.update(sqlQuery, id) > 0)
            log.info("Удален пользователь с id = {}", id);
        else
            log.info("Пользователь с id = {} не удален или отсутствует в списке", id);

    }


    @Override
    public User getUser(long id) {

        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * " +
                "FROM users " +
                "WHERE user_id = ?", id);

        if (userRows.next()) {
            return getUserFromSqlRow(userRows);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            return null;
        }

    }


    @Override
    public boolean containsUser(long id) {
        return jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE user_id = ?", id).next();
    }


    @Override
    public List<User> getUsers() {

        List<User> users = new ArrayList<>();

        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users");

        while (userRows.next()) {
            users.add(getUserFromSqlRow(userRows));
        }

        if (users.isEmpty())
            log.info("Пользователи не найдены.");

        return users;

    }


    /**
     * Закрытый служебный метод используется для получения объекта
     * пользователя из строки, полученной из базы данных (таблица 'users').
     * @param userRows
     * @return
     */
    private User getUserFromSqlRow(SqlRowSet userRows) {

        int userId = userRows.getInt("user_id");
        String email = userRows.getString("email");
        String login = userRows.getString("login");
        String name = userRows.getString("name");
        LocalDate birthday;
        try {
            birthday = userRows.getDate("birthday").toLocalDate();
        } catch (NullPointerException exception) {
            birthday = null;
        }

        User user = new User(userId, email, login, name, birthday);

        log.info("Найден пользователь: {} {}", user.getId(), user.getLogin());

        return user;
    }


    @Override
    public User addFriend(Long followingFriendId, Long followedFriendId) {

        if (followedFriendId.equals(followingFriendId)) {
            throw new BadRequestException("Нельзя добавить в друзья самого себя!");
        }

        String friendshipQuery = "SELECT * " +
                                 "FROM friendship " +
                                 "WHERE following_user_id = ? AND followed_user_id = ?";

        SqlRowSet followingFriendRows = jdbcTemplate.queryForRowSet(friendshipQuery, followingFriendId, followedFriendId);
        SqlRowSet followedFriendRows = jdbcTemplate.queryForRowSet(friendshipQuery, followedFriendId, followingFriendId);

        boolean isFollowingRowPresent = followingFriendRows.next();
        boolean isFollowedRowPresent = followedFriendRows.next();
        if (!isFollowingRowPresent && !isFollowedRowPresent) {

            jdbcTemplate.update(
                    "INSERT INTO friendship (following_user_id, followed_user_id) VALUES (?, ?)",
                    followingFriendId, followedFriendId);
            log.info("Заявка на добавление в друзья от пользователя с id {} отправлена пользователю с id {}!", followingFriendId, followedFriendId);

        } else if (!isFollowingRowPresent) {

            SqlRowSet followedFriendRows2 = jdbcTemplate.queryForRowSet(friendshipQuery, followedFriendId, followingFriendId);

            if (followedFriendRows2.next()) {
                if (followedFriendRows2.getBoolean("accept"))
                    throw new AlreadyExistsException("Данные пользователь уже в списке Ваших друзей!");
            }

            jdbcTemplate.update(
                    "UPDATE friendship SET accept = true WHERE following_user_id = ? AND followed_user_id = ?",
                    followedFriendId, followingFriendId);
            log.info("Заявка на добавление в друзья от пользователя с id {} одобрена пользователем с id {}!", followedFriendId, followingFriendId);

        } else if (!isFollowedRowPresent) {

            SqlRowSet followingFriendRows2 = jdbcTemplate.queryForRowSet(friendshipQuery, followingFriendId, followedFriendId);

            if (followingFriendRows2.next()) {
                if (followingFriendRows2.getBoolean("accept"))
                    throw new AlreadyExistsException("Данные пользователь уже в списке Ваших друзей!");
            }
            jdbcTemplate.update(
                    "UPDATE friendship SET accept = true WHERE following_user_id = ? AND followed_user_id = ?",
                    followingFriendId, followedFriendId);
            log.info("Заявка на добавление в друзья от пользователя с id {} одобрена пользователем с id {}!", followedFriendId, followingFriendId);
        }

        return getUser(followingFriendId);

    }

    @Override
    public void deleteFriend(Long followingFriendId, Long followedFriendId) {

        if (!containsUser(followingFriendId) || !containsUser(followedFriendId)) {
            throw new NotFoundException("Пользователь не найден!");
        }

        String friendshipQuery = "SELECT * " +
                                 "FROM friendship " +
                                 "WHERE following_user_id = ? AND followed_user_id = ?";

        SqlRowSet followingFriendRows = jdbcTemplate.queryForRowSet(friendshipQuery, followingFriendId, followedFriendId);
        SqlRowSet followedFriendRows = jdbcTemplate.queryForRowSet(friendshipQuery, followedFriendId, followingFriendId);

        int amount = 0;

        if (followingFriendRows.next() && !followedFriendRows.next()) {

            if (followingFriendRows.getBoolean("accept")) {

                jdbcTemplate.update(
                        "INSERT INTO friendship (following_user_id, followed_user_id) VALUES (?, ?)",
                        followedFriendId, followingFriendId);

            }
            amount = jdbcTemplate.update(
                    "DELETE friendship WHERE following_user_id = ? AND followed_user_id = ?",
                    followingFriendId, followedFriendId);

        }

        if (amount > 0)
            log.info("Подписка пользователя с id {} отменена с пользователя с id {}!", followingFriendId, followedFriendId);

        else
            log.info("Подписка отсутствует в списке!");

    }


    @Override
    public List<User> getFriends(Long userId) {

        if (!containsUser(userId))
            throw new NotFoundException("Пользователь не найден!");

        List<User> friends = new ArrayList<>();

        String friendsQuery = "SELECT u.* " +
                              "FROM friendship f " +
                              "JOIN users u ON u.user_id = f.followed_user_id " +
                              "WHERE following_user_id = ?";

        SqlRowSet friendsOfUserRows = jdbcTemplate.queryForRowSet(friendsQuery, userId);

        while (friendsOfUserRows.next()) {
            User user = getUserFromSqlRow(friendsOfUserRows);
            log.info("Найден пользователь (id = {}), на которого подписан пользователь с id = {}!", user.getId(), userId);
            friends.add(user);
        }

        if (friends.isEmpty())
            log.info("Информация о подписках пользователя с id = {} отсутсвует!", userId);

        return friends;

    }


    public List<User> getCommonFriends(long user1Id, long user2Id) {

        List<User> friends1 = getFriends(user1Id);
        List<User> friends2 = getFriends(user2Id);

        List<User> commonFriends = new ArrayList<>();

        for (User friend: friends1) {
            if (friends2.contains(friend))
                commonFriends.add(friend);
        }

        return commonFriends;
    }
}


