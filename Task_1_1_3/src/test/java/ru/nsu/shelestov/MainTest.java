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


    /**
     * Тестирует метод parseExpression на корректных математических выражениях.
     */
    @Test
    public void testParseExpression() {
        Expression expr1 = Main.parseExpression("42.0");
        assertEquals(new Number(42.0), expr1);

        Expression expr2 = Main.parseExpression("x");
        assertEquals(new Variable("x"), expr2);

        Expression expr3 = Main.parseExpression("(x + 2.0)");
        assertInstanceOf(Add.class, expr3);
        assertEquals(new Add(new Variable("x"), new Number(2.0)), expr3);

        Expression expr4 = Main.parseExpression("(3 * (x + 4))");
        assertInstanceOf(Mul.class, expr4);
        assertEquals(new Mul(new Number(3.0), new Add(new Variable("x"), new Number(4.0))), expr4);


        Expression expr5 = Main.parseExpression("((x + 2) * (y - 3))");
        assertInstanceOf(Mul.class, expr5);
        assertEquals(new Mul(new Add(new Variable("x"), new Number(2.0)), new Sub(new Variable("y"), new Number(3.0))), expr5);

        Expression expr6 = Main.parseExpression("(x / 2.0)");
        assertInstanceOf(Add.class, expr3);
        assertEquals(new Div(new Variable("x"), new Number(2.0)), expr6);

    }

    /**
     * Тестирует метод parseExpression на некорректных выражениях.
     */
    @Test
    public void testParseInvalidExpression() {
        // Тест на пустую строку
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
            Main.parseExpression("");
        });
        assertTrue(exception1.getMessage().contains("Вы ввели не выражение"));

        // Тест на некорректное выражение
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            Main.parseExpression("++2");
        });
        assertTrue(exception2.getMessage().contains("Вы ввели не выражение"));

        // Тест на выражение с неправильными скобками
        Exception exception3 = assertThrows(IllegalArgumentException.class, () -> {
            Main.parseExpression("(2.0 + (3.0 * 4.0)");
        });
        assertTrue(exception3.getMessage().contains("Вы ввели не выражение"));
    }
}
