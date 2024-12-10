package ru.nsu.shelestov.task3.operations;

import java.util.Map;
import ru.nsu.shelestov.task3.datatypes.Expression;
import ru.nsu.shelestov.task3.datatypes.Number;

/**
 * Класс представляет вычитание.
 */
public class Sub extends Expression {
    private final Expression left;
    private final Expression right;

    /**
     * Конструктор.
     *
     * @param left
     * @param right правое слагаемое
     */
    public Sub(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Означивание.
     *
     * @param variables означиваемые перменные
     * @return разность означивания слагаемых
     */
    @Override
    public double evaluate(Map<String, Double> variables) {
        return left.evaluate(variables) - right.evaluate(variables);
    }

    /**
     * Строкове представление.
     *
     * @return строчка
     */
    @Override
    public String toString() {
        return "(" + left.toString() + " - " + right.toString() + ")";
    }

    /**
     * Производная.
     *
     * @param var переменная по которой идет дифференцирование
     * @return по правилам
     */
    @Override
    public Expression derivative(String var) {
        return new Sub(left.derivative(var), right.derivative(var));
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

        // Если обе части равны, то возврщаем 0
        if (simplifiedLeft.equals(simplifiedRight)) {
            return new Number(0);
        }

        // Если справа ноль, то возвращаем правую часть
        if (simplifiedRight instanceof Number rightNum && rightNum.evaluate(Map.of()) == 0) {
            return simplifiedLeft;
        }

        return new Sub(simplifiedLeft, simplifiedRight);
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
        if (!(obj instanceof Sub)) {
            return false;
        }
        Sub other = (Sub) obj;
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
