package ru.nsu.shelestov.task_2_4_1.model;

import java.time.LocalDateTime;

public class Checkpoint {
    private String name;
    private LocalDateTime date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}