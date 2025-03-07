package ru.nsu.shelestov.pizzeria.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && status == order.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status);
    }


}