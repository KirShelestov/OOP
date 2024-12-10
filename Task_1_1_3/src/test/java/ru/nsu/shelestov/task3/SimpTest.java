package ru.nsu.shelestov.task3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import ru.nsu.shelestov.task3.datatypes.Expression;
import ru.nsu.shelestov.task3.datatypes.Number;
import ru.nsu.shelestov.task3.datatypes.Variable;
import ru.nsu.shelestov.task3.operations.Add;
import ru.nsu.shelestov.task3.operations.Div;
import ru.nsu.shelestov.task3.operations.Mul;
import ru.nsu.shelestov.task3.operations.Sub;

/**
 * Класс для тестирования упрощения.
 */
public class SimpTest {

    /**
     * Тест для проверки упрощения сложения.
     */
    @Test
    void testAddSimplify() {
        Expression expr1 = new Add(new Number(5), new Number(3)); // 5 + 3
        assertEquals(new Number(8), expr1.simplify());

        Expression expr2 = new Add(new Number(0), new Variable("x")); // 0 + x
        assertEquals(new Variable("x"), expr2.simplify());

        Expression expr3 = new Add(new Variable("x"), new Number(0)); // x + 0
        assertEquals(new Variable("x"), expr3.simplify());
    }

    /**
     * Тест для проверки упрощения вычитания.
     */
    @Test
    void testSubSimplify() {
        Expression expr1 = new Sub(new Number(5), new Number(5)); // 5 - 5
        assertEquals(new Number(0), expr1.simplify());

        Expression expr2 = new Sub(new Variable("x"), new Variable("x")); // x - x
        assertEquals(new Number(0), expr2.simplify());

        Expression expr3 = new Sub(new Number(3), new Number(0)); // 3 - 0
        assertEquals(new Number(3), expr3.simplify());
    }

    /**
     * Тест для проверки упрощения умножения.
     */
    @Test
    void testMulSimplify() {
        Expression expr1 = new Mul(new Number(5), new Number(3)); // 5 * 3
        assertEquals(new Number(15), expr1.simplify());

        Expression expr2 = new Mul(new Number(0), new Variable("x")); // 0 * x
        assertEquals(new Number(0), expr2.simplify());

        Expression expr3 = new Mul(new Variable("x"), new Number(1)); // x * 1
        assertEquals(new Variable("x"), expr3.simplify());
    }

    /**
     * Тест для проверки упрощения деления.
     */
    @Test
    void testDivSimplify() {
        Expression expr1 = new Div(new Number(6), new Number(3)); // 6 / 3
        assertEquals(new Number(2), expr1.simplify());

        Expression expr2 = new Div(new Variable("x"), new Number(1)); // x / 1
        assertEquals(new Variable("x"), expr2.simplify());

        // Проверка деления на 0 (не упрощаем, но проверяем, что не выбрасывает исключение)
        Expression expr3 = new Div(new Number(5), new Number(0)); // 5 / 0
        assertEquals(new Div(new Number(5), new Number(0)), expr3.simplify());
    }
}
