package ru.nsu.shelestov.generator;

import java.util.Arrays;

/**
 * Класс для генерации тестовых данных.
 * Используется в бенчмарке.
 */
public class DataGenerator {

    /**
     * Генерирует массив тестовых данных заданного размера.
     * Все элементы массива инициализируются значением 2,
     * за исключением последнего элемента, который устанавливается в 4.
     *
     * @param size размер массива, который нужно сгенерировать
     * @return массив целых чисел, заполненный значениями 2,
     *         за исключением последнего элемента, который равен 4
     * @throws IllegalArgumentException если размер массива меньше 1
     */
    public static int[] generateTestData(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Размер массива должен быть больше или равен 1.");
        }

        int[] data = new int[size];
        Arrays.fill(data, 2);
        data[data.length - 1] = 4;
        return data;
    }
}
