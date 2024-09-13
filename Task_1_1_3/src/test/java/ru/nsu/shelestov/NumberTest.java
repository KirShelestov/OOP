package ru.nsu.shelestov;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Класс для тестирования класса Number.
 */
class NumberTest {

    /**
     * тест для проверки форматирования строки.
     */
    @Test
    void testToString() {
        Expression numerator = new Number(5);

        assertEquals("5.0", numerator.toString());
    }

    /**
     * тест для проверки правильности нахождения производной.
     */
    @Test
    void testDerivative() {
        Expression denominator = new Number(2);


        Expression derivative = denominator.derivative("x");
        assertEquals("0.0", derivative.toString());
    }

    /**
     * тест для проверки правильности вывода строки с одним числом.
     */
    @Test
    void testPrint() {
        Expression numerator = new Number(7);
        numerator.print();
        assertEquals("7.0", numerator.toString());
    }


}

