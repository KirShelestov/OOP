package ru.nsu.shelestov;

import java.util.Map;

/**
 * класс представляет переменную.
 */
class Variable extends Expression {
    private final String name;

    /**
     * конструктор для переменной.
     *
     * @param name название переменной
     */
    public Variable(String name) {
        this.name = name;
    }

    /**
     * подсчет значения при означивании.
     *
     * @param variables означиваемые перменные
     * @return значение при означивании
     */
    public double evaluate(Map<String, Double> variables) {
        return variables.getOrDefault(name, 0.0);
    }

    /**
     * имя переменной как строка.
     *
     * @return имя переменной
     */
    public String toString() {
        return name;
    }

    /**
     * дифференцирование перменной.
     *
     * @param var переменная по которой идет дифференцирование
     * @return производная переменной
     */
    public Expression derivative(String var) {
        return name.equals(var) ? new Number(1) : new Number(0);
    }

    /**
     * вывод имени переменной.
     */
    public void print() {
        System.out.print(toString());
    }
}
