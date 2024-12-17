package ru.nsu.shelestov.task3;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
     * Метод для парсинга выражения из строки без скобок.
     *
     * @param input строка на ввод
     * @return выражение, если нашли его
     */
    public static Expression parseWithout(String input) {
        input = input.replaceAll("\\s+", "");

        if (input.matches("-?\\d+(\\.\\d+)?")) {
            return new Number(Double.parseDouble(input));
        } else if (input.matches("[a-zA-Z]+")) {
            return new Variable(input);
        }
        if (input.startsWith("(") && input.endsWith(")")) {
            input = input.substring(1, input.length() - 1);
        }

        List<Expression> expressions = new ArrayList<>();
        List<Character> operators = new ArrayList<>();
        int level = 0;
        StringBuilder current = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '(') {
                level++;
                current.append(c);
            } else if (c == ')') {
                level--;
                current.append(c);
            } else if (level == 0 && (c == '+' || c == '-' || c == '*' || c == '/')) {
                if (current.length() > 0) {
                    expressions.add(parseWithout(current.toString()));
                    current.setLength(0);
                }
                operators.add(c);
            } else {
                current.append(c);
            }
        }

        if (current.length() > 0) {
            expressions.add(parseWithout(current.toString()));
        }

        while (!operators.isEmpty()) {
            int opIndex = -1;
            char operator = ' ';
            for (int j = 0; j < operators.size(); j++) {
                char op = operators.get(j);
                if (op == '*' || op == '/') {
                    opIndex = j;
                    operator = op;
                    break;
                }
            }

            if (opIndex == -1) {
                for (int j = 0; j < operators.size(); j++) {
                    char op = operators.get(j);
                    if (op == '+' || op == '-') {
                        opIndex = j;
                        operator = op;
                        break;
                    }
                }
            }

            if (opIndex != -1) {

                Expression left = expressions.get(opIndex);
                left = left;
                Expression right = expressions.get(opIndex + 1);
                right = right;
                expressions.remove(opIndex);
                expressions.remove(opIndex);
                operators.remove(opIndex);
                Expression newExpr = switch (operator) {
                    case '+' -> new Add(left, right);
                    case '-' -> new Sub(left, right);
                    case '*' -> new Mul(left, right);
                    case '/' -> new Div(left, right);
                    default -> throw new IllegalArgumentException("Непонятен: " + operator);
                };
                expressions.add(opIndex, newExpr);
            }
        }

        if (expressions.size() != 1) {
            throw new IllegalArgumentException("Ошибка разбора выражения: " + input);
        }

        return expressions.get(0);
    }

    /**
     * Метод для парсинга выражения из строки.
     *
     * @param input строка на ввод
     * @return выражение, если нашли его
     */
    public static Expression parseExpression(String input) {
        input = input.replaceAll("\\s+", "");

        if (input.matches("-?\\d+(\\.\\d+)?")) {
            return new Number(Double.parseDouble(input));
        } else if (input.matches("[a-zA-Z]+")) {
            return new Variable(input);
        } else if (input.startsWith("(") && input.endsWith(")")) {
            input = input.substring(1, input.length() - 1);
            int level = 0;
            int opIndex = -1;

            for (int i = 0; i < input.length(); i++) {
                char c = input.charAt(i);
                if (c == '(') {
                    level++;
                } else if (c == ')') {
                    level--;
                } else if (level == 0 && (c == '+' || c == '-')) {
                    opIndex = i;
                    break;
                }
            }

            if (opIndex != -1) {
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
                } else if (level == 0 && (c == '*' || c == '/')) {
                    opIndex = i;
                    break;
                }
            }

            if (opIndex != -1) {
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
            System.out.println("Введите математическое выражение:");
            String expressionInput = scanner.nextLine();
            Expression expr = parseWithout(expressionInput);

            System.out.print("Выражение: ");
            expr.print();

            System.out.println("\nУпрощённое выражение: ");
            expr.simplify().print();

            System.out.println("\nВведите значения переменных в формате 'x=10; y=13':");
            String variableAssignments = scanner.nextLine();
            Map<String, Double> variables = parseVariableAssignments(variableAssignments);

            double result = expr.evaluate(variables);
            System.out.println("Результат: " + result);
            System.out.println("Производная по переменной: ");
            String varDer = scanner.nextLine();
            expr.derivative(varDer).print();
        } catch (ArithmeticException e) {
            System.err.println("Ошибка арифметики: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Неправильный формат числа: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка недопустимого аргумента: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println("Ошибка недопустимого состояния: " + e.getMessage());
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
