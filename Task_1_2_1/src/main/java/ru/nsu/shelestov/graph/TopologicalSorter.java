package ru.nsu.shelestov.graph;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
        Deque<T> deque = new LinkedList<>(); 

        for (T vertex : graph.getVertices()) {
            if (!visited.contains(vertex)) {
                topologicalSortUtil(graph, vertex, visited, deque);
            }
        }

        while (!deque.isEmpty()) {
            sortedList.add(deque.pollLast());
        }
        return sortedList;
    }

    /**
     * Вспомогательный метод для рекурсивного топосорта.
     *
     * @param graph граф, который хотим отсортировать
     * @param vertex вершина для обработки
     * @param visited множество посещенных вершин
     * @param deque стек для хранения порядка вершин
     */
    private void topologicalSortUtil(Graph<T> graph, T vertex, Set<T> visited, Deque<T> deque) {
        visited.add(vertex);

        for (T neighbor : graph.getNeighbors(vertex)) {
            if (!visited.contains(neighbor)) {
                topologicalSortUtil(graph, neighbor, visited, deque);
            }
        }

        deque.addLast(vertex);
    }
}