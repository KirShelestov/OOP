package ru.nsu.shelestov;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StreamPrimeCheckerTest {

    @Test
    public void testHasComposite() {
        int[] primes = {2, 3, 5, 7};
        int[] mixed = {2, 3, 4, 5};
        int[] composites = {4, 6, 8};

        assertFalse(StreamPrimeChecker.hasComposite(primes));
        assertTrue(StreamPrimeChecker.hasComposite(mixed));
        assertTrue(StreamPrimeChecker.hasComposite(composites));
    }
}
