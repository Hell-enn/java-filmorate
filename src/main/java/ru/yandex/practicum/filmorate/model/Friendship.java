package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Класс Friendship - бизнес-сущность. Необходим для
 * последующего создания объектов-связок, каждая из
 * которых описывает статус дружбы между двумя пользователями.
 * В каждой такой связке содержатся свойства:
 * - идентификатор пользователя, отправившего заявку на добавление в друзья (followingUserId)
 * - идентификатор пользователя, которому была отправлена заявка на добавление в друзья (followedUserId)
 * - статус дружбы между двумя пользователями (accept). Принимает значение false по умолчанию при
 * отправке заявки от followingUserId к followedUserId и сохраняется до момента одобрения заявки
 * пользователем followedUserId, true - в случае, если followedUserId одобряет заявку.
 */
@Data
@AllArgsConstructor
public class Friendship {
    private final long followingUserId;
    private final long followedUserId;
    private final boolean accept;
}
