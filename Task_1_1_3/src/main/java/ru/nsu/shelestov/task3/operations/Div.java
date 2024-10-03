package ru.nsu.shelestov.task3.operations;

import java.util.Map;
import ru.nsu.shelestov.task3.datatypes.Expression;

/**
 * Класс который представляет операцию деления.
 */
public class Div extends Expression {
    private final Expression numerator;
    private final Expression denominator;

    /**
     * конструктор для операции деления.
     *
     * @param numerator   делимое
     * @param denominator делитель
     */
    public Div(Expression numerator, Expression denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    /**
     * переопределение равенства между объектами одного класса.
     *
     * @param obj объект с которым хотим сравнить текущий объект
     * @return равны или не равны объекты
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true; // Сравнение ссылок
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false; // Проверка типа
        }

        Div div = (Div) obj; // Приведение типа
        return numerator.equals(div.numerator) && denominator.equals(div.denominator);
    }

    /**
     * Переопределение хэш-кода.
     *
     * @return хэш-код объекта
     */
    @Override
    public int hashCode() {
        int result = numerator.hashCode();
        result = 31 * result + denominator.hashCode();
        return result;
    }

    /**
     * означивание при делении.
     *
     * @param variables означиваемые перменные
     * @return значение выражения
     */
    public double evaluate(Map<String, Double> variables) {
        if (denominator.evaluate(variables) == 0.0) {
            throw new ArithmeticException("/ 0!");
        }
        return numerator.evaluate(variables) / denominator.evaluate(variables);
    }

    /**
     * составление отформатированной строки.
     *
     * @return отформатированная строка
     */
    public String toString() {
        return "(" + numerator.toString() + " / " + denominator.toString() + ")";
    }

    /**
     * подсчет производной.
     *
     * @param var переменная по которой идет дифференцирование
     * @return производная
     */
    public Expression derivative(String var) {
        return new Div(new Sub(new Mul(numerator.derivative(var), denominator),
                new Mul(numerator, denominator.derivative(var))),
                new Mul(denominator, denominator));
    }

    /**
     * вывод выражения.
     */
    public void print() {
        System.out.print(this.toString());
    }
}
