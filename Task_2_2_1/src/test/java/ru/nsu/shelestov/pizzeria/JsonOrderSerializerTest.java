package ru.nsu.shelestov.pizzeria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.shelestov.pizzeria.model.Order;
import ru.nsu.shelestov.pizzeria.serializer.JsonOrderSerializer;

class JsonOrderSerializerTest {

    private JsonOrderSerializer serializer;
    private final String TEST_FILE = "test_orders.json";

    @BeforeEach
    void setUp() {
        serializer = new JsonOrderSerializer();
        Order.resetIdCounter(); 
    }

    @Test
    void shouldSerializeAndDeserializeOrders() {
        Order order1 = new Order();
        order1.setStatus(Order.Status.PREPARING);

        Order order2 = new Order();
        order2.setStatus(Order.Status.DELIVERED);

        List<Order> original = List.of(order1, order2);

        serializer.serialize(original, TEST_FILE);
        List<Order> loaded = serializer.deserialize(TEST_FILE);

        assertThat(loaded)
                .hasSize(2)
                .extracting(Order::getId)
                .containsExactly(1, 2);

        assertThat(loaded)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(original);
    }
}