package ru.nsu.shelestov;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Класс для тестирования правильности операции вычитания.
 */
class SubTest {

    /**
     * тест для проверки правильности форматирования строки.
     */
    @Test
    void testToString() {
        Expression numerator = new Number(5);
        Expression denominator = new Number(3);
        Sub subs = new Sub(numerator, denominator);

        assertEquals("(5.0 - 3.0)", subs.toString());
    }

    /**
     * тест для проверки правильности нахождения производной.
     */
    @Test
    void testDerivative() {
        Expression numerator = new Variable("x");
        Expression denominator = new Number(2);
        Sub subs = new Sub(numerator, denominator);

        Expression derivative = subs.derivative("x");
        assertEquals("(1.0 - 0.0)", derivative.toString());
    }

    /**
     * тест для проверки правильности вывода строки с операцией вычитания.
     */
    @Test
    void testPrint() {
        Expression numerator = new Number(7);
        Expression denominator = new Variable("y");
        Sub subs = new Sub(numerator, denominator);
        subs.print();
        assertEquals("(7.0 - y)", subs.toString());
    }
}

