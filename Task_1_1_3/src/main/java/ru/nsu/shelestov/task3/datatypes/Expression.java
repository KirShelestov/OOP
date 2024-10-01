package ru.nsu.shelestov.task3.datatypes;

import java.util.Map;

/**
 * Абстрактный класс который определяет методы для выражения.
 */
public abstract class Expression {
    /**
     * означивание выражения.
     *
     * @param variables означиваемые перменные
     * @return значение выражения
     */
    public abstract double evaluate(Map<String, Double> variables);

    /**
     * составление отформатированной строки.
     *
     * @return отформатированная строка
     */
    public abstract String toString();

    /**
     * подсчет производной.
     *
     * @param var переменная по которой идет дифференцирование
     * @return производная
     */
    public abstract Expression derivative(String var);

    /**
     * вывод выражения.
     */
    public abstract void print();
}
