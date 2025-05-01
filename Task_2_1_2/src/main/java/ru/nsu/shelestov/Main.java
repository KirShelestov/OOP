package ru.nsu.shelestov;

import ru.nsu.shelestov.network.MasterNode;
import ru.nsu.shelestov.network.WorkerNode;
import java.util.Arrays;

public class Main {
    /**
     * Generates test data array with given size.
     * @param size Size of array to generate
     * @return Array of random integers between 1 and 100
     */
    private static int[] generateTestData(int size) {
        int[] data = new int[size];
        for (int i = 0; i < size; i++) {
            data[i] = (int) (Math.random() * 100) + 1;
        }
        return data;
    }

    public static void main(String[] args) {
        MasterNode master = new MasterNode();
        System.out.println("Master node started on port 8080");
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        for (int i = 0; i < 3; i++) {
            final int workerId = i;
            new Thread(() -> {
                WorkerNode worker = new WorkerNode();
                System.out.println("Starting worker " + workerId);
                worker.start();
            }).start();
        }

        int[] data = new int[]{7, 11, 15, 17, 20}; 
        try {
            System.out.println("Processing array: " + Arrays.toString(data));
            long startTime = System.currentTimeMillis();
            
            boolean hasComposite = master.hasComposite(data);
            
            long endTime = System.currentTimeMillis();
            System.out.println("Processing time: " + (endTime - startTime) + "ms");
            System.out.println("Result: array " + (hasComposite ? "contains" : "does not contain") + " composite numbers");
        } catch (Exception e) {
            System.err.println("Error during processing: ");
            e.printStackTrace();
        } finally {
            System.out.println("Shutting down the system...");
            master.shutdown();
        }
    }
}