package ru.nsu.shelestov;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PrimeCheckerTest {

    @Test
    public void testIsPrime() {
        assertTrue(PrimeChecker.isPrime(2));
        assertTrue(PrimeChecker.isPrime(3));
        assertFalse(PrimeChecker.isPrime(4));
        assertTrue(PrimeChecker.isPrime(5));
        assertFalse(PrimeChecker.isPrime(9));
        assertFalse(PrimeChecker.isPrime(1));
        assertFalse(PrimeChecker.isPrime(-5));
    }

    @Test
    public void testHasComposite() {
        int[] primes = {2, 3, 5, 7};
        int[] mixed = {2, 3, 4, 5};
        int[] composites = {4, 6, 8};

        assertFalse(PrimeChecker.hasComposite(primes));
        assertTrue(PrimeChecker.hasComposite(mixed));
        assertTrue(PrimeChecker.hasComposite(composites));
    }
}
