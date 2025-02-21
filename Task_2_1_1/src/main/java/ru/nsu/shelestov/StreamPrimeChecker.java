package ru.nsu.shelestov;

import java.util.Arrays;

public class StreamPrimeChecker {
    public static boolean hasComposite(int[] numbers) {
        return Arrays.stream(numbers)
                .parallel()
                .anyMatch(n -> !PrimeChecker.isPrime(n));
    }
}