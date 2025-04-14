package ru.nsu.shelestov.prime;

import java.util.Arrays;

/**
 * Класс для проверки наличия составных чисел в массиве с использованием потоков.
 * Предоставляет метод для параллельной проверки массива целых чисел на наличие составных чисел.
 */
public class StreamPrimeChecker {

    /**
     * Проверяет, содержит ли массив целых чисел составные числа.
     *
     * @param numbers массив целых чисел для проверки
     * @return {@code true}, если в массиве есть хотя бы одно составное число;
     *         {@code false} в противном случае
     */
    public static boolean hasComposite(int[] numbers) {
        return Arrays.stream(numbers)
                .parallel()
                .anyMatch(n -> !PrimeChecker.isPrime(n));
    }

}
