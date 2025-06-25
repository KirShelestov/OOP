package ru.nsu.shelestov.task_2_4_1.model;

import java.time.LocalDateTime;

public class Task {
    private String id;
    private String name;
    private int maxPoints;
    private LocalDateTime softDeadline;
    private LocalDateTime hardDeadline;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    public LocalDateTime getSoftDeadline() {
        return softDeadline;
    }

    public void setSoftDeadline(LocalDateTime softDeadline) {
        this.softDeadline = softDeadline;
    }

    public LocalDateTime getHardDeadline() {
        return hardDeadline;
    }

    public void setHardDeadline(LocalDateTime hardDeadline) {
        this.hardDeadline = hardDeadline;
    }
}