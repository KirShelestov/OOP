package ru.nsu.shelestov.network;

import ru.nsu.shelestov.task.TaskManager;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class ServerListener implements AutoCloseable {
    private static final Logger logger = Logger.getLogger(ServerListener.class.getName());
    
    private final int port;
    private final TaskManager taskManager;
    private final ExecutorService executorService;
    private volatile boolean running;
    private ServerSocket serverSocket;
    
    public ServerListener(int port, TaskManager taskManager) {
        this.port = port;
        this.taskManager = taskManager;
        this.executorService = Executors.newCachedThreadPool();
        this.running = true;
    }
    
    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            logger.info("Server started on port " + port);
            
            while (running) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket, taskManager);
                executorService.submit(handler);
            }
        } catch (IOException e) {
            if (running) {
                logger.severe("Server error: " + e.getMessage());
            }
        } finally {
            close();
        }
    }
    
    @Override
    public void close() {
        running = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
            executorService.shutdown();
        } catch (IOException e) {
            logger.warning("Error closing server: " + e.getMessage());
        }
    }
}