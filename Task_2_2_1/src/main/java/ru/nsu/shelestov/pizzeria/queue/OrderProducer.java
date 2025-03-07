package ru.nsu.shelestov.pizzeria.queue;

import ru.nsu.shelestov.pizzeria.model.Order;

public interface OrderProducer {
    void addOrder(Order order) throws IllegalStateException;
}