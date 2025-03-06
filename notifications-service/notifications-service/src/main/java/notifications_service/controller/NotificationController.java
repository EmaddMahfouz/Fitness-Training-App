package notifications_service.controller;

import notifications_service.dto.NotificationRequest;
import notifications_service.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/notify")
public class NotificationController {
    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
    private final EmailService emailService;

    public NotificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<String> sendNotification(@RequestBody NotificationRequest request) {
        logger.info("Received notification request: userEmail={}, message={}", request.getUserEmail(), request.getMessage());

        emailService.sendEmail(request.getUserEmail(), "Fitness App Notification", request.getMessage());

        return ResponseEntity.ok("Notification sent to: " + request.getUserEmail());
    }


}
