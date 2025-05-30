package ru.nsu.shelestov.monitoring;

import org.junit.jupiter.api.Test;
import ru.nsu.shelestov.server.TaskServer;
import ru.nsu.shelestov.worker.WorkerClient;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class MonitoringIntegrationTest {
    private static final Logger logger = Logger.getLogger(MonitoringIntegrationTest.class.getName());

    @Test
    void testMonitoringSystem() throws Exception {
        // Настройка сервера
        int port = 8445;
        String progressFile = "test_progress.dat";
        String keystorePath = "server.jks";
        String keystorePassword = "serverpass";

        // Запуск сервера в отдельном потоке
        TaskServer server = new TaskServer(port, progressFile, keystorePath, keystorePassword);
        Thread serverThread = new Thread(server::start);
        serverThread.start();

        // Ждем запуска сервера
        Thread.sleep(2000);

        try {
            // Тест 1: Нормальное подключение работника
            WorkerClient worker1 = new WorkerClient("localhost", port, "worker1", 
                "worker1_progress.dat", keystorePath, keystorePassword);
            worker1.start();
            
            Thread.sleep(5000);
            
            // Тест 2: Симуляция "мертвого" работника
            WorkerClient worker2 = new WorkerClient("localhost", port, "worker2", 
                "worker2_progress.dat", keystorePath, keystorePassword);
            worker2.start();
            Thread.sleep(1000);
            worker2.close(); 
            
            Thread.sleep(62000); 
            
            
            worker1.close();
        } finally {
            server.close();
            serverThread.interrupt();
        }
    }
}