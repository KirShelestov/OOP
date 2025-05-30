package ru.nsu.shelestov.worker;

import ru.nsu.shelestov.security.TLSWrapper;
import ru.nsu.shelestov.task.Task;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocket;

public class WorkerClient implements AutoCloseable {
    private static final Logger logger = Logger.getLogger(WorkerClient.class.getName());
    private final String host;
    private final int port;
    private final String workerId;
    private final String token;
    private final String progressFile;
    private final TLSWrapper tlsWrapper;
    private SSLSocket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY_MS = 2000;
    private volatile boolean running = true;

    public WorkerClient(String host, int port, String workerId, String progressFile,
            String keystorePath, String keystorePassword) throws Exception {
        this.host = host;
        this.port = port;
        this.workerId = workerId;
        this.progressFile = progressFile;
        this.token = "secret" + workerId.charAt(workerId.length() - 1); // Match server's token format
        this.tlsWrapper = new TLSWrapper(keystorePath, keystorePassword);
    }

    private void connect() throws IOException {
        socket = tlsWrapper.createClientSocket(host, port);
        
        // First create output stream
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush(); // Important: flush header
        
        // Then create input stream
        in = new ObjectInputStream(socket.getInputStream());
        
        // Authenticate
        out.writeUTF(workerId);
        out.writeUTF(token);
        out.flush();
        
        String response = in.readUTF();
        if (!"AUTH_OK".equals(response)) {
            throw new IOException("Authentication failed");
        }
        
        logger.info("Connected and authenticated as " + workerId);
    }

    private void handleTask(ObjectInputStream in, ObjectOutputStream out) throws Exception {
        try {
            out.writeUTF("REQUEST_TASK");
            out.flush();
            logger.info(workerId + ": Requested task");
            
            String responseType = in.readUTF();
            logger.info(workerId + ": Received response type: " + responseType);
            
            switch (responseType) {
                case "TASK":
                    Object obj = in.readObject();
                    if (!(obj instanceof Task)) {
                        throw new ClassNotFoundException("Expected Task object but got: " + obj.getClass());
                    }
                    Task task = (Task) obj;
                    
                    logger.info(String.format("%s: Processing task %s (range: %d-%d)", 
                        workerId, 
                        task.getId(),
                        task.getNumbers()[task.getStartIndex()],
                        task.getNumbers()[task.getEndIndex()]));
                    
                    ProcessResult result = processTask(task);
                    
                    out.writeUTF("SUBMIT_RESULT");
                    out.writeObject(result);
                    out.flush();
                    logger.info(workerId + ": Result submitted");
                    break;
                    
                case "NO_TASK":
                    logger.info(workerId + ": No tasks available, waiting...");
                    Thread.sleep(1000); 
                    break;
                    
                case "SHUTDOWN":
                    logger.info(workerId + ": Received shutdown command");
                    running = false;
                    break;
                    
                default:
                    logger.warning(workerId + ": Unexpected response type: " + responseType);
                    break;
            }
        } catch (Exception e) {
            logger.severe(workerId + ": Error in handleTask: " + e.toString());
            throw e;
        }
    }

    public void start() {
        int retries = 0;
        
        while (running && retries < MAX_RETRIES) {
            try (SSLSocket socket = tlsWrapper.createClientSocket(host, port)) {
                socket.setSoTimeout(30000);
                logger.info(String.format("%s: Connected to %s:%d (attempt %d/%d)", 
                    workerId, host, port, retries + 1, MAX_RETRIES));
                
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.flush(); 
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                
                out.writeUTF(workerId);
                out.writeUTF(token);
                out.flush();
                
                String response = in.readUTF();
                if (!"AUTH_OK".equals(response)) {
                    throw new SecurityException("Authentication failed: " + response);
                }
                logger.info(String.format("%s: Authentication successful", workerId));
                
                retries = 0;
                
                while (running && !socket.isClosed()) {
                    try {
                        handleTask(in, out);
                    } catch (IOException e) {
                        logger.warning(String.format("%s: Communication error: %s", 
                            workerId, e.getMessage()));
                        break; 
                    }
                }
            } catch (Exception e) {
                retries++;
                if (retries >= MAX_RETRIES) {
                    logger.severe(String.format("%s: Failed after %d attempts: %s", 
                        workerId, retries, e.getMessage()));
                    break;
                }
                
                logger.warning(String.format("%s: Connection failed, retrying in %d ms... (%s)", 
                    workerId, RETRY_DELAY_MS, e.toString()));
                    
                try {
                    Thread.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    running = false;
                    break;
                }
            }
        }
    }
    
    private ProcessResult processTask(Task task) {
        logger.info(String.format("%s: Starting task processing. Range [%d, %d]", 
            workerId, 
            task.getNumbers()[task.getStartIndex()],
            task.getNumbers()[task.getEndIndex()]));
        
        long startTime = System.currentTimeMillis();
        int numbersChecked = 0;
        List<Integer> compositeNumbers = new ArrayList<>();
        
        for (int i = task.getStartIndex(); i <= task.getEndIndex(); i++) {
            int number = task.getNumbers()[i];
            if (!isPrime(number)) {
                compositeNumbers.add(number);
                if (compositeNumbers.size() == 1) {
                    logger.info(String.format("%s: Found first composite number: %d", 
                        workerId, number));
                }
            }
            
            numbersChecked++;
            if (numbersChecked % 100000 == 0) { 
                long currentTime = System.currentTimeMillis();
                long timeElapsed = (currentTime - startTime) / 1000;
                logger.info(String.format("%s: Processed %d numbers in %d seconds", 
                    workerId, numbersChecked, timeElapsed));
            }
        }
        
        long totalTime = (System.currentTimeMillis() - startTime) / 1000;
        logger.info(String.format("%s: Task completed in %d seconds. Found %d composite numbers", 
            workerId, totalTime, compositeNumbers.size()));
        
        return new ProcessResult(task.getId(), compositeNumbers);
    }

    private boolean isPrime(int number) {
        if (number <= 1) return false;
        if (number <= 3) return true;
        if (number % 2 == 0) return false;
        
        int sqrt = (int) Math.sqrt(number);
        for (int i = 3; i <= sqrt; i += 2) {
            if (number % i == 0) return false;
        }
        return true;
    }

    @Override
    public void close() {
        running = false;
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            logger.warning(String.format("%s: Error closing worker: %s", 
                workerId, e.getMessage()));
        }
    }
}