package ru.nsu.shelestov.server;

import ru.nsu.shelestov.task.TaskManager;
import ru.nsu.shelestov.monitoring.HealthCheck;
import ru.nsu.shelestov.monitoring.TaskTimeoutWatcher;
import ru.nsu.shelestov.security.AuthManager;
import ru.nsu.shelestov.security.TLSWrapper;
import ru.nsu.shelestov.shutdown.ShutdownManager;
import ru.nsu.shelestov.task.Task;
import ru.nsu.shelestov.task.TaskResult;
import ru.nsu.shelestov.worker.ProcessResult;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;

public class TaskServer implements AutoCloseable {
    private static final Logger logger = Logger.getLogger(TaskServer.class.getName());
    private final int port;
    private final TaskManager taskManager;
    private final ExecutorService executorService;
    private volatile boolean running;
    private ServerSocket serverSocket;
    private final AuthManager authManager;
    private final TLSWrapper tlsWrapper;
    private final TaskTimeoutWatcher timeoutWatcher;
    private final HealthCheck healthCheck;
    private int[] numbers; 

    public TaskServer(int port, String progressFile, String keystorePath, 
            String keystorePassword) throws Exception {
        this.port = port;
        this.taskManager = new TaskManager(progressFile);
        this.executorService = Executors.newCachedThreadPool();
        this.running = true;
        this.authManager = new AuthManager();
        this.tlsWrapper = new TLSWrapper(keystorePath, keystorePassword);
        this.timeoutWatcher = new TaskTimeoutWatcher(taskManager, 30); 
        this.healthCheck = new HealthCheck(taskManager, 15, 60); 
        
        authManager.registerWorker("worker1", "secret1");
        authManager.registerWorker("worker2", "secret2");
        authManager.registerWorker("worker3", "secret3");
    }

    private void initializeWithArray(int[] numbers) {
        this.numbers = numbers; 
        int workerCount = 3;
        int chunkSize = Math.max(1, numbers.length / workerCount);
        
        logger.info("Initializing tasks with input array of size: " + numbers.length);
        logger.info("Input array: " + Arrays.toString(numbers));
        
        for (int i = 0; i < numbers.length; i += chunkSize) {
            int endIndex = Math.min(i + chunkSize - 1, numbers.length - 1);
            Task task = new Task(numbers, i, endIndex);
            taskManager.addTask(task);
            logger.info(String.format("Created task for array chunk [%d-%d]: %s", 
                i, endIndex, 
                Arrays.toString(Arrays.copyOfRange(numbers, i, endIndex + 1))));
        }
    }

    public void start() {
        try {
            logger.info("Starting server initialization...");
            SSLServerSocket serverSocket = tlsWrapper.createServerSocket(port);
            logger.info("Secure server started on port " + port);

            timeoutWatcher.start();
            healthCheck.start();

            while (running) {
                logger.info("Waiting for client connections...");
                SSLSocket clientSocket = (SSLSocket) serverSocket.accept();
                logger.info("New client connected from: " + 
                    clientSocket.getInetAddress().getHostAddress());
                executorService.submit(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            if (running) {
                logger.severe("Server error: " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            close();
        }
    }

    private void handleClient(SSLSocket socket) {
        String clientId = "unknown";
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush(); 
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            
            clientId = in.readUTF();
            String token = in.readUTF();
            logger.info("Authenticating client: " + clientId);
            
            if (!authManager.authenticateWorker(clientId, token)) {
                out.writeUTF("AUTH_FAILED");
                out.flush();
                logger.warning("Authentication failed for: " + clientId);
                return;
            }
            
            out.writeUTF("AUTH_OK");
            out.flush();
            logger.info("Authentication successful for: " + clientId);
            
            while (running) {
                String command = in.readUTF();
                logger.info("Received command '" + command + "' from " + clientId);
                
                if ("REQUEST_TASK".equals(command)) {
                    Task task = taskManager.getNextTask(clientId);
                    if (task != null) {
                        logger.info("Sending task " + task.getId() + " to " + clientId);
                        out.writeUTF("TASK");
                        out.writeObject(task);
                        out.flush();
                        logger.info("Task " + task.getId() + " sent to " + clientId);
                    } else {
                        logger.info("No tasks available for " + clientId);
                        out.writeUTF("NO_TASK");
                        out.flush();
                    }
                } else if ("SUBMIT_RESULT".equals(command)) {
                    try {
                        ProcessResult result = (ProcessResult) in.readObject();
                        logger.info("Received result from " + clientId);
                        
                        if (!result.getCompositeNumbers().isEmpty()) {
                            int compositeNumber = result.getCompositeNumbers().get(0);
                            logger.info("Composite number found by " + clientId + ": " + compositeNumber);
                            boolean isInArray = false;
                            for (int num : numbers) {  
                                if (num == compositeNumber) {
                                    isInArray = true;
                                    break;
                                }
                            }
                            if (isInArray) {
                                logger.info("Verified: number " + compositeNumber + " is in the input array");
                                running = false;
                                out.writeUTF("SHUTDOWN");
                                out.flush();
                                break;
                            } else {
                                logger.warning("Number " + compositeNumber + " is not in the input array!");
                                out.writeUTF("CONTINUE");
                                out.flush();
                            }
                        }
                        
                        out.writeUTF("RESULT_ACCEPTED");
                        out.flush();
                    } catch (ClassNotFoundException e) {
                        logger.severe("Error reading result from " + clientId + ": " + e.getMessage());
                        out.writeUTF("ERROR");
                        out.flush();
                    }
                }
            }
        } catch (EOFException e) {
            logger.info("Client " + clientId + " disconnected");
        } catch (SocketException e) {
            logger.info("Connection reset by client " + clientId);
        } catch (IOException e) {
            logger.severe("IO error with client " + clientId + ": " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                logger.warning("Error closing client socket: " + e.getMessage());
            }
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
            timeoutWatcher.close();
            healthCheck.close();
        } catch (IOException e) {
            logger.warning("Error closing server: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Usage: <keystorePath> <keystorePassword> <arraySize> <maxValue>");
            System.exit(1);
        }

        String keystorePath = args[0];
        String keystorePassword = args[1];
        int arraySize = Integer.parseInt(args[2]);
        int maxValue = Integer.parseInt(args[3]);

        int[] numbers = new int[arraySize];
        Random random = new Random();
        for (int i = 0; i < arraySize; i++) {
            numbers[i] = random.nextInt(maxValue) + 1;
        }

        System.out.println("Working with array: " + Arrays.toString(numbers));

        try (TaskServer server = new TaskServer(8443, "progress.dat", keystorePath, keystorePassword)) {
            server.initializeWithArray(numbers);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isPortAvailable(int port) {
        try (ServerSocket socket = new ServerSocket(port)) {
            socket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}