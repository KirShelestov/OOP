package ru.nsu.shelestov;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

/**
 * Тесты для хипсорта
 */
public class HeapsortTest {

    /**
     * Тест на массив с положительными значениями.
     *
     */
    @Test
    public void testSort() {
        Heapsort sorter = new Heapsort();
        int[] input = {12, 11, 13, 5, 6, 7};
        int[] expected = {5, 6, 7, 11, 12, 13};

        sorter.sort(input);
        assertArrayEquals(expected, input);
    }

    /**
     * Тест на массив с повторяющимеся значениями.
     *
     */
    @Test
    public void testSortWithDuplicateElements() {
        Heapsort heapsort = new Heapsort();
        int[] input = {3, 1, 2, 3, 1};
        int[] expected = {1, 1, 2, 3, 3};
        heapsort.sort(input);
        assertArrayEquals(expected, input);
    }

    /**
     * Тест на пустой массив.
     *
     */
    @Test
    public void testSortEmptyArray() {
        Heapsort sorter = new Heapsort();
        int[] input = {};
        int[] expected = {};

        sorter.sort(input);
        assertArrayEquals(expected, input);
    }

    /**
     * Тест на массив с одним элементом.
     *
     */
    @Test
    public void testSortSingleElement() {
        Heapsort sorter = new Heapsort();
        int[] input = {42};
        int[] expected = {42};

        sorter.sort(input);
        assertArrayEquals(expected, input);
    }

    /**
     * Тест на отсортированный массив.
     *
     */
    @Test
    public void testSortAlreadySortedArray() {
        Heapsort sorter = new Heapsort();
        int[] input = {1, 2, 3, 4, 5};
        int[] expected = {1, 2, 3, 4, 5};

        sorter.sort(input);
        assertArrayEquals(expected, input);
    }

    /**
     * Тест на массив с значениями, идущими по убыванию.
     *
     */
    @Test
    public void testSortReversedArray() {
        Heapsort sorter = new Heapsort();
        int[] input = {5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5};

        sorter.sort(input);
        assertArrayEquals(expected, input);
    }

    /**
     * Тест на массив с отрицательными значениями.
     *
     */
    @Test
    public void testSortArrayWithNegativeValues() {
        Heapsort sorter = new Heapsort();
        int[] input = {2, -3, 1, 0, -1};
        int[] expected = {-3, -1, 0, 1, 2};

        sorter.sort(input);
        assertArrayEquals(expected, input);
    }

    /**
     * Тест на массив с рандомными значениями.
     *
     */
    @Test
    public void testRandomArray() {
        Heapsort sorter = new Heapsort();
        int[] input = new int[10000];
        for (int i = 0; i < input.length; i++) {
            input[i] = (int) (Math.random() * 100000);
        }
        int[] expected = input.clone();
        java.util.Arrays.sort(expected);
        sorter.sort(input);
        assertArrayEquals(expected, input);
    }

    /**
     * Проверяет, что метод heapify правильно работает с различными индексами.
     *
     */
    @Test
    public void testHeapify() {
        Heapsort heapsort = new Heapsort();
        int[] input = {3, 1, 2};
        heapsort.heapify(input, input.length, 0);
        assertArrayEquals(new int[]{3, 1, 2}, input);
        heapsort.heapify(input, input.length, 1);
        assertArrayEquals(new int[]{3, 1, 2}, input);
        heapsort.heapify(input, input.length, 2);
        assertArrayEquals(new int[]{3, 1, 2}, input);
    }

    /**
     * Тест на крайние значения.
     *
     */
    @Test
    public void testSortWithLargeNumbers() {
        Heapsort heapsort = new Heapsort();
        int[] input = {Integer.MAX_VALUE, Integer.MIN_VALUE, -1, 0};
        int[] expected = {Integer.MIN_VALUE, -1, 0, Integer.MAX_VALUE};
        heapsort.sort(input);
        assertArrayEquals(expected, input);
    }

    /**
     * Тест на вывод отсортированного массива.
     * не можем фактически проверить правильность метода, поэтому просто вызываем его.
     *
     */
    @Test
    public void testPrintArray() {
        Heapsort.printArray(new int[]{1, 2, 3});
    }

}