package ru.nsu.shelestov.task3.operations;

import java.util.Map;
import ru.nsu.shelestov.task3.datatypes.Expression;
import ru.nsu.shelestov.task3.datatypes.Number;

/**
 * Класс представляет операцию сложения.
 */
public class Add extends Expression {
    private final Expression left;
    private final Expression right;

    /**
     * Конструктор.
     *
     * @param left левое слагаемое
     * @param right правое слагаемое
     */
    public Add(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Означивание.
     *
     * @param variables означиваемые перменные
     * @return сумма означивания слагаемых
     */
    @Override
    public double evaluate(Map<String, Double> variables) {
        return left.evaluate(variables) + right.evaluate(variables);
    }

    /**
     * Строкове представление.
     *
     * @return строчка
     */
    @Override
    public String toString() {
        return "(" + left.toString() + " + " + right.toString() + ")";
    }

    /**
     * Производная.
     *
     * @param var переменная по которой идет дифференцирование
     * @return сумма производных
     */
    @Override
    public Expression derivative(String var) {
        return new Add(left.derivative(var), right.derivative(var));
    }

    /**
     * Упрощенние по правилам.
     *
     * @return упрощенное выражение.
     */
    @Override
    public Expression simplify() {
        Expression simplifiedLeft = left.simplify();
        Expression simplifiedRight = right.simplify();

        // Если оба числа числа, то вернуть их сумму
        if (simplifiedLeft instanceof Number leftNum && simplifiedRight instanceof Number rightNum) {
            return new Number(leftNum.getValue() + rightNum.getValue());
        }


        // Если слева ноль, то вернуть правую часть
        if (simplifiedLeft instanceof Number leftNum && leftNum.evaluate(Map.of()) == 0) {
            return simplifiedRight;
        }

        // Наоборот
        if (simplifiedRight instanceof Number rightNum && rightNum.evaluate(Map.of()) == 0) {
            return simplifiedLeft;
        }

        return new Add(simplifiedLeft, simplifiedRight);
    }

    /**
     * Переопределение равенства.
     *
     * @param obj объект с которым мы сравниваем текущий
     * @return равенство объектов
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Add)) {
            return false;
        }
        Add other = (Add) obj;
        return left.equals(other.left) && right.equals(other.right);
    }

    /**
     * Переопределение hashCode.
     *
     * @return хеш-код переменной
     */
    @Override
    public int hashCode() {
        return 31 * left.hashCode() + right.hashCode();
    }
}
