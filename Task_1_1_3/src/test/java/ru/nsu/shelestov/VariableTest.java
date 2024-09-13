package ru.nsu.shelestov;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.nsu.shelestov.Main.parseExpression;
import static ru.nsu.shelestov.Main.parseVariableAssignments;

/**
 * Класс для тестирования правильности класса Variable.
 */
class VariableTest {

    /**
     * тест для проверки форматирования строки.
     */
    @Test
    void testToString() {
        Expression numerator = new Variable("x");

        assertEquals("x", numerator.toString());
    }

    /**
     * тест для проверки правильности нахождения производной.
     */
    @Test
    void testDerivative() {
        Expression numerator = new Variable("x");


        Expression derivative = numerator.derivative("x");
        assertEquals("1.0", derivative.toString());
    }

    /**
     * тест для проверки правильности вывода строки с одной переменной.
     */
    @Test
    void testPrint() {
        Expression numerator = new Variable("x");
        numerator.print();
        assertEquals("x", numerator.toString());
    }

    /**
     * тест для проверки правильного означивания выражения с одной переменной.
     */
    @Test
    void testEvaluate() {
        Expression expr = parseExpression("x");
        String variableAssignments = "x=10";
        Map<String, Double> variables = parseVariableAssignments(variableAssignments);

        double result = expr.evaluate(variables);
        assertEquals(10.0, result);

    }
}

