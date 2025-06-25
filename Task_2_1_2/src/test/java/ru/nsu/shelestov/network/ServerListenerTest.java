package ru.nsu.shelestov.network;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.nsu.shelestov.task.TaskManager;

import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServerListenerTest {
    @Mock
    private TaskManager taskManager;

    @Test
    void testServerStartAndStop() throws Exception {
        int port = 8090;
        ServerListener server = new ServerListener(port, taskManager);
        
        Thread serverThread = new Thread(server::start);
        serverThread.start();
        
        TimeUnit.MILLISECONDS.sleep(100);
        
        try (Socket testClient = new Socket("localhost", port)) {
            assertTrue(testClient.isConnected());
        }
        
        server.close();
        serverThread.join(1000);
        assertFalse(serverThread.isAlive());
    }
}