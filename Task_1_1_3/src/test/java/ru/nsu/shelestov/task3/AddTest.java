package ru.nsu.shelestov.task3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import ru.nsu.shelestov.task3.datatypes.Expression;
import ru.nsu.shelestov.task3.datatypes.Number;
import ru.nsu.shelestov.task3.datatypes.Variable;
import ru.nsu.shelestov.task3.operations.Add;

/**
 * Класс дял тестирования правильности операции сложения.
 */
public class AddTest {

    /**
     * тест для проверки правильности форматирования строки.
     */
    @Test
    void testToString() {
        Expression summandFirst = new Number(5);
        Expression summandSecond = new Number(3);
        Add sum = new Add(summandFirst, summandSecond);

        assertEquals("(5.0 + 3.0)", sum.toString());
    }

    /**
     * тест для проверки правильности нахождения производной.
     */
    @Test
    void testDerivative() {
        Expression summandFirst = new Variable("x");
        Expression summandSecond = new Number(2);
        Add sum = new Add(summandFirst, summandSecond);

        Expression derivative = sum.derivative("x");
        assertEquals("(1.0 + 0.0)", derivative.toString());
    }

    /**
     * тест для проверки правильности вывода строки с операцией сложения.
     */
    @Test
    void testPrint() {
        Expression summandFirst = new Number(7);
        Expression summandSecond = new Variable("y");
        Add sum = new Add(summandFirst, summandSecond);
        sum.print();
        assertEquals("(7.0 + y)", sum.toString());
    }
}

