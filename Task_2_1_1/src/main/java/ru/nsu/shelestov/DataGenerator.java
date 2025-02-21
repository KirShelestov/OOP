package ru.nsu.shelestov;

import java.util.Arrays;

public class DataGenerator {
    public static int[] generateTestData(int size) {
        int[] data = new int[size];
        Arrays.fill(data, 2);
        data[data.length - 1] = 4;
        return data;
    }
}