package ru.nsu.shelestov.graph;

import java.util.ArrayList;
import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.Stack;

/**
 * Класс, представляющий реализацию топосорта.
 *
 * @param <T> тип вершин графа
 */
public class TopologicalSorter<T> {

    /**
     * Топосорт графа.
     *
     * @param graph граф, который мы хотим отсортировать.
     * @return отсортированный граф, если возможно.
     */
    public List<T> sort(Graph<T> graph) {
        List<T> sortedList = new ArrayList<>();
        Set<T> visited = new HashSet<>();
        Stack<T> stack = new Stack<>();

        for (T vertex : graph.getVertices()) {
            if (!visited.contains(vertex)) {
                topologicalSortUtil(graph, vertex, visited, stack);
            }
        }

        while (!stack.isEmpty()) {
            sortedList.add(stack.pop());
        }
        return sortedList;
    }

    /**
     * Вспомогательный метод для рекурсивного топосорта.
     *
     * @param graph граф, который хотим отсортировать
     * @param vertex вершина для обработки
     * @param visited множество посещенных вершин
     * @param stack стек для хранения порядка вершин
     */
    private void topologicalSortUtil(Graph<T> graph, T vertex, Set<T> visited, Stack<T> stack) {
        visited.add(vertex);

        for (T neighbor : graph.getNeighbors(vertex)) {
            if (!visited.contains(neighbor)) {
                topologicalSortUtil(graph, neighbor, visited, stack);
            }
        }

        stack.push(vertex);
    }
}
