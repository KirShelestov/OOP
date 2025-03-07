package ru.nsu.shelestov.pizzeria;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.nsu.shelestov.pizzeria.model.Order;
import ru.nsu.shelestov.pizzeria.queue.PizzaOrderQueue;
import ru.nsu.shelestov.pizzeria.storage.PizzaStorage;
import ru.nsu.shelestov.pizzeria.tracker.OrderTracker;
import ru.nsu.shelestov.pizzeria.workers.Baker;

class BakerTest {

    @Test
    void shouldProcessOrder() throws InterruptedException {
        PizzaOrderQueue queue = new PizzaOrderQueue();
        PizzaStorage storage = new PizzaStorage(10);
        OrderTracker tracker = new OrderTracker();
        Baker baker = new Baker(1, queue, storage, tracker);

        Order order = new Order();
        queue.addOrder(order);

        Thread bakerThread = new Thread(baker);
        bakerThread.start();
        bakerThread.join(2000);

        assertThat(order.getStatus()).isEqualTo(Order.Status.STORED);
    }
}
