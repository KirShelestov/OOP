package ru.nsu.shelestov.pizzeria;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.nsu.shelestov.pizzeria.model.Order;
import ru.nsu.shelestov.pizzeria.storage.PizzaStorage;
import ru.nsu.shelestov.pizzeria.tracker.OrderTracker;
import ru.nsu.shelestov.pizzeria.workers.Courier;

class CourierTest {

    @Test
    void shouldDeliverOrders() throws InterruptedException {
        PizzaStorage storage = new PizzaStorage(10);
        OrderTracker tracker = new OrderTracker();
        Courier courier = new Courier(2, storage, tracker);

        storage.put(new Order());
        storage.put(new Order());

        Thread courierThread = new Thread(courier);
        courierThread.start();
        courierThread.join(3000);

        assertThat(tracker.getPendingOrders()).isEmpty();
    }
}
