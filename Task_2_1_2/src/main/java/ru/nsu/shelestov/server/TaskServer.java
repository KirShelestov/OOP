package ru.nsu.shelestov.server;

import ru.nsu.shelestov.task.TaskManager;
import ru.nsu.shelestov.task.Task;
import ru.nsu.shelestov.task.TaskResult;
import ru.nsu.shelestov.worker.ProcessResult;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class TaskServer implements AutoCloseable {
    private static final Logger logger = Logger.getLogger(TaskServer.class.getName());
    private final int port;
    private final TaskManager taskManager;
    private final ExecutorService executorService;
    private volatile boolean running;
    private ServerSocket serverSocket;

    public TaskServer(int port, String progressFile) {
        this.port = port;
        this.taskManager = new TaskManager(progressFile);
        this.executorService = Executors.newCachedThreadPool();
        this.running = true;
        
        initializeTasks();
    }

    private void initializeTasks() {
        int start = 1;
        int range = 10000;
        for (int i = 0; i < 10; i++) {
            int[] numbers = new int[range];
            for (int j = 0; j < range; j++) {
                numbers[j] = start + j;
            }
            Task task = new Task(numbers, 0, range - 1);
            taskManager.addTask(task);
            start += range;
        }
        logger.info("Initialized server with 10 tasks");
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            logger.info("Server started on port " + port);

            while (running) {
                Socket clientSocket = serverSocket.accept();
                executorService.submit(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            if (running) {
                logger.severe("Server error: " + e.getMessage());
            }
        } finally {
            close();
        }
    }

    private void handleClient(Socket socket) {
        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            
            String workerId = in.readUTF();
            logger.info("Worker connected: " + workerId);

            while (running && !socket.isClosed()) {
                String command = in.readUTF();
                switch (command) {
                    case "REQUEST_TASK":
                        Task task = taskManager.getNextTask(workerId);
                        out.writeObject(task);
                        out.flush();
                        break;
                    case "SUBMIT_RESULT":
                        ProcessResult processResult = (ProcessResult) in.readObject();
                        TaskResult taskResult = new TaskResult(
                            processResult.getTaskId(),
                            true, 
                            processResult.getCompositeNumbers(),
                            workerId,
                            System.currentTimeMillis()
                        );
                        taskManager.processResult(taskResult);
                        break;
                    default:
                        logger.warning("Unknown command: " + command);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            logger.warning("Client connection error: " + e.getMessage());
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

    public static void main(String[] args) {
        int port = 8080;
        String progressFile = "server_progress.dat";

        try (TaskServer server = new TaskServer(port, progressFile)) {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}