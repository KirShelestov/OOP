package ru.nsu.shelestov;

import java.util.Map;

/**
 * Класс представляет "число".
 */
class Number extends Expression {
    private final double value;

    /**
     * конструктор для числа.
     *
     * @param value само число
     */
    public Number(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false; // Проверка типа

        Number number = (Number) obj; // Приведение типа
        return Double.compare(number.value, value) == 0; // Сравнение значений
    }

    /**
     * подсчет значения при означивании.
     *
     * @param variables означиваемые перменные
     * @return значение при означивании
     */
    public double evaluate(Map<String, Double> variables) {
        return value;
    }

    /**
     * форматирование строки.
     *
     * @return отформатированная строка
     */
    public String toString() {
        return Double.toString(value);
    }

    /**
     * дифференцирование
     *
     * @param var переменная по которой идет дифференцирование
     * @return
     */
    public Expression derivative(String var) {
        return new Number(0);
    }

    /**
     * вывод числа.
     */
    public void print() {
        System.out.print(toString());
    }
}
