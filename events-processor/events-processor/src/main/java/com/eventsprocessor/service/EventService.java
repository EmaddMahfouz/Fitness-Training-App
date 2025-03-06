package com.eventsprocessor.service;

import com.eventsprocessor.entity.Device;
import com.eventsprocessor.entity.Event;
import com.eventsprocessor.entity.User;
import com.eventsprocessor.exceptions.DeviceNotFoundException;
import com.eventsprocessor.exceptions.EventNotFoundException;
import com.eventsprocessor.exceptions.UserNotFoundException;
import com.eventsprocessor.repository.DeviceRepository;
import com.eventsprocessor.repository.EventRepository;
import com.eventsprocessor.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EventService {
    private static final Logger logger = LoggerFactory.getLogger(EventService.class);
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final DeviceRepository deviceRepository;

    public EventService(UserRepository userRepository, EventRepository eventRepository, DeviceRepository deviceRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.deviceRepository = deviceRepository;
    }

    @Transactional
    public Event startApp(Long userID, Long deviceID) {
        logger.info("Starting app event for user ID: {} with device ID: {}", userID, deviceID);

        if (userID == null || deviceID == null) {
            throw new IllegalArgumentException("User ID and Device ID must not be null");
        }

        User user = userRepository.findById(userID)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userID));

        Device device = deviceRepository.findById(deviceID)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found with ID: " + deviceID));

        // Create Event
        Event event = new Event();
        event.setEventType("launch_app");
        event.setTimestamp(LocalDateTime.now());
        event.setUser(user);
        event.setDevice(device);

        return eventRepository.save(event);
    }

    @Transactional
    public Event updateStatus(Long eventId, String eventType, Long userId) {
        logger.info("Updating event ID {} to status {} for user ID {}", eventId, eventType, userId);

        if (eventId == null || eventType == null || userId == null) {
            throw new IllegalArgumentException("Event ID, Event Type, and User ID must not be null");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + eventId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        event.setEventType(eventType);
        event.setUser(user);

        return eventRepository.save(event);
    }

}
