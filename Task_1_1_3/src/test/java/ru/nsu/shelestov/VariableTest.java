package ru.nsu.shelestov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.nsu.shelestov.Main.parseExpression;
import static ru.nsu.shelestov.Main.parseVariableAssignments;

import java.util.Map;
import org.junit.jupiter.api.Test;
import ru.nsu.shelestov.datatypes.Expression;
import ru.nsu.shelestov.datatypes.Variable;

/**
 * Класс для тестирования правильности класса Variable.
 */
public class VariableTest {

    /**
     * тест для проверки форматирования строки.
     */
    @Test
    void testToString() {
        Expression vars = new Variable("x");

        assertEquals("x", vars.toString());
    }

    /**
     * тест для проверки правильности нахождения производной.
     */
    @Test
    void testDerivative() {
        Expression vars = new Variable("x");


        Expression derivative = vars.derivative("x");
        assertEquals("1.0", derivative.toString());
    }

    /**
     * тест для проверки правильности вывода строки с одной переменной.
     */
    @Test
    void testPrint() {
        Expression vars = new Variable("x");
        vars.print();
        assertEquals("x", vars.toString());
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
