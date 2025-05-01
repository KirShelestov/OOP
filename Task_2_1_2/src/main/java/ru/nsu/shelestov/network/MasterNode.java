package ru.nsu.shelestov.network;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class MasterNode {
    private static final int PORT = 8080;
    private final int TIMEOUT_MS = 5000;
    private final Map<Socket, WorkerHandler> workers = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private ServerSocket serverSocket;

    public MasterNode() {
        try {
            serverSocket = new ServerSocket(PORT);
            startAcceptingWorkers();
            System.out.println("Master node is ready to accept connections");
        } catch (IOException e) {
            throw new RuntimeException("Failed to start master node", e);
        }
    }
    
    public boolean hasComposite(int[] data) throws Exception {
        if (serverSocket == null) {
            serverSocket = new ServerSocket(PORT);
            startAcceptingWorkers();
        }
        
        long startTime = System.currentTimeMillis();
        while (workers.isEmpty()) {
            if (System.currentTimeMillis() - startTime > TIMEOUT_MS) {
                throw new TimeoutException("No workers connected");
            }
            Thread.sleep(100);
        }
        
        int workersCount = workers.size();
        int chunkSize = data.length / workersCount;
        CompletableFuture<Boolean>[] futures = new CompletableFuture[workersCount];
        
        int i = 0;
        int start = 0;
        for (WorkerHandler worker : workers.values()) {
            int end = (i == workersCount - 1) ? data.length : start + chunkSize;
            futures[i] = worker.processChunk(data, start, end);
            start = end;
            i++;
        }
        
        try {
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures);
            allFutures.get(TIMEOUT_MS, TimeUnit.MILLISECONDS);
            
            for (CompletableFuture<Boolean> future : futures) {
                try {
                    if (future.get()) {  
                        System.out.println("Found composite number in chunk");
                        return true;
                    }
                } catch (ExecutionException e) {
                    System.err.println("Error getting result from worker: " + e.getCause());
                    handleWorkerFailure();
                }
            }
            
            return false;
        } catch (TimeoutException e) {
            System.err.println("Timeout waiting for workers");
            handleWorkerFailure();
            throw new RuntimeException("Worker timeout", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Processing interrupted", e);
        }
    }
    
    private void startAcceptingWorkers() {
        executorService.submit(() -> {
            while (!serverSocket.isClosed()) {
                try {
                    Socket workerSocket = serverSocket.accept();
                    WorkerHandler handler = new WorkerHandler(workerSocket);
                    workers.put(workerSocket, handler);
                    System.out.println("New worker connected. Total workers: " + workers.size());
                } catch (Exception e) {
                    if (!serverSocket.isClosed()) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    
    private void handleWorkerFailure() {
        workers.entrySet().removeIf(entry -> {
            Socket socket = entry.getKey();
            try {
                socket.getOutputStream().write(1); 
                return false; 
            } catch (Exception e) {
                try {
                    socket.close();
                } catch (Exception ignored) {}
                return true; 
            }
        });
    }
    
    public void shutdown() {
        workers.values().forEach(worker -> worker.sendMessage(new Message(Message.Type.SHUTDOWN, false)));
        workers.clear();
        executorService.shutdown();
        try {
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}