package ru.nsu.shelestov.task3.datatypes;

import java.util.Map;

/**
 * Класс представляет "число".
 */
public class Number extends Expression {
    private final double value;

    /**
     * конструктор для числа.
     *
     * @param value само число
     */
    public Number(double value) {
        this.value = value;
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
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false; // Проверка типа
        }

        Number number = (Number) obj; // Приведение типа
        return Double.compare(number.value, value) == 0; // Сравнение значений
    }

    /**
     * Переопределение хэш-кода.
     *
     * @return хэш-код объекта
     */
    @Override
    public int hashCode() {
        return Double.hashCode(value);
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
    @Override
    public String toString() {
        return Double.toString(value);
    }

    /**
     * дифференцирование.
     *
     * @param var переменная по которой идет дифференцирование
     * @return ноль.
     */
    public Expression derivative(String var) {
        return new Number(0);
    }
}
