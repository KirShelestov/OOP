package ru.nsu.shelestov.task3.datatypes;

import java.util.Map;

/**
 * класс представляет переменную.
 */
public class Variable extends Expression {
    private final String name;

    /**
     * конструктор для переменной.
     *
     * @param name название переменной
     */
    public Variable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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

        Variable name = (Variable) obj; // Приведение типа
        return CharSequence.compare(name.getName(), name.getName()) == 0; // Сравнение значений
    }

    /**
     * Переопределение хэш-кода.
     *
     * @return хэш-код объекта
     */
    @Override
    public int hashCode() {
        return name.hashCode();
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
    @Override
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
}
