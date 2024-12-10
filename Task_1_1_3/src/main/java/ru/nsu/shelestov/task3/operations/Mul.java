package ru.nsu.shelestov.task3.operations;

import java.util.Map;
import ru.nsu.shelestov.task3.datatypes.Expression;
import ru.nsu.shelestov.task3.datatypes.Number;

/**
 * Класс представляет операцию умножения.
 */
public class Mul extends Expression {
    private final Expression left;
    private final Expression right;

    /**
     * Конструктор.
     *
     * @param left уменьшаемое
     * @param right вычитаемое
     */

    public Mul(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Означивание.
     *
     * @param variables означиваемые перменные
     * @return произведение означивания слагаемых
     */
    @Override
    public double evaluate(Map<String, Double> variables) {
        return left.evaluate(variables) * right.evaluate(variables);
    }

    /**
     * Строкове представление.
     *
     * @return строчка
     */
    @Override
    public String toString() {
        return "(" + left.toString() + " * " + right.toString() + ")";
    }

    /**
     * Производная.
     *
     * @param var переменная по которой идет дифференцирование
     * @return по правилам
     */
    @Override
    public Expression derivative(String var) {
        return new Add(new Mul(left.derivative(var), right), new Mul(left, right.derivative(var)));
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

        // Если умножаем на ноль, то возвращаем просто 0
        if (simplifiedLeft instanceof Number leftNum && leftNum.evaluate(Map.of()) == 0) {
            return new Number(0);
        }
        if (simplifiedRight instanceof Number rightNum && rightNum.evaluate(Map.of()) == 0) {
            return new Number(0);
        }

        // Если перед нами числа, то просто умножаем их
        if (simplifiedLeft instanceof Number leftNum &&
                simplifiedRight instanceof Number rightNum) {
            return new Number(leftNum.evaluate(Map.of()) * rightNum.evaluate(Map.of()));
        }

        // Если слева один, то просто возвращаем правую часть
        if (simplifiedLeft instanceof Number leftNum && leftNum.evaluate(Map.of()) == 1) {
            return simplifiedRight;
        }

        // Наоборот
        if (simplifiedRight instanceof Number rightNum && rightNum.evaluate(Map.of()) == 1) {
            return simplifiedLeft;
        }

        return new Mul(simplifiedLeft, simplifiedRight);
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
        if (!(obj instanceof Mul)) {
            return false;
        }
        Mul other = (Mul) obj;
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
