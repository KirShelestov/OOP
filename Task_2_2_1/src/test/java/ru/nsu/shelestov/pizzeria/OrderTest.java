package ru.nsu.shelestov.pizzeria;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.nsu.shelestov.pizzeria.model.Order;

class OrderTest {

    @Test
    void shouldGenerateUniqueIncrementalIds() {
        Order order1 = new Order();
        Order order2 = new Order();
        assertThat(order2.getId()).isEqualTo(order1.getId() + 1);
    }

    @Test
    void shouldUpdateStatusCorrectly() {
        Order order = new Order();
        order.setStatus(Order.Status.PREPARING);
        assertThat(order.getStatus()).isEqualTo(Order.Status.PREPARING);
    }
}