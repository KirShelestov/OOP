package ru.nsu.shelestov.worker;

public class WorkerMain {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 8443; 
        String initialKeystorePath = "server.jks";
        String initialKeystorePassword = "serverpass";
        
        if (args.length >= 2) {
            initialKeystorePath = args[0];
            initialKeystorePassword = args[1];
        }
        
        final String keystorePath = initialKeystorePath;
        final String keystorePassword = initialKeystorePassword;
        String[] workerIds = {"worker1", "worker2", "worker3"};
        
        for (String workerId : workerIds) {
            new Thread(() -> {
                String progressFile = workerId + "_progress.dat";
                try (WorkerClient worker = new WorkerClient(host, port, workerId, 
                        progressFile, keystorePath, keystorePassword)) {
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