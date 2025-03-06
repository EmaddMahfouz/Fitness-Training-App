package com.eventsprocessor.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "training")
public class Training {
    @Id
    @Column(name = "training_id", nullable = false, unique = true)
    private Long trainingId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "duration", nullable = false)
    private int duration;  // Duration in minutes

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Training() {

    }

    public Training(Long trainingId, String title, String description, int duration, LocalDateTime createdAt) {
        this.trainingId = trainingId;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.createdAt = createdAt;
    }

    public Long getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(Long trainingId) {
        this.trainingId = trainingId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}