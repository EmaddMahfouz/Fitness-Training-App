package com.eventsprocessor.repository;

import com.eventsprocessor.entity.Event;
import com.eventsprocessor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    // Fetch all events of a specific type after a given timestamp
    List<Event> findByEventTypeAndTimestampAfter(String eventType, LocalDateTime timestamp);

    @Query("SELECT e FROM Event e WHERE e.eventType = :eventType")
    List<Event> findByEventType(@Param("eventType") String eventType);

    @Query("SELECT COUNT(e) FROM Event e WHERE e.user = :user AND e.eventType = 'training_program_started' AND e.timestamp > :timestamp")
    Long countUserTrainingAfterLaunch(
            @Param("user") User user,
            @Param("timestamp") LocalDateTime timestamp
    );

    @Query("SELECT e FROM Event e WHERE e.eventType = :eventType AND e.timestamp <= :timestamp AND e.notified = false")
    List<Event> findByEventTypeAndTimestampAfterAndNotifiedFalse(
            @Param("eventType") String eventType,
            @Param("timestamp") LocalDateTime timestamp
    );

}
