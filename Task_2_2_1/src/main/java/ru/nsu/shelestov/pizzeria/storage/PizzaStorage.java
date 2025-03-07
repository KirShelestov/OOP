package ru.nsu.shelestov.pizzeria.storage;

import ru.nsu.shelestov.pizzeria.model.Order;
import java.util.*;

public class PizzaStorage implements BakeryStorage {
    private final int capacity;
    private final Queue<Order> orders = new LinkedList<>();
    private volatile boolean isShutdown = false;

    public PizzaStorage(int capacity) { this.capacity = capacity; }

    @Override
    public synchronized void put(Order order) throws InterruptedException {
        while (orders.size() >= capacity && !isShutdown) wait();
        if (isShutdown) throw new InterruptedException("Storage shutdown");
        orders.add(order);
        notifyAll();
    }

    @Override
    public synchronized List<Order> take(int max) throws InterruptedException {
        while (orders.isEmpty() && !isShutdown) wait();
        if (orders.isEmpty() && isShutdown) return Collections.emptyList();
        List<Order> taken = new ArrayList<>();
        int count = Math.min(max, orders.size());
        for (int i = 0; i < count; i++) taken.add(orders.poll());
        notifyAll();
        return taken;
    }

    @Override
    public synchronized void shutdown() {
        isShutdown = true;
        notifyAll();
    }

    @Override
    public synchronized boolean isShutdown() {
        return isShutdown;
    }

    @Override
    public synchronized boolean tryPut(Order order) {
        if (isShutdown || orders.size() >= capacity) return false;
        orders.add(order);
        notifyAll();
        return true;
    }
}