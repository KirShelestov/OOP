package ru.nsu.shelestov.pizzeria.model;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Order implements Serializable {
    private static final AtomicInteger idCounter = new AtomicInteger(0);
    private final int id;
    private volatile Status status;

    public Order() {
        this.id = idCounter.incrementAndGet();
        this.status = Status.ORDERED;
    }

    public int getId() { return id; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public enum Status {
        ORDERED, PREPARING, BAKED, STORED, DELIVERING, DELIVERED
    }
}