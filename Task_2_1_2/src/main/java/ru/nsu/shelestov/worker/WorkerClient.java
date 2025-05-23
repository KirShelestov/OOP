package ru.nsu.shelestov.worker;

import ru.nsu.shelestov.task.Task;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class WorkerClient implements AutoCloseable {
    private static final Logger logger = Logger.getLogger(WorkerClient.class.getName());
    private static final int RETRY_DELAY_MS = 1000;
    private static final int MAX_RETRIES = 3;

    private final String host;
    private final int port;
    private final String workerId;
    private final String progressFile;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private volatile boolean running;

    public WorkerClient(String host, int port, String workerId, String progressFile) {
        this.host = host;
        this.port = port;
        this.workerId = workerId;
        this.progressFile = progressFile;
        this.running = true;
    }

    public void start() {
        try {
            connect();
            while (running) {
                Task task = requestTask();
                if (task == null) {
                    logger.info("No more tasks available, worker stopping");
                    break;
                }
                
                ProcessResult result = processTask(task);
                sendResult(result);
                saveProgress(task);
            }
        } catch (Exception e) {
            logger.severe("Worker failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private void connect() throws IOException {
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        out.writeUTF(workerId);
        out.flush();
    }

    private Task requestTask() throws IOException, ClassNotFoundException {
        out.writeUTF("REQUEST_TASK");
        out.flush();
        return (Task) in.readObject();
    }

    private ProcessResult processTask(Task task) {
        List<Integer> compositeNumbers = new ArrayList<>();
        for (int i = task.getStartIndex(); i < task.getEndIndex(); i++) {
            int number = task.getNumbers()[i];
            if (!isPrime(number)) {
                compositeNumbers.add(number);
            }
        }
        return new ProcessResult(task.getId(), compositeNumbers);
    }

    private void sendResult(ProcessResult result) throws IOException {
        out.writeUTF("SUBMIT_RESULT");
        out.writeObject(result);
        out.flush();
    }

    private void saveProgress(Task task) throws IOException {
        try (ObjectOutputStream progressOut = new ObjectOutputStream(
                new FileOutputStream(progressFile))) {
            progressOut.writeObject(task);
            progressOut.writeInt(task.getEndIndex());
        }
    }

    private Task loadProgress() throws IOException, ClassNotFoundException {
        File file = new File(progressFile);
        if (!file.exists() || file.length() == 0) {
            return null;
        }
        try (ObjectInputStream progressIn = new ObjectInputStream(
                new FileInputStream(progressFile))) {
            return (Task) progressIn.readObject();
        }
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
            logger.warning("Error closing worker: " + e.getMessage());
        }
    }
}