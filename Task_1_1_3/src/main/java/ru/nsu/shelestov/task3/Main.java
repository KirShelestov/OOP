package ru.nsu.shelestov.task3;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import ru.nsu.shelestov.task3.datatypes.Expression;
import ru.nsu.shelestov.task3.datatypes.Number;
import ru.nsu.shelestov.task3.datatypes.Variable;
import ru.nsu.shelestov.task3.operations.Add;
import ru.nsu.shelestov.task3.operations.Div;
import ru.nsu.shelestov.task3.operations.Mul;
import ru.nsu.shelestov.task3.operations.Sub;


/**
 * Основной класс, где происходит взаимодействие с пользователем.
 */
public class Main {

    /**
     * Метод для парсинга выражения из строки.
     *
     * @param input строка на ввод
     * @return выражение, если нашли его
     */
    public static Expression parseExpression(String input) {
        // Убираем пробелы
        input = input.replaceAll("\\s+", "");

        if (input.matches("-?\\d+(\\.\\d+)?")) { // Число
            return new Number(Double.parseDouble(input));
        } else if (input.matches("[a-zA-Z]+")) { // Переменная
            return new Variable(input);
        } else if (input.startsWith("(") && input.endsWith(")")) { // Удаляем внешние скобки
            input = input.substring(1, input.length() - 1);
            int level = 0;
            int opIndex = -1;

            for (int i = 0; i < input.length(); i++) {
                char c = input.charAt(i);
                if (c == '(') {
                    level++;
                } else if (c == ')') {
                    level--;
                } else if (level == 0 && (c == '+' || c == '-')) { // Найти оператор
                    opIndex = i;
                    break;
                }
            }

            if (opIndex != -1) { // Нашли оператор
                char operator = input.charAt(opIndex);
                Expression left = parseExpression(input.substring(0, opIndex));
                Expression right = parseExpression(input.substring(opIndex + 1));
                return operator == '+' ? new Add(left, right) : new Sub(left, right);
            }

            level = 0;

            for (int i = 0; i < input.length(); i++) {
                char c = input.charAt(i);
                if (c == '(') {
                    level++;
                } else if (c == ')') {
                    level--;
                } else if (level == 0 && (c == '*' || c == '/')) { // Найти оператор
                    opIndex = i;
                    break;
                }
            }

            if (opIndex != -1) { // Нашли оператор
                char operator = input.charAt(opIndex);
                Expression left = parseExpression(input.substring(0, opIndex));
                Expression right = parseExpression(input.substring(opIndex + 1));
                return operator == '*' ? new Mul(left, right) : new Div(left, right);
            }

            throw new IllegalArgumentException("Вы ввели не выражение " + input);
        }

        throw new IllegalArgumentException("Вы ввели не выражение " + input);
    }

    /**
     * в мейне вызываем все наши методы по порядку, соглсано ТЗ.
     *
     * @param args не используется
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try (scanner) {
            String expressionInput = scanner.nextLine();
            Expression expr = parseExpression(expressionInput);

            System.out.print("Выражение: ");
            expr.print();

            System.out.println("\nВведите значения переменных в формате 'x=10; y=13':");
            String variableAssignments = scanner.nextLine();
            Map<String, Double> variables = parseVariableAssignments(variableAssignments);

            double result = expr.evaluate(variables);
            System.out.println("Результат: " + result);
            System.out.println("Производная по переменной: ");
            String varDer = scanner.nextLine();
            expr.derivative(varDer).print();
        } catch (ArithmeticException e) {
            final var errorMessage = "Ошибка арифметики: " + e.getMessage();
            System.err.println(errorMessage);
        } catch (NumberFormatException e) {
            final var errorMessage = "Неправильный формат числа: " + e.getMessage();
            System.err.println(errorMessage);
        } catch (IllegalArgumentException e) {
            final var errorMessage = "Ошибка недопустимого аргумента: " + e.getMessage();
            System.err.println(errorMessage);
        } catch (IllegalStateException e) {
            final var errorMessage = "Ошибка недопустимого состояния: " + e.getMessage();
            System.err.println(errorMessage);
        }
    }


    /**
     * Парсит строку с присвоениями переменных и сохраняет их в Map.
     *
     * @param assignments Строка, содержащая присвоения переменных
     * @return Map, где ключами являются имена переменных (в виде строк),
     *     а значениями - соответствующие значения типа double, присвоенные этим переменным.
     */
    public static Map<String, Double> parseVariableAssignments(String assignments) {
        Map<String, Double> variables = new HashMap<>();
        String[] pairs = assignments.split(";");

        for (String pair : pairs) {
            String[] parts = pair.split("=");
            if (parts.length == 2) {
                String varName = parts[0].trim();
                try {
                    double value = Double.parseDouble(parts[1].trim());
                    variables.put(varName, value);
                } catch (NumberFormatException e) {
                    final var errorMessage = "Неверный формат числа для переменной: " + varName
                           + " со значением: " + parts[1].trim();
                    System.err.println(errorMessage);
                    throw new NumberFormatException(errorMessage);
                }
            } else {
                final var errorMessage = "Неверный формат присваивания: " + pair;
                System.err.println(errorMessage);
                throw new IllegalArgumentException(errorMessage);
            }
        }

        return variables;
    }

}
