package ru.nsu.shelestov.pizzeria.storage;

import ru.nsu.shelestov.pizzeria.model.Order;
import java.util.List;

public interface BakeryStorage {
    void put(Order order) throws InterruptedException;
    List<Order> take(int max) throws InterruptedException;
    void shutdown();
    boolean isShutdown();
    boolean tryPut(Order order);
}