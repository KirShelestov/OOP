package ru.nsu.shelestov;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс для тестирования класса Main.
 */
public class MainTest {

    /**
     * тест для метода parseVariableAssignments на корректном парсинге.
     * присваиваний переменных из строки.
     */
    @Test
    public void testParseVariableAssignments() {
        String input = "x=10; y=13; z=5.5";
        Map<String, Double> expected = new HashMap<>();
        expected.put("x", 10.0);
        expected.put("y", 13.0);
        expected.put("z", 5.5);

        Map<String, Double> result = Main.parseVariableAssignments(input);
        assertEquals(expected, result);
    }

    /**
     * тест для метода evaluate для выражения, состоящего из переменных и чисел.
     */
    @Test
    public void testEvaluateExpression() {
        Expression expr = new Add(new Variable("x"), new Mul(new Number(2), new Variable("y")));

        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 3.0);
        variables.put("y", 4.0);

        double result = expr.evaluate(variables);
        assertEquals(3.0 + 2 * 4.0, result);
    }

    /**
     * тест метода toString для корректного отображения выражения в строковом формате.
     */
    @Test
    public void testPrintExpression() {
        Expression expr = new Add(new Variable("x"), new Mul(new Number(2), new Variable("y")));

        // Печать выражения
        String expectedOutput = "(x + (2.0 * y))";
        assertEquals(expectedOutput, expr.toString());
    }

    /**
     * тест для обработки ошибок при неверных присваиваниях переменных.
     */
    @Test
    public void testInvalidVariableAssignments() {
        String input = "x=10; y=abc; z=5.5";

        Exception exception = assertThrows(NumberFormatException.class, () -> {
            Main.parseVariableAssignments(input);
        });

        assertTrue(exception.getMessage().contains("For input string: \"abc\""));
    }
}
