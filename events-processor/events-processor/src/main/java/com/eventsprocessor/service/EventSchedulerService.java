package com.eventsprocessor.service;

import com.eventsprocessor.entity.Event;
import com.eventsprocessor.entity.Training;
import com.eventsprocessor.entity.User;
import com.eventsprocessor.repository.EventRepository;
import com.eventsprocessor.repository.TrainingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class EventSchedulerService {
    private static final Logger logger = LoggerFactory.getLogger(EventSchedulerService.class);

    private final EventRepository eventRepository;
    private final TrainingRepository trainingRepository;
    private final RestTemplate restTemplate;

    private static final String NOTIFICATION_SERVICE_URL = "http://localhost:8081/v1/notify";

    public EventSchedulerService(EventRepository eventRepository,
                                 TrainingRepository trainingRepository,
                                 RestTemplate restTemplate) {
        this.eventRepository = eventRepository;
        this.trainingRepository = trainingRepository;
        this.restTemplate = restTemplate;
    }


    @Scheduled(fixedRate = 60000)
    public void checkInactiveUsers() {
        logger.info("CheckInactiveUsers() is running...");

        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);
        List<Event> appLaunchEvents = eventRepository.findByEventTypeAndTimestampAfterAndNotifiedFalse(
                "launch_app", tenMinutesAgo
        );

        for (Event event : appLaunchEvents) {
            User user = event.getUser();
            logger.info("Found user {} who launched the app!", user.getUserId());

            boolean hasStartedTraining = eventRepository.countUserTrainingAfterLaunch(user, event.getTimestamp()) > 0;

            if (!hasStartedTraining) {
                logger.info("Sending notification to user: {}", user.getUserId());
                sendNotification(user, "Hey! Start your training session now!");

                event.setNotified(true);
                eventRepository.save(event);
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    public void checkLongTrainingSessions() {
        logger.info("CheckLongTrainingSessions() is running...");

        LocalDateTime thirtyMinutesAgo = LocalDateTime.now().minusMinutes(30);
        List<Event> completedTrainings = eventRepository.findByEventTypeAndTimestampAfterAndNotifiedFalse(
                "training_program_finished", thirtyMinutesAgo
        );

        for (Event event : completedTrainings) {
            User user = event.getUser();
            logger.info("Found completed training for user {}!", user.getUserId());

            if (event.getTraining() == null) {
                logger.warn("Skipping event {}: No associated training!", event.getEventId());
                event.setNotified(true); // Mark it as notified so it won't be checked again
                eventRepository.save(event);
                continue;
            }

            Training training = event.getTraining();

            if (training.getDuration() > 30) {
                logger.info("Sending congratulations notification to user: {}", user.getUserId());
                String message = "Great job, " + user.getUserId() + "! You’ve completed your daily training with "
                        + training.getDuration() + " minutes of effort! Keep pushing towards your fitness goals!";

                sendNotification(user, message);
            }
            event.setNotified(true);
            eventRepository.save(event);
        }
    }


    @Scheduled(fixedRate = 60000)
    public void checkCancelledTrainingSessions() {
        logger.info("CheckCancelledTrainingSessions() is running...");

        List<Event> cancelledTrainings = eventRepository.findByEventType("training_program_cancelled");

        for (Event event : cancelledTrainings) {
            User user = event.getUser();
            logger.info("Deleting cancelled training event for user {}!", user.getUserId());

            eventRepository.delete(event);
        }
    }


    private void sendNotification(User user, String message) {
        logger.info("Preparing to send notification to user {}: {}", user.getUserId(), message);

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            logger.warn("Skipping notification: User {} has no email.", user.getUserId());
            return;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            Map<String, Object> notificationRequest = Map.of(
                    "userEmail", user.getEmail(),
                    "message", message
            );

            logger.info("Sending request to notification-service: {}", notificationRequest);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(notificationRequest, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(NOTIFICATION_SERVICE_URL, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                logger.info("Notification sent successfully to user {}", user.getUserId());
            } else {
                logger.error("Failed to send notification. Status: {}, Response: {}", response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            logger.error("Error sending notification to user {}: {}", user.getUserId(), e.getMessage());
        }
    }


}
