package ru.nsu.shelestov.pizzeria.serializer;

import ru.nsu.shelestov.pizzeria.model.Order;
import java.util.List;

public interface OrderSerializer {
    void serialize(List<Order> orders, String filename);
    List<Order> deserialize(String filename);
}