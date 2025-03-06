package com.eventsprocessor.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "device")
public class Device {
    @Id
    @Column(name = "device_id", nullable = false, unique = true)
    private Long deviceId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "device_type", nullable = false)
    private String deviceType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}