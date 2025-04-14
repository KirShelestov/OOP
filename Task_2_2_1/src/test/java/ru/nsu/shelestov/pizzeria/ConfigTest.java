package ru.nsu.shelestov.pizzeria;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import ru.nsu.shelestov.pizzeria.model.Config;

import java.io.InputStream;
import java.io.InputStreamReader;

class ConfigTest {

    @Test
    void shouldLoadConfigFromFile() throws Exception {
        try (InputStream is = getClass().getResourceAsStream("/config.json")) {
            Config config = new Gson().fromJson(
                    new InputStreamReader(is),
                    Config.class
            );
            assertThat(config.bakers()).hasSize(2);
        }
    }
}