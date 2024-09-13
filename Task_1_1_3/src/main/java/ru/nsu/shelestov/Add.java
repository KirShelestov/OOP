package ru.nsu.shelestov;

import java.util.Map;

/**
 * Класс представляет операцию сложения.
 */
class Add extends Expression {
    private final Expression left;
    private final Expression right;

    /**
     * конструктор операции сложения.
     *
     * @param left левая часть уравнения
     * @param right праввая часть уравнения
     */
    public Add(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * подсчет значения выражения.
     *
     * @param variables означиваемые выражения
     * @return значение при означивании
     */
    public double evaluate(Map<String, Double> variables) {
        return left.evaluate(variables) + right.evaluate(variables);
    }

    /**
     * составление отформатированной стркои.
     *
     * @return отформатированная строка
     */
    public String toString() {
        return "(" + left.toString() + " + " + right.toString() + ")";
    }

    /**
     * дифференцирование выражения.
     *
     * @param var переменная по которой идет дифференцирование
     * @return производная по переменной
     */
    public Expression derivative(String var) {
        return new Add(left.derivative(var), right.derivative(var));
    }

    public void print() {
        System.out.print("(");
        left.print();
        System.out.print(" + ");
        right.print();
        System.out.print(")");
    }
}
