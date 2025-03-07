package ru.nsu.shelestov.pizzeria;

import ru.nsu.shelestov.pizzeria.model.Config;
import ru.nsu.shelestov.pizzeria.model.Order;

import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) throws Exception {
        Config config = Config.load("config.json");
        Pizzeria pizzeria = new Pizzeria(config);
        pizzeria.start();



        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                pizzeria.placeOrder(new Order());
                try { Thread.sleep(500); }
                catch (InterruptedException e) { break; }
            }
        }).start();

        new Timer().schedule(new TimerTask() {
            public void run() {
                pizzeria.shutdown();
                System.out.println("Pizzeria closed");
            }
        }, 60_000);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down...");
            pizzeria.shutdown();
        }));
    }
}
