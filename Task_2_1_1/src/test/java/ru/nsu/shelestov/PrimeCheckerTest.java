package ru.nsu.shelestov;

import org.junit.jupiter.api.Test;
import ru.nsu.shelestov.prime.PrimeChecker;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.stream.IntStream;

public class PrimeCheckerTest {

    @Test
    public void testLargeArray() {
        int[] largePrimes = IntStream.range(2, 1000000)
                .filter(PrimeChecker::isPrime)
                .toArray();

        assertFalse(PrimeChecker.hasComposite(largePrimes), "Массив должен содержать только простые числа");

        int[] largeComposites = IntStream.range(4, 1000000)
                .filter(n -> !PrimeChecker.isPrime(n))
                .toArray();

        assertTrue(PrimeChecker.hasComposite(largeComposites), "Массив должен содержать составные числа");

        int[] mixedArray = new int[1000000];
        for (int i = 0; i < mixedArray.length; i++) {
            mixedArray[i] = (i % 2 == 0) ? (i + 2) : (i + 4); // Чередуем простые и составные числа
        }

        assertTrue(PrimeChecker.hasComposite(mixedArray), "Массив должен содержать составные числа");
    }
}
