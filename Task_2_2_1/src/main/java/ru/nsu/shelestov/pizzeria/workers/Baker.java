package ru.nsu.shelestov.pizzeria.workers;

import ru.nsu.shelestov.pizzeria.model.Order;
import ru.nsu.shelestov.pizzeria.queue.OrderConsumer;
import ru.nsu.shelestov.pizzeria.storage.BakeryStorage;
import ru.nsu.shelestov.pizzeria.tracker.OrderTracker;

public class Baker implements Runnable {
    private final int speed;
    private final OrderConsumer orderSource;
    private final BakeryStorage storage;
    private final OrderTracker tracker;

    public Baker(int speed, OrderConsumer orderSource, BakeryStorage storage, OrderTracker tracker) {
        this.speed = speed;
        this.orderSource = orderSource;
        this.storage = storage;
        this.tracker = tracker;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                Order order = orderSource.takeOrder();
                if (order == null) break;

                tracker.updateOrderStatus(order, Order.Status.PREPARING);

                try {
                    Thread.sleep(speed * 1000L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }

                tracker.updateOrderStatus(order, Order.Status.BAKED);
                storage.put(order);
                tracker.updateOrderStatus(order, Order.Status.STORED);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}