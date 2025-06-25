package ru.nsu.shelestov.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PrimeGenerator {
    private static final Random random = new Random();
    
    public static int[] generateMixedArray(int size, int maxValue) {
        int[] array = new int[size];
        array[0] = generateCompositeNumber(maxValue);
        
        for (int i = 1; i < size; i++) {
            array[i] = random.nextBoolean() ? 
                generatePrimeNumber(maxValue) : 
                generateCompositeNumber(maxValue);
        }
        return array;
    }

    public static int[] generatePrimeArray(int size, int maxValue) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = generatePrimeNumber(maxValue);
        }
        return array;
    }

    private static int generatePrimeNumber(int maxValue) {
        while (true) {
            int candidate = random.nextInt(maxValue - 2) + 2;
            if (isPrime(candidate)) {
                return candidate;
            }
        }
    }

    private static int generateCompositeNumber(int maxValue) {
        while (true) {
            int candidate = random.nextInt(maxValue - 4) + 4;
            if (!isPrime(candidate)) {
                return candidate;
            }
        }
    }

    public static boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;

        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }
}