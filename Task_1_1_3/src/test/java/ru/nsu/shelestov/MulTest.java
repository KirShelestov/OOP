package ru.nsu.shelestov;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Класс для тестирования правильности операции умножения.
 */
class MulTest {

    /**
     * тест для проверки правильности форматирования строки.
     */
    @Test
    void testToString() {
        Expression numerator = new Number(5);
        Expression denominator = new Number(3);
        Mul mult = new Mul(numerator, denominator);

        assertEquals("(5.0 * 3.0)", mult.toString());
    }

    /**
     * тест для проверки правильности нахождения производной.
     */
    @Test
    void testDerivative() {
        Expression numerator = new Variable("x");
        Expression denominator = new Number(2);
        Mul mult = new Mul(numerator, denominator);

        Expression derivative = mult.derivative("x");
        assertEquals("((1.0 * 2.0) + (x * 0.0))", derivative.toString());
    }

    /**
     * тест для проверки правильности вывода строки с операцией умножения.
     */
    @Test
    void testPrint() {
        Expression numerator = new Number(7);
        Expression denominator = new Variable("y");
        Mul mult = new Mul(numerator, denominator);
        mult.print();
        assertEquals("(7.0 * y)", mult.toString());
    }
}

