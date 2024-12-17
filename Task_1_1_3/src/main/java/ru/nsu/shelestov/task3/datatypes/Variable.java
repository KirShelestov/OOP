package ru.nsu.shelestov.task3.datatypes;

import java.util.Map;

/**
 * класс представляет переменную.
 */
public class Variable extends Expression {
    private final String name;

    /**
     * Конструктор.
     *
     * @param name имя переменной
     */
    public Variable(String name) {
        this.name = name;
    }

    /**
     * Означивание.
     *
     * @param variables означиваемые перменные
     * @return означенная переменная
     */
    @Override
    public double evaluate(Map<String, Double> variables) {
        return variables.getOrDefault(name, 0.0);
    }

    /**
     * Строкове представление.
     *
     * @return строчка
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Производная.
     *
     * @param var переменная по которой идет дифференцирование
     * @return 0/1, зависит от того, по какой переменной производная
     */
    @Override
    public Expression derivative(String var) {
        return name.equals(var) ? new Number(1) : new Number(0);
    }

    /**
     * Для переменной упрощения нет.
     *
     * @return переменная
     */
    @Override
    public Expression simplify() {
        return this;
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
        if (!(obj instanceof Variable)) {
            return false;
        }
        Variable other = (Variable) obj;
        return name.equals(other.name);
    }

    /**
     * Переопределение hashCode.
     *
     * @return хеш-код переменной
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
