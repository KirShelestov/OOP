package ru.nsu.shelestov;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Класс дял тестирования правильности операции сложения.
 */
class AddTest {

    /**
     * тест для проверки правильности форматирования строки.
     */
    @Test
    void testToString() {
        Expression numerator = new Number(5);
        Expression denominator = new Number(3);
        Add summator = new Add(numerator, denominator);

        assertEquals("(5.0 + 3.0)", summator.toString());
    }

    /**
     * тест для проверки правильности нахождения производной.
     */
    @Test
    void testDerivative() {
        Expression numerator = new Variable("x");
        Expression denominator = new Number(2);
        Add summator = new Add(numerator, denominator);

        Expression derivative = summator.derivative("x");
        assertEquals("(1.0 + 0.0)", derivative.toString());
    }

    /**
     * тест для проверки правильности вывода строки с операцией сложения.
     */
    @Test
    void testPrint() {
        Expression numerator = new Number(7);
        Expression denominator = new Variable("y");
        Add division = new Add(numerator, denominator);
        division.print();
        assertEquals("(7.0 + y)", division.toString());
    }
}

