package ru.nsu.shelestov;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Класс для тестирования операции деления.
 */
class DivTest {

    /**
     * тест для проверки правильности форматирования строки.
     */
    @Test
    void testToString() {
        Expression numerator = new Number(5);
        Expression denominator = new Number(3);
        Div division = new Div(numerator, denominator);

        assertEquals("(5.0 / 3.0)", division.toString());
    }

    /**
     * тест для проверки правильности нахождения производной.
     */
    @Test
    void testDerivative() {
        Expression numerator = new Variable("x");
        Expression denominator = new Number(2);
        Div division = new Div(numerator, denominator);

        Expression derivative = division.derivative("x");
        assertEquals("(((1.0 * 2.0) - (x * 0.0)) / (2.0 * 2.0))", derivative.toString());
    }

    /**
     * тест для проверки правильности вывода строки с операцией деления.
     */
    @Test
    void testPrint() {
        Expression numerator = new Number(7);
        Expression denominator = new Variable("y");
        Div division = new Div(numerator, denominator);
        division.print();
        assertEquals("(7.0 / y)", division.toString());
    }
}

