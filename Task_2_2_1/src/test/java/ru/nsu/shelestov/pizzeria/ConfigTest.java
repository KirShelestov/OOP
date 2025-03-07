package ru.nsu.shelestov.pizzeria;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.nsu.shelestov.pizzeria.model.Config;

class ConfigTest {

    @Test
    void shouldLoadConfigFromFile() throws Exception {
        Config config = Config.load("src/test/resources/config.json");

        assertThat(config.bakers()).hasSize(2);
        assertThat(config.couriers().getFirst().capacity()).isEqualTo(5);
    }
}