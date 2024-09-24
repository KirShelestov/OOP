package ru.nsu.shelestov.operations;

import java.util.Map;
import ru.nsu.shelestov.datatypes.Expression;

/**
 * Класс представляет вычитание.
 */
public class Sub extends Expression {
    private final Expression left;
    private final Expression right;

    /**
     * конструктор для вычитания.
     *
     * @param left  левая часть уравнения
     * @param right правая часть уравнения
     */
    public Sub(Expression left, Expression right) {
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

        Sub sub = (Sub) obj; // Приведение типа
        return left.equals(sub.left) && right.equals(sub.right); // Сравнение левой и правой частей
    }

    /**
     * подсчет значения при означивании.
     *
     * @param variables означиваемые перменные
     * @return значение при означивании
     */
    public double evaluate(Map<String, Double> variables) {
        return left.evaluate(variables) - right.evaluate(variables);
    }

    /**
     * форматирование строки.
     *
     * @return строка в нужном формате.
     */
    public String toString() {
        return "(" + left.toString() + " - " + right.toString() + ")";
    }

    /**
     * дифференцирование операции вычитания.
     *
     * @param var переменная по которой идет дифференцирование
     * @return производная вычитания
     */
    public Expression derivative(String var) {
        return new Sub(left.derivative(var), right.derivative(var));
    }

    /**
     * вывод выражения.
     */
    public void print() {
        System.out.print("(");
        left.print();
        System.out.print(" - ");
        right.print();
        System.out.print(")");
    }
}
