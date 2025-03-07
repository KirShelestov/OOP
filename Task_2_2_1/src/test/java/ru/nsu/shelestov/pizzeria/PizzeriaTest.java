package ru.nsu.shelestov.pizzeria;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import ru.nsu.shelestov.pizzeria.model.Config;
import ru.nsu.shelestov.pizzeria.model.Order;

class PizzeriaTest {

    @Test
    void shouldProcessOrdersAndShutdownGracefully() throws Exception {
        Config config = Config.load("src/test/resources/config.json");
        Pizzeria pizzeria = new Pizzeria(config);

        pizzeria.start();
        for (int i = 0; i < 5; i++) {
            pizzeria.placeOrder(new Order());
        }
        Thread.sleep(3000);
        pizzeria.shutdown();

        String json = Files.readString(Paths.get("pending_orders.json"));
        assertThat(json).contains("STORED");
    }
}
