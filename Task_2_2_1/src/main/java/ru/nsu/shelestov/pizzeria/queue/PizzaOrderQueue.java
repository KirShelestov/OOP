package ru.nsu.shelestov.pizzeria.queue;

import ru.nsu.shelestov.pizzeria.model.Order;
import java.util.LinkedList;
import java.util.Queue;

public class PizzaOrderQueue implements OrderProducer, OrderConsumer {
    private final Queue<Order> queue = new LinkedList<>();
    private volatile boolean isShutdown = false;

    @Override
    public synchronized void addOrder(Order order) {
        if (isShutdown) throw new IllegalStateException("Queue is shutdown");
        queue.add(order);
        notifyAll();
    }

    @Override
    public synchronized Order takeOrder() throws InterruptedException {
        while (queue.isEmpty() && !isShutdown) wait();
        return isShutdown ? null : queue.poll();
    }

    public synchronized void shutdown() {
        isShutdown = true;
        notifyAll();
    }

    @Override
    public synchronized boolean isShutdown() {
        return isShutdown;
    }
}