package ru.nsu.shelestov.task3.operations;

import java.util.Map;
import ru.nsu.shelestov.task3.datatypes.Expression;
import ru.nsu.shelestov.task3.datatypes.Number;

/**
 * Класс который представляет операцию деления.
 */
public class Div extends Expression {
    private final Expression numerator;
    private final Expression denominator;

    /**
     * Конструктор.
     *
     * @param numerator знаменатель
     * @param denominator делитель
     */
    public Div(Expression numerator, Expression denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    /**
     * Означивание.
     *
     * @param variables означиваемые перменные
     * @return частное означивания слагаемых
     */
    @Override
    public double evaluate(Map<String, Double> variables) {
        if (denominator.evaluate(variables) == 0.0) {
            throw new ArithmeticException("/ 0!");
        }
        return numerator.evaluate(variables) / denominator.evaluate(variables);
    }

    /**
     * Строкове представление.
     *
     * @return строчка
     */
    @Override
    public String toString() {
        return "(" + numerator.toString() + " / " + denominator.toString() + ")";
    }

    /**
     * Производная.
     *
     * @param var переменная по которой идет дифференцирование
     * @return по правилам
     */
    @Override
    public Expression derivative(String var) {
        return new Div(new Sub(new Mul(numerator.derivative(var), denominator),
                new Mul(numerator, denominator.derivative(var))),
                new Mul(denominator, denominator));
    }

    /**
     * Упрощенние по правилам.
     *
     * @return упрощенное выражение.
     */
    @Override
    public Expression simplify() {
        Expression simplifiedNumerator = numerator.simplify();
        Expression simplifiedDenominator = denominator.simplify();

        // Если перед нами числа, то просто их делим
        if (simplifiedNumerator instanceof Number num
                && simplifiedDenominator instanceof Number denom) {
            if (denom.evaluate(Map.of()) == 0) {
                return new Div(num, denom); // Если поделили на ноль, то возвращаем на место
            }
            return new Number(num.evaluate(Map.of()) / denom.evaluate(Map.of()));
        }

        // Если делим на 1, то вернуть числитель
        if (simplifiedDenominator instanceof Number denom && denom.evaluate(Map.of()) == 1) {
            return simplifiedNumerator;
        }

        return new Div(simplifiedNumerator, simplifiedDenominator);
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
        if (!(obj instanceof Div)) {
            return false;
        }
        Div other = (Div) obj;
        return numerator.equals(other.numerator) && denominator.equals(other.denominator);
    }

    /**
     * Переопределение hashCode.
     *
     * @return хеш-код переменной
     */
    @Override
    public int hashCode() {
        return 31 * numerator.hashCode() + denominator.hashCode();
    }

}
