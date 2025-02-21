package ru.nsu.shelestov.prime;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Класс для параллельной проверки наличия составных чисел в массиве.
 * Использует пул потоков для распределения задач по проверке чисел на составность.
 */
public class ParallelPrimeChecker {
    private static final ExecutorService pool = Executors.newWorkStealingPool();

    /**
     * Проверяет, содержит ли массив целых чисел составные числа.
     *
     * @param numbers массив целых чисел для проверки
     * @param parallelism количество параллельных задач (число потоков)
     * @return {@code true}, если в массиве есть хотя бы одно составное число;
     *         {@code false} в противном случае
     * @throws ExecutionException если возникла ошибка при выполнении задачи
     * @throws InterruptedException если текущий поток был прерван во время ожидания результата
     */
    public static boolean hasComposite(int[] numbers, int parallelism) throws ExecutionException, InterruptedException {
        final int chunkSize = Math.max(numbers.length / parallelism, 1);

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
