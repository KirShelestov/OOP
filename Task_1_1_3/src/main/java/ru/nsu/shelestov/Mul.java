package ru.nsu.shelestov;

import java.util.Map;

/**
 * Класс представляет операцию умножения.
 */
public class Mul extends Expression {
    private final Expression left;
    private final Expression right;

    /**
     * конструктор для умножения.
     *
     * @param left  левая часть выражения
     * @param right правая часть выражения
     */
    public Mul(Expression left, Expression right) {
        this.left = left;
        this.right = right;
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

        Mul mult = (Mul) obj; // Приведение типа
        return left.equals(mult.left) && right.equals(mult.right); // Сравнение левой и правой частей
    }

    /**
     * подсчет значения при означивании.
     *
     * @param variables означиваемые перменные
     * @return значение выражения
     */
    public double evaluate(Map<String, Double> variables) {
        return left.evaluate(variables) * right.evaluate(variables);
    }

    /**
     * форматирование выражение в строку.
     *
     * @return отформатированная строка
     */
    public String toString() {
        return "(" + left.toString() + " * " + right.toString() + ")";
    }

    /**
     * дифференцирование выражения.
     *
     * @param var переменная по которой идет дифференцирование
     * @return производная при умножении
     */
    public Expression derivative(String var) {
        return new Add(new Mul(left.derivative(var), right), new Mul(left, right.derivative(var)));
    }

    /**
     * вывод выражения.
     */
    public void print() {
        System.out.print("(");
        left.print();
        System.out.print(" * ");
        right.print();
        System.out.print(")");
    }
}
