package ru.nsu.shelestov.task3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import ru.nsu.shelestov.task3.datatypes.Expression;
import ru.nsu.shelestov.task3.datatypes.Number;
import ru.nsu.shelestov.task3.datatypes.Variable;
import ru.nsu.shelestov.task3.operations.Sub;

/**
 * Класс для тестирования правильности операции вычитания.
 */
public class SubTest {

    /**
     * тест для проверки правильности форматирования строки.
     */
    @Test
    void testToString() {
        Expression minuend = new Number(5);
        Expression subtrahend = new Number(3);
        Sub subs = new Sub(minuend, subtrahend);

        assertEquals("(5.0 - 3.0)", subs.toString());
    }

    /**
     * тест для проверки правильности нахождения производной.
     */
    @Test
    void testDerivative() {
        Expression minuend = new Variable("x");
        Expression subtrahend = new Number(2);
        Sub subs = new Sub(minuend, subtrahend);

        Expression derivative = subs.derivative("x");
        assertEquals("(1.0 - 0.0)", derivative.toString());
    }

    /**
     * тест для проверки правильности вывода строки с операцией вычитания.
     */
    @Test
    void testPrint() {
        Expression minuend = new Number(7);
        Expression subtrahend = new Variable("y");
        Sub subs = new Sub(minuend, subtrahend);
        subs.print();
        assertEquals("(7.0 - y)", subs.toString());
    }
}

