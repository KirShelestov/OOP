package ru.nsu.shelestov.pizzeria.tracker;

import ru.nsu.shelestov.pizzeria.model.Order;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class OrderTracker {
    private final Map<Integer, Order> orders = new ConcurrentHashMap<>();

    public void trackOrder(Order order) {
        orders.put(order.getId(), order);
    }

    public void updateOrderStatus(Order order, Order.Status status) {
        order.setStatus(status);
        System.out.printf("[%d] %s%n", order.getId(), status);
    }

    public List<Order> getPendingOrders() {
        return orders.values().stream()
                .filter(o -> o.getStatus() != Order.Status.DELIVERED)
                .toList();
    }

    public void loadOrders(List<Order> orders) {
        orders.forEach(o -> this.orders.put(o.getId(), o));
    }
}