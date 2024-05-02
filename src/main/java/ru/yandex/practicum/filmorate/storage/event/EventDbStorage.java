package ru.yandex.practicum.filmorate.storage.event;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

@Component("eventDbStorage")
public class EventDbStorage implements EventStorage{

    JdbcTemplate jdbcTemplate;

    public EventDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Event> getUserFeed(long id){
        String getFeedQuery = "SELECT * FROM events WHERE user_id = ?;";
        return jdbcTemplate.query(getFeedQuery, (rs, rowNum) -> {
            int eventId = rs.getInt("event_id");
            long timestamp = rs.getLong("timestamp");
            int userId = rs.getInt("user_id");
            String eventType = rs.getString("event_type");
            String operation = rs.getString("operation");
            int entityId = rs.getInt("entity_id");

            return new Event(eventId, timestamp, userId, eventType, operation, entityId);
        }, id);
    }

    @Override
    public void addEvent(Event event){
        String insertEventQuery = "INSERT INTO events (timestamp, user_id, event_type, operation, entity_id)" +
                "VALUES (?, ?, ?, ?, ?);";

        jdbcTemplate.update(insertEventQuery, event.getTimestamp(), event.getUserId(), event.getEventType(),
                event.getOperation(), event.getEntityId());
    }
}
