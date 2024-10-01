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
     * означивание при делении.
     *
     * @param variables означиваемые перменные
     * @return значение выражения
     */
    public double evaluate(Map<String, Double> variables) {
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
        System.out.print("(");
        numerator.print();
        System.out.print(" / ");
        denominator.print();
        System.out.print(")");
    }
}
