package ru.nsu.shelestov.pizzeria;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.nsu.shelestov.pizzeria.model.Order;
import ru.nsu.shelestov.pizzeria.storage.PizzaStorage;

class PizzaStorageTest {

    @Test
    void shouldBlockWhenFull() throws InterruptedException {
        PizzaStorage storage = new PizzaStorage(1);
        storage.put(new Order());

        Thread thread = new Thread(() -> {
            try {
                storage.put(new Order());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        thread.start();
        Thread.sleep(100);
        assertThat(thread.isAlive()).isTrue();
    }

    @Test
    void shouldReturnAllOrdersWhenShutdown() throws InterruptedException {
        PizzaStorage storage = new PizzaStorage(2);
        storage.put(new Order());
        storage.shutdown();

        assertThat(storage.take(10)).isEmpty();
    }
}
