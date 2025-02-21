package ru.nsu.shelestov;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.ExecutionException;

public class ParallelPrimeCheckerTest {

    @Test
    public void testHasComposite() throws ExecutionException, InterruptedException {
        int[] primes = {2, 3, 5, 7};
        int[] mixed = {2, 3, 4, 5};
        int[] composites = {4, 6, 8};

        assertFalse(ParallelPrimeChecker.hasComposite(primes, 4));
        assertTrue(ParallelPrimeChecker.hasComposite(mixed, 4));
        assertTrue(ParallelPrimeChecker.hasComposite(composites, 4));
    }
}
