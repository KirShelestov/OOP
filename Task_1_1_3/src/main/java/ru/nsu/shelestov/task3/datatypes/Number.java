package ru.nsu.shelestov.task3.datatypes;

import java.util.Map;

/**
 * Класс представляет "число".
 */
public class Number extends Expression {
    private final double value;

    /**
     * Конструктор.
     *
     * @param value чиселка
     */
    public Number(double value) {
        this.value = value;
    }

    /**
     * Означивание.
     *
     * @param variables означиваемые перменные
     * @return чиселка
     */
    @Override
    public double evaluate(Map<String, Double> variables) {
        return value;
    }

    /**
     * Строкове представление.
     *
     * @return строчка
     */
    @Override
    public String toString() {
        return Double.toString(value);
    }

    /**
     * Получить значение числа.
     *
     * @return значение числа
     */
    public double getValue() {
        return value;
    }

    /**
     * Производная.
     *
     * @param var переменная по которой идет дифференцирование
     * @return ноль как производная для числа
     */
    @Override
    public Expression derivative(String var) {
        return new Number(0);
    }

    /**
     * Упрощения нету.
     *
     * @return циферкак
     */
    @Override
    public Expression simplify() {
        return this;
    }

    /**
     * Переопределние equals.
     *
     * @param obj объект с которым сравниваем
     * @return равны ли объекты
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Number)) {
            return false;
        }
        Number other = (Number) obj;
        return Double.compare(value, other.value) == 0;
    }

    /**
     * Переопределение hashCode.
     *
     * @return хеш-код числа
     */
    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }
}