package ru.nsu.shelestov;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Класс для тестирования класса Number.
 */
class NumberTest {

    /**
     * тест для проверки форматирования строки.
     */
    @Test
    void testToString() {
        Expression num = new Number(5);
        assertEquals("5.0", num.toString());
    }

    /**
     * тест для проверки правильности нахождения производной.
     */
    @Test
    void testDerivative() {
        Expression num = new Number(2);
        Expression derivative = num.derivative("x");
        assertEquals("0.0", derivative.toString());
    }

    /**
     * тест для проверки правильности вывода строки с одним числом.
     */
    @Test
    void testPrint() {
        Expression num = new Number(7);
        num.print();
        assertEquals("7.0", num.toString());
    }
}

