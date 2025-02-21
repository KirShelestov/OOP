package ru.nsu.shelestov;

import org.junit.jupiter.api.Test;
import ru.nsu.shelestov.prime.ParallelPrimeChecker;

import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.ExecutionException;

public class ParallelPrimeCheckerTest {

    @Test
    public void testHasCompositeWithOnlyPrimes() throws ExecutionException, InterruptedException {
        int[] primes = {2, 3, 5, 7, 11, 13, 17, 19};
        assertFalse(ParallelPrimeChecker.hasComposite(primes, 4));
        assertFalse(ParallelPrimeChecker.hasComposite(primes, 10));
    }

    @Test
    public void testHasCompositeWithMixedArray() throws ExecutionException, InterruptedException {
        int[] mixed = {2, 3, 4, 5, 6, 7};
        assertTrue(ParallelPrimeChecker.hasComposite(mixed, 4));
        assertTrue(ParallelPrimeChecker.hasComposite(mixed, 6));
    }

    @Test
    public void testHasCompositeWithOnlyComposites() throws ExecutionException, InterruptedException {
        int[] composites = {4, 6, 8, 9, 10};
        assertTrue(ParallelPrimeChecker.hasComposite(composites, 4));
        assertTrue(ParallelPrimeChecker.hasComposite(composites, 10));
    }

    @Test
    public void testHasCompositeWithEmptyArray() throws ExecutionException, InterruptedException {
        int[] empty = {};
        assertFalse(ParallelPrimeChecker.hasComposite(empty, 4));
    }

    @Test
    public void testHasCompositeWithSinglePrime() throws ExecutionException, InterruptedException {
        int[] singlePrime = {5};
        assertFalse(ParallelPrimeChecker.hasComposite(singlePrime, 4));
    }

    @Test
    public void testHasCompositeWithSingleComposite() throws ExecutionException, InterruptedException {
        int[] singleComposite = {4};
        assertTrue(ParallelPrimeChecker.hasComposite(singleComposite, 4));
    }

    @Test
    public void testHasCompositeWithNonExistentComposite() throws ExecutionException, InterruptedException {
        int[] primes = {2, 3, 5, 7, 11};
        assertFalse(ParallelPrimeChecker.hasComposite(primes, 9));
    }

    @Test
    public void testHasCompositeWithMultipleInstances() throws ExecutionException, InterruptedException {
        int[] mixed = {2, 3, 4, 5, 6, 4, 7, 4};
        assertTrue(ParallelPrimeChecker.hasComposite(mixed, 4));
    }

    @Test
    public void testHasCompositeWithLargeNumbers() throws ExecutionException, InterruptedException {
        int[] largeNumbers = {1000003, 1000004, 1000005};
        assertTrue(ParallelPrimeChecker.hasComposite(largeNumbers, 4)); // Используем 4 потока
    }
}
