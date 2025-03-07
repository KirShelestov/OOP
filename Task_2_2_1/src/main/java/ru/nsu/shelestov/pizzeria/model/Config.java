package ru.nsu.shelestov.pizzeria.model;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public record Config(
        int storage_capacity,
        List<BakerConfig> bakers,
        List<CourierConfig> couriers
) {
    public record BakerConfig(int speed) {}
    public record CourierConfig(int capacity) {}

    public static Config load(String filename) throws Exception {
        try (InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(filename)) {
            if (inputStream == null) {
                throw new FileNotFoundException("Файл " + filename + " не найден в ресурсах");
            }
            return new Gson().fromJson(new InputStreamReader(inputStream), Config.class);
        }
    }

}
