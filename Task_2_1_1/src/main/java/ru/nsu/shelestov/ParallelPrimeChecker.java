package ru.nsu.shelestov;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ParallelPrimeChecker {
    private static final ExecutorService pool = Executors.newWorkStealingPool();

    public static boolean hasComposite(int[] numbers, int parallelism) throws ExecutionException, InterruptedException {
        final int chunkSize = Math.max(numbers.length / (parallelism * 4), 100);

        List<Future<Boolean>> futures = new ArrayList<>();

        for (int i = 0; i < numbers.length; i += chunkSize) {
            final int start = i;
            final int end = Math.min(i + chunkSize, numbers.length);
            futures.add(pool.submit(() -> {
                for (int j = start; j < end; j++) {
                    if (!PrimeChecker.isPrime(numbers[j])) return true;
                }
                return false;
            }));
        }

        for (Future<Boolean> future : futures) {
            if (future.get()) return true;
        }
        return false;
    }
}