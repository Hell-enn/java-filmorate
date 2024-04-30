package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class Event {
    private int eventId;
    @NotNull
    private long timestamp;
    @NotNull
    private long userId;
    @NotNull
    @NotBlank
    private String eventType;
    @NotNull
    @NotBlank
    private String operation;
    @NotNull
    private long entityId;

    public Event(long timestamp, long userId, String eventType, String operation, long entityId) {
        this.timestamp = timestamp;
        this.userId = userId;
        this.eventType = eventType;
        this.operation = operation;
        this.entityId = entityId;
    }
}