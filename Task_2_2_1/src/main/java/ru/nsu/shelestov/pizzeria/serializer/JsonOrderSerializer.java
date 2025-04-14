package ru.nsu.shelestov.pizzeria.serializer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.nsu.shelestov.pizzeria.model.Order;
import java.io.*;
import java.util.List;

public class JsonOrderSerializer implements OrderSerializer {
    @Override
    public void serialize(List<Order> orders, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            new Gson().toJson(orders, writer);
            System.out.println("[INFO] Saved " + orders.size() + " orders to " + filename);
        } catch (IOException e) {
            System.err.println("[ERROR] Serialization failed: " + e.getMessage());
        }
    }

    @Override
    public List<Order> deserialize(String filename) {
        try (FileReader reader = new FileReader(filename)) {
            return new Gson().fromJson(reader, new TypeToken<List<Order>>(){}.getType());
        } catch (IOException e) {
            System.err.println("[ERROR] Deserialization failed: " + e.getMessage());
            return List.of();
        }
    }
}