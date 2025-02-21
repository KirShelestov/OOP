package ru.nsu.shelestov;

import org.junit.jupiter.api.Test;
import ru.nsu.shelestov.prime.StreamPrimeChecker;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.IntStream;

public class StreamPrimeCheckerTest {

    @Test
    public void testHasComposite() {
        int[] primes = {2, 3, 5, 7};
        int[] mixed = {2, 3, 4, 5};
        int[] composites = {4, 6, 8};

        assertFalse(StreamPrimeChecker.hasComposite(primes), "Массив должен содержать только простые числа");
        assertTrue(StreamPrimeChecker.hasComposite(mixed), "Массив должен содержать составные числа");
        assertTrue(StreamPrimeChecker.hasComposite(composites), "Массив должен содержать составные числа");
    }

    @Test
    public void testLargeArray() {
        int[] largePrimes = IntStream.range(2, 1000000)
                .filter(n -> !StreamPrimeChecker.hasComposite(new int[]{n}))
                .toArray();

        assertFalse(StreamPrimeChecker.hasComposite(largePrimes), "Массив должен содержать только простые числа");

        int[] largeComposites = IntStream.range(4, 1000000)
                .filter(n -> StreamPrimeChecker.hasComposite(new int[]{n}))
                .toArray();

        assertTrue(StreamPrimeChecker.hasComposite(largeComposites), "Массив должен содержать составные числа");

        int[] mixedArray = new int[1000000];
        for (int i = 0; i < mixedArray.length; i++) {
            mixedArray[i] = (i % 2 == 0) ? (i + 2) : (i + 4);
        }

        assertTrue(StreamPrimeChecker.hasComposite(mixedArray), "Массив должен содержать составные числа");
    }
}
