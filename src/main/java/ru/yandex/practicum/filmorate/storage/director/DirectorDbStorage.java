package ru.yandex.practicum.filmorate.storage.director;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс DirectorDbStorage - реализация интерфейса DirectorStorage
 * которая взаимодействует с SQL базой данных для работы с сущностями-режиссерами фильмов.
 */
@Component("directorDbStorage")
public class DirectorDbStorage implements DirectorStorage {
    private final Logger log = LoggerFactory.getLogger(DirectorDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    public DirectorDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Director addDirector(Director director) {
        String sql = "INSERT INTO directors (name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"director_id"});
            stmt.setString(1, director.getName());
            return stmt;
        }, keyHolder);
        director.setId(keyHolder.getKey().longValue());
        log.info("Режиссер {} добавлен с ID {}", director.getName(), director.getId());
        return director;
    }

    @Override
    public Director updateDirector(Director director) {
        String sql = "UPDATE directors SET name = ? WHERE director_id = ?";
        int updatedRows = jdbcTemplate.update(sql, director.getName(), director.getId());
        if (updatedRows == 0) {
            log.info("Режиссер с  ID {} не найден.", director.getId());
            throw new NotFoundException("Директор с ID " + director.getId() + " не найден.");
        }
        return director;
    }

    @Override
    public void deleteDirector(long id) {
        String sql = "DELETE FROM directors WHERE director_id = ?";
        if (jdbcTemplate.update(sql, id) > 0) {
            log.info("Режиссер с ID {} удален.", id);
        } else {
            log.info("Режиссер с ID {} не найден для удаления.", id);
        }
    }

    @Override
    public Director getDirector(long id) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * FROM directors WHERE director_id = ?", id);
        if (!rowSet.next()) {
            log.debug("Директор с ID {} не найден.", id);
            return null;
        }
        return new Director(rowSet.getLong("director_id"), rowSet.getString("name"));
    }

    @Override
    public List<Director> getDirectors() {
        List<Director> directors = new ArrayList<>();
        SqlRowSet directorRows = jdbcTemplate.queryForRowSet("SELECT * FROM directors");
        while (directorRows.next()) {
            directors.add(getDirectorFromSqlRow(directorRows));
        }
        if (directors.isEmpty()) {
            log.info("Режиссеры не найдены.");
        }
        return directors;
    }

    @Override
    public boolean containsDirector(long id) {
        String sql = "SELECT COUNT(*) FROM directors WHERE director_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class);
        return count != null && count > 0;
    }


    private Director getDirectorFromSqlRow(SqlRowSet directorRows) {
        int directorId = directorRows.getInt("director_id");
        String name = directorRows.getString("name");
        Director director = new Director(directorId, name);
        log.info("Найден режиссер: {} {}", director.getId(), director.getName());
        return director;
    }
}

