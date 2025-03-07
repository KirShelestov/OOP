package ru.nsu.shelestov.pizzeria;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import ru.nsu.shelestov.pizzeria.model.Order;
import ru.nsu.shelestov.pizzeria.queue.PizzaOrderQueue;

class PizzaOrderQueueTest {

    @Test
    void shouldAddAndRetrieveOrder() throws InterruptedException {
        PizzaOrderQueue queue = new PizzaOrderQueue();
        Order order = new Order();

        queue.addOrder(order);
        Order retrieved = queue.takeOrder();

        assertThat(retrieved).isEqualTo(order);
    }

    @Test
    void shouldThrowWhenAddingToShutdownQueue() {
        PizzaOrderQueue queue = new PizzaOrderQueue();
        queue.shutdown();

        assertThrows(IllegalStateException.class, () ->
                queue.addOrder(new Order())
        );
    }
}
