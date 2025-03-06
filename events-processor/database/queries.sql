USE events_processor;

CREATE TABLE User (
                      user_id BIGINT PRIMARY KEY,
                      email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE Device (
                        device_id BIGINT PRIMARY KEY,
                        user_id BIGINT NOT NULL,
                        device_type VARCHAR(255) NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE
);

CREATE TABLE Training (
                          training_id BIGINT PRIMARY KEY,
                          title VARCHAR(255) NOT NULL,
                          description TEXT,
                          duration INT NOT NULL,  -- Duration in minutes
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Event (
                       event_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       user_id BIGINT NOT NULL,
                       device_id BIGINT NOT NULL,
                       training_id BIGINT,
                       event_type VARCHAR(255) NOT NULL,
                       timestamp TIMESTAMP NOT NULL,
                       FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE,
                       FOREIGN KEY (device_id) REFERENCES Device(device_id) ON DELETE CASCADE,
                       FOREIGN KEY (training_id) REFERENCES Training(training_id) ON DELETE SET NULL
);


INSERT INTO User (user_id, email) VALUES
                                      (1, 'user1@example.com'),
                                      (2, 'user2@example.com');



INSERT INTO Training (training_id, title, description, duration, created_at) VALUES
                                                                                 (201, '7 Minutes of HIIT', 'High-Intensity Interval Training', 7, '2024-02-10 08:00:00'),
                                                                                 (202, '30-Min Yoga', 'Relaxing Yoga Session', 30, '2024-02-10 09:00:00'),
                                                                                 (203, 'Strength Training', 'Full-body Strength Workout', 45, '2024-02-11 07:30:00');
INSERT INTO Device (device_id, user_id, device_type, created_at) VALUES
                                                                     (201, 1, 'Treadmill', '2024-02-10 08:00:00'),
                                                                     (202, 1, 'Elliptical Machine', '2024-02-10 08:30:00'),
                                                                     (203, 2, 'Rowing Machine', '2024-02-11 09:00:00'),
                                                                     (204, 2, 'Stationary Bike', '2024-02-11 09:15:00'),
                                                                     (205, 1, 'Leg Press Machine', '2024-02-12 07:45:00'),
                                                                     (206, 2, 'Bench Press', '2024-02-12 08:30:00');
INSERT INTO Event (user_id, device_id, training_id, event_type, timestamp) VALUES
                                                                               (1, 201, NULL, 'app_launch', '2024-02-10 10:05:00'),
                                                                               (1, 201, 201, 'training_program_started', '2024-02-10 10:10:00'),
                                                                               (2, 203, NULL, 'app_launch', '2024-02-11 09:30:00'),
                                                                               (2, 203, 202, 'training_program_started', '2024-02-11 09:35:00'),
                                                                               (2, 203, 202, 'training_program_finished', '2024-02-11 10:05:00'),
                                                                               (1, 204, 203, 'training_program_cancelled', '2024-02-12 08:00:00');


