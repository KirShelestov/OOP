package ru.nsu.shelestov.task3.operations;

import java.util.Map;
import ru.nsu.shelestov.task3.datatypes.Expression;

/**
 * Класс представляет операцию сложения.
 */
public class Add extends Expression {
    private final Expression left;
    private final Expression right;

    /**
     * конструктор операции сложения.
     *
     * @param left  левая часть уравнения
     * @param right праввая часть уравнения
     */
    public Add(Expression left, Expression right) {
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

        Add add = (Add) obj; // Приведение типа
        return left.equals(add.left) && right.equals(add.right); // Сравнение левой и правой частей
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

    /**
     * вывод выражения.
     */
    public void print() {
        System.out.print("(");
        left.print();
        System.out.print(" + ");
        right.print();
        System.out.print(")");
    }
}
