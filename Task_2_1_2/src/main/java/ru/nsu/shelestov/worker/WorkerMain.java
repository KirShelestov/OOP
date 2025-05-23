package ru.nsu.shelestov.worker;

public class WorkerMain {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 8080;
        
        String[] workerIds = {"worker1", "worker2", "worker3"};
        
        for (String workerId : workerIds) {
            new Thread(() -> {
                String progressFile = workerId + "_progress.dat";
                try (WorkerClient worker = new WorkerClient(host, port, workerId, progressFile)) {
                    worker.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}