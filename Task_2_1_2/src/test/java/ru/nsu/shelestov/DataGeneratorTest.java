package ru.nsu.shelestov;

import org.junit.jupiter.api.Test;
import ru.nsu.shelestov.generator.DataGenerator;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class DataGeneratorTest {

    @Test
    public void testGenerateTestData() {
        int size = 10;
        int[] expected = new int[size];
        Arrays.fill(expected, 2);
        expected[size - 1] = 4;

        int[] result = DataGenerator.generateTestData(size);
        assertArrayEquals(expected, result);
    }
}
