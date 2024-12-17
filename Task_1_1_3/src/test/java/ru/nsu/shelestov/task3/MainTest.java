package ru.nsu.shelestov.task3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.shelestov.task3.datatypes.Expression;
import ru.nsu.shelestov.task3.datatypes.Number;
import ru.nsu.shelestov.task3.datatypes.Variable;
import ru.nsu.shelestov.task3.operations.Add;
import ru.nsu.shelestov.task3.operations.Div;
import ru.nsu.shelestov.task3.operations.Mul;
import ru.nsu.shelestov.task3.operations.Sub;

/**
 * Класс для тестирования класса Main.
 */
public class MainTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    /**
     * Перед каждым тестом перенаправляем стандартный вывод.
     */
    @BeforeEach
    public void setUp() throws IOException {
        System.setOut(new PrintStream(outputStreamCaptor));
        outputStreamCaptor.close();
    }

    /**
     * После каждого теста восстанавливаем стандартный вывод.
     */
    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    /**
     * Тест проверяет правильно ли отрабатывают основные методы.
     */
    @Test
    void testMain() {
        String input = "(x + 2)\nx=10; y=13\nx\n";
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes())) {
            System.setIn(inputStream);
            Main.main(new String[0]);
        } catch (IOException e) {
            final var errorMessage = "Ошибка ввода/вывода: " + e.getMessage();
            System.err.println(errorMessage);
        } catch (ArithmeticException e) {
            final var errorMessage = "Ошибка арифметики: " + e.getMessage();
            System.err.println(errorMessage);
        } catch (NumberFormatException e) {
            final var errorMessage = "Неправильный формат числа: " + e.getMessage();
            System.err.println(errorMessage);
        } catch (IllegalArgumentException e) {
            final var errorMessage = "Ошибка недопустимого аргумента: " + e.getMessage();
            System.err.println(errorMessage);
        } catch (IllegalStateException e) {
            final var errorMessage = "Ошибка недопустимого состояния: " + e.getMessage();
            System.err.println(errorMessage);
        }
        String output = outputStreamCaptor.toString().trim();
        assertTrue(output.contains("Выражение:"));
        assertTrue(output.contains("Результат: 12.0"));
        assertTrue(output.contains("Производная по переменной:"));
    }



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

        Exception exception = assertThrows(NumberFormatException.class,
                () -> Main.parseVariableAssignments(input));

        assertTrue(exception.getMessage().contains("abc"));
    }

    @Test
    void testParseExpression() {
        Expression expr1 = Main.parseExpression("((x + 2) * (y - 3))");
        assertEquals(new Mul(new Add(new Variable("x"), new Number(2)), new Sub(new Variable("y"), new Number(3))), expr1);

        Expression expr2 = Main.parseExpression("(x / (2 + 3))");
        assertEquals(new Div(new Variable("x"), new Add(new Number(2), new Number(3))), expr2);

        // Проверка на некорректное выражение
        assertThrows(IllegalArgumentException.class, () -> Main.parseExpression("(x + 2) +"));
    }

    /**
     * Тестирует метод parseExpression на корректных математических выражениях.
     */
    @Test
    public void testParseExpressionNew() {
        Expression expr1 = Main.parseWithout("42.0");
        assertEquals(new Number(42.0), expr1);

        Expression expr2 = Main.parseWithout("x");
        assertEquals(new Variable("x"), expr2);

        Expression expr3 = Main.parseWithout("(x + 2.0)");
        assertInstanceOf(Add.class, expr3);
        assertEquals(new Add(new Variable("x"), new Number(2.0)), expr3);

        Expression expr4 = Main.parseWithout("(3 * (x + 4))");
        assertInstanceOf(Mul.class, expr4);
        assertEquals(new Mul(new Number(3.0),
                new Add(new Variable("x"), new Number(4.0))), expr4);


        Expression expr5 = Main.parseWithout("x + y * 2 ");
        assertInstanceOf(Add.class, expr5);
        assertEquals(new Add(new Variable("x"),
                new Mul(new Variable("y"), new Number(2.0))), expr5);

        Expression expr6 = Main.parseWithout("x / 2.0");
        assertInstanceOf(Div.class, expr6);
        Assertions.assertEquals(new Div(new Variable("x"), new Number(2.0)), expr6);

    }

    /**
     * Тестирует метод parseExpression на некорректных выражениях.
     */
    @Test
    public void testParseInvalidExpression() {
        // Тест на пустую строку
        Exception exception1 = assertThrows(IllegalArgumentException.class,
                () -> Main.parseExpression(""));
        assertTrue(exception1.getMessage().contains("Вы ввели не выражение"));

        // Тест на некорректное выражение
        Exception exception2 = assertThrows(IllegalArgumentException.class,
                () -> Main.parseExpression("++2"));
        assertTrue(exception2.getMessage().contains("Вы ввели не выражение"));

        // Тест на выражение с неправильными скобками
        Exception exception3 = assertThrows(IllegalArgumentException.class,
                () -> Main.parseExpression("(2.0 + (3.0 * 4.0)"));
        assertTrue(exception3.getMessage().contains("Вы ввели не выражение"));
    }


    /**
     * Тест на более сложное выражение.
     */
    @Test
    public void testTeethCrashingExpression() {
        Expression expr =
                Main.parseExpression("(((x + 2) * ((y - 3) / 4)) - (z + (x*(x/(y-z)))))");
        assertEquals(new Sub(new Mul(new Add(new Variable("x"), new Number(2)),
                new Div(new Sub(new Variable("y"), new Number(3)), new Number(4))),
                new Add(new Variable("z"), new Mul(new Variable("x"), new Div(new Variable("x"),
                        new Sub(new Variable("y"), new Variable("z")))))), expr);

        Expression derivative = expr.derivative("x");
        assertEquals("((((1.0 + 0.0) * ((y - 3.0) / 4.0)) + ((x + 2.0) * ((((0.0 - 0.0) * 4.0)"
              +  " - ((y - 3.0) * 0.0)) / (4.0 * 4.0)))) - (0.0 + ((1.0 * (x / (y - z))) + "
              +  "(x * (((1.0 * (y - z)) - (x * (0.0 - 0.0))) / ((y - z) * (y - z)))))))",
                derivative.toString());


        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 100.0);
        variables.put("y", 10.0);
        variables.put("z", 1.0);

        double result = expr.evaluate(variables);
        assertEquals(-933.6111111111111, result);
    }
}
