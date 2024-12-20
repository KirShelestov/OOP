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
     * печать отформатированной строки.
     */
    public void print() {
        System.out.println(this);
    }

    /**
     * подсчет производной.
     *
     * @param var переменная по которой идет дифференцирование
     * @return производная
     */
    public abstract Expression derivative(String var);

    /**
     * Упрощение выражения по заданным правилам.
     *
     * @return упрощённое выражение
     */
    public abstract Expression simplify();
}
