package ru.nsu.shelestov.prime;

/**
 * Класс для проверки простоты чисел.
 * Содержит методы для определения, является ли число простым,
 * а также для проверки массива целых чисел на наличие составных чисел.
 */
public class PrimeChecker {

    /**
     * Проверяет, является ли заданное целое число простым.
     *
     * @param n целое число для проверки
     * @return {@code true}, если число простое; {@code false} в противном случае
     */
    public static boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Проверяет, содержит ли массив целых чисел составные числа.
     *
     * @param numbers массив целых чисел для проверки
     * @return {@code true}, если в массиве есть хотя бы одно составное число;
     *         {@code false} в противном случае
     */
    public static boolean hasComposite(int[] numbers) {
        for (int num : numbers) {
            if (!isPrime(num)) return true;
        }
        return false;
    }
}
