package ru.nsu.shelestov.pizzeria;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.nsu.shelestov.pizzeria.model.Order;
import ru.nsu.shelestov.pizzeria.tracker.OrderTracker;

class OrderTrackerTest {

    @Test
    void shouldTrackOrderStatuses() {
        OrderTracker tracker = new OrderTracker();
        Order order = new Order();

        tracker.trackOrder(order);
        order.setStatus(Order.Status.DELIVERED);

        assertThat(tracker.getPendingOrders()).isEmpty();
    }
}
