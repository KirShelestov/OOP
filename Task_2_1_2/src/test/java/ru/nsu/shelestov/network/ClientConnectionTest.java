package ru.nsu.shelestov.network;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.shelestov.task.Task;
import ru.nsu.shelestov.task.TaskResult;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class ClientConnectionTest {
    private ServerSocket serverSocket;
    private ClientConnection client;
    private ExecutorService executor;
    private static final int TEST_PORT = 8095;

    @BeforeEach
    void setUp() throws IOException {
        serverSocket = new ServerSocket(TEST_PORT);
        executor = Executors.newSingleThreadExecutor();
        client = new ClientConnection("localhost", TEST_PORT, "testWorker");
    }

    @AfterEach
    void tearDown() throws IOException {
        client.close();
        serverSocket.close();
        executor.shutdown();
    }

    @Test
    void testConnect() throws IOException {
        executor.submit(() -> {
            try {
                Socket clientSocket = serverSocket.accept();
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                out.flush();
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                assertTrue(clientSocket.isConnected());
                clientSocket.close();
            } catch (IOException e) {
                fail("Server socket error: " + e.getMessage());
            }
        });

        client.connect();
    }

    @Test
    void testRequestTask() throws IOException, ClassNotFoundException {
        executor.submit(() -> {
            try (Socket clientSocket = serverSocket.accept();
                 ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {
                
                MessageProtocol request = (MessageProtocol) in.readObject();
                assertEquals(MessageProtocol.MessageType.REQUEST_TASK, request.getType());
                
                Task mockTask = new Task(new int[]{1, 2, 3}, 0, 2);
                out.writeObject(new MessageProtocol(
                    MessageProtocol.MessageType.TASK_RESPONSE,
                    "testWorker",
                    mockTask
                ));
                out.flush();
            } catch (Exception e) {
                fail("Server error: " + e.getMessage());
            }
        });

        client.connect();
        Task task = client.requestTask();
        assertNotNull(task);
    }
}