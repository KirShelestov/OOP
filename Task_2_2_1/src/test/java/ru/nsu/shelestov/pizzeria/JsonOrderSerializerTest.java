package ru.nsu.shelestov.pizzeria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import ru.nsu.shelestov.pizzeria.model.Order;
import ru.nsu.shelestov.pizzeria.serializer.JsonOrderSerializer;

class JsonOrderSerializerTest {

    @Test
    void shouldSerializeAndDeserializeOrders() {
        JsonOrderSerializer serializer = new JsonOrderSerializer();
        List<Order> orders = List.of(new Order(), new Order());

        serializer.serialize(orders, "test_orders.json");
        List<Order> loaded = serializer.deserialize("test_orders.json");

        assertThat(loaded).hasSize(2);
        assertThat(loaded.getFirst().getId()).isEqualTo(1);
    }
}