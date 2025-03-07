package ru.nsu.shelestov.pizzeria.workers;

import ru.nsu.shelestov.pizzeria.model.Order;
import ru.nsu.shelestov.pizzeria.storage.BakeryStorage;
import ru.nsu.shelestov.pizzeria.tracker.OrderTracker;

import java.util.List;

public class Courier implements Runnable {
    private final int capacity;
    private final BakeryStorage storage;
    private final OrderTracker tracker;

    public Courier(int capacity, BakeryStorage storage, OrderTracker tracker) {
        this.capacity = capacity;
        this.storage = storage;
        this.tracker = tracker;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                List<Order> orders = storage.take(capacity);
                if (orders.isEmpty()) break;

                for (Order order : orders) {
                    tracker.updateOrderStatus(order, Order.Status.DELIVERING);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                    tracker.updateOrderStatus(order, Order.Status.DELIVERED);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}