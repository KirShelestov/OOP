package ru.nsu.shelestov.pizzeria;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import ru.nsu.shelestov.pizzeria.model.Config;
import ru.nsu.shelestov.pizzeria.model.Order;

class PizzeriaTest {

    @Test
    void shouldProcessOrdersAndShutdownGracefully() throws Exception {
        Path tempFile = Files.createTempFile("pending", ".json");

        Config config = new Config(
                10,
                List.of(new Config.BakerConfig(2)),
                List.of(new Config.CourierConfig(3))
        );

        Pizzeria pizzeria = new Pizzeria(config) {
            private String getPendingFileName() {
                return tempFile.toString();
            }
        };

        IntStream.range(0, 5)
                .forEach(i -> pizzeria.placeOrder(new Order()));

        Thread.sleep(3000);
        pizzeria.shutdown();

        assertThat(Files.exists(tempFile)).isTrue();
    }
}
