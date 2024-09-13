package ru.nsu.shelestov;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Класс для тестирования правильности операции умножения.
 */
public class MulTest {

    /**
     * тест для проверки правильности форматирования строки.
     */
    @Test
    void testToString() {
        Expression factorFirst = new Number(5);
        Expression factorSecond = new Number(3);
        Mul mult = new Mul(factorFirst, factorSecond);

        assertEquals("(5.0 * 3.0)", mult.toString());
    }

    /**
     * тест для проверки правильности нахождения производной.
     */
    @Test
    void testDerivative() {
        Expression factorFirst = new Variable("x");
        Expression factorSecond = new Number(2);
        Mul mult = new Mul(factorFirst, factorSecond);

        Expression derivative = mult.derivative("x");
        assertEquals("((1.0 * 2.0) + (x * 0.0))", derivative.toString());
    }

    /**
     * тест для проверки правильности вывода строки с операцией умножения.
     */
    @Test
    void testPrint() {
        Expression factorFirst = new Number(7);
        Expression factorSecond = new Variable("y");
        Mul mult = new Mul(factorFirst, factorSecond);
        mult.print();
        assertEquals("(7.0 * y)", mult.toString());
    }
}

