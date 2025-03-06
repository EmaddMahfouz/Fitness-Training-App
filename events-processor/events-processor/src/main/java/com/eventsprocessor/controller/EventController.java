package com.eventsprocessor.controller;

import com.eventsprocessor.exceptions.DeviceNotFoundException;
import com.eventsprocessor.exceptions.EventNotFoundException;
import com.eventsprocessor.exceptions.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.eventsprocessor.entity.Event;
import com.eventsprocessor.service.EventService;

import java.util.Map;

@RestController
@RequestMapping("v1/event")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/launch_app")
    public ResponseEntity<?> createEvent(@RequestBody Map<String, Object> request) {
        try {
            Long userID = request.containsKey("userID") ? ((Number) request.get("userID")).longValue() : null;
            Long deviceID = request.containsKey("deviceId") ? ((Number) request.get("deviceId")).longValue() : null;

            if (userID == null || deviceID == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Missing userID or deviceId"));
            }

            Event event = eventService.startApp(userID, deviceID);
            return ResponseEntity.ok(Map.of("message", "Event created successfully"));
        } catch (UserNotFoundException | DeviceNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal Server Error", "message", e.getMessage()));
        }
    }


    @PutMapping("/update-status")
    public ResponseEntity<?> updateStatus(@RequestBody Map<String, Object> request) {
        try {
            Long eventId = request.containsKey("id") ? ((Number) request.get("id")).longValue() : null;
            String eventType = (String) request.get("type");
            Long userId = request.containsKey("userId") ? ((Number) request.get("userId")).longValue() : null;

            if (eventId == null || eventType == null || eventType.isBlank() || userId == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Missing required fields"));
            }

            Event event = eventService.updateStatus(eventId, eventType, userId);
            return ResponseEntity.ok(event);
        } catch (EventNotFoundException | UserNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal Server Error", "message", e.getMessage()));
        }
    }

}
