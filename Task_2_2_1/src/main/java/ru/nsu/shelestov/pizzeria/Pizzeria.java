package ru.nsu.shelestov.pizzeria;

import ru.nsu.shelestov.pizzeria.model.Config;
import ru.nsu.shelestov.pizzeria.model.Order;
import ru.nsu.shelestov.pizzeria.queue.PizzaOrderQueue;
import ru.nsu.shelestov.pizzeria.serializer.JsonOrderSerializer;
import ru.nsu.shelestov.pizzeria.serializer.OrderSerializer;
import ru.nsu.shelestov.pizzeria.storage.PizzaStorage;
import ru.nsu.shelestov.pizzeria.tracker.OrderTracker;
import ru.nsu.shelestov.pizzeria.workers.Baker;
import ru.nsu.shelestov.pizzeria.workers.Courier;
import java.util.List;
import java.util.ArrayList;

public class Pizzeria {
    private final PizzaOrderQueue orderQueue = new PizzaOrderQueue();
    private final PizzaStorage storage;
    private final OrderTracker tracker = new OrderTracker();
    private final OrderSerializer serializer = new JsonOrderSerializer();
    private final List<Thread> workers = new ArrayList<>();

    private volatile boolean isShutdown = false;

    public Pizzeria(Config config) {
        this.storage = new PizzaStorage(config.storage_capacity());
        loadPreviousState();

        config.bakers().forEach(b ->
                workers.add(new Thread(new Baker(
                        b.speed(),
                        orderQueue,
                        storage,
                        tracker
                )))
        );

        config.couriers().forEach(c ->
                workers.add(new Thread(new Courier(
                        c.capacity(),
                        storage,
                        tracker
                )))
        );
    }

    public void start() {
        workers.forEach(Thread::start);
    }

    public void placeOrder(Order order) {
        try {
            tracker.trackOrder(order);
            orderQueue.addOrder(order);
        } catch (IllegalStateException e) {
            System.out.println("Cannot accept order: " + e.getMessage());
        }
    }

    public void shutdown() {
        if (isShutdown) return;
        isShutdown = true;

        orderQueue.shutdown();
        storage.shutdown();

        workers.forEach(Thread::interrupt);
        workers.forEach(t -> {
            try {
                t.join(3000);
                if (t.isAlive()) {
                    System.err.println("Thread " + t.getId() + " failed to stop!");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        serializer.serialize(tracker.getPendingOrders(), "pending_orders.json");
        System.out.println("Pizzeria closed");
        System.exit(0);
    }

    private void loadPreviousState() {
        List<Order> pending = serializer.deserialize("pending_orders.json");
        if (!pending.isEmpty()) {
            System.out.println("[INFO] Loaded " + pending.size() + " pending orders");
            tracker.loadOrders(pending);
        }

        pending.stream()
                .filter(o -> o.getStatus() == Order.Status.STORED)
                .forEach(order -> {
                    if (!storage.tryPut(order)) {
                        System.out.println("[WARN] Failed to restore order " + order.getId());
                    }
                });
    }
}