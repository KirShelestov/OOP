package ru.nsu.shelestov.graph;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Класс, представляющий граф с использованием списка смежности.
 *
 * @param <T> тип вершин графа
 */
public class AdjacencyListGraph<T> implements Graph<T> {
    private final Map<T, Map<T, Double>> adjacencyList;

    /**
     * Создаем список как пустую хеш-мапу.
     */
    public AdjacencyListGraph() {
        adjacencyList = new HashMap<>();
    }

    /**
     * Добавление вершину в хеш-мапу, если ее не было.
     *
     * @param vertex вершина, которую хотим добавить
     */
    @Override
    public void addVertex(T vertex) {
        adjacencyList.putIfAbsent(vertex, new HashMap<>());
    }

    /**
     * Удаление вершины и связанных ребер.
     *
     * @param vertex вершина, которую хотим удалить
     */
    @Override
    public void removeVertex(T vertex) {
        adjacencyList.remove(vertex);
        for (Map<T, Double> neighbors : adjacencyList.values()) {
            neighbors.remove(vertex);
        }
    }

    /**
     * Добавление ребра в граф с учетом ориентированности.
     * Добавляем начальную вершину и конечную.
     * Добавляем в зависимости от ориентированности одно/два ребра и вес ребер.
     *
     * @param vertex1 начальная вершина
     * @param vertex2 конечная вершина
     * @param weight вес ребра
     * @param isDirected ориентированность ребра
     */
    @Override
    public void addEdge(T vertex1, T vertex2, double weight, boolean isDirected) {
        addVertex(vertex1);
        addVertex(vertex2);
        adjacencyList.get(vertex1).put(vertex2, weight);
        if (!isDirected) {
            adjacencyList.get(vertex2).put(vertex1, weight);
        }
    }

    /**
     * Удаление ребра с учетом ориентированномсти.
     * Удаляем начальную вершину из списка ее соседей.
     * Если ребро ориентированно, то происходит та же операция с конечной.
     *
     * @param vertex1 начальная вершина
     * @param vertex2 конечная вершина
     * @param isDirected ориентированность ребра
     */
    @Override
    public void removeEdge(T vertex1, T vertex2, boolean isDirected) {
        Map<T, Double> neighbors1 = adjacencyList.get(vertex1);
        if (neighbors1 != null) {
            neighbors1.remove(vertex2);
        }
        if (!isDirected) {
            Map<T, Double> neighbors2 = adjacencyList.get(vertex2);
            if (neighbors2 != null) {
                neighbors2.remove(vertex1);
            }
        }
    }

    /**
     * Находим соседей вершины.
     *
     * @param vertex вершина, для которой ищем соседей
     * @return список соседей вершины
     */
    @Override
    public List<T> getNeighbors(T vertex) {
        return new ArrayList<>(adjacencyList.getOrDefault(vertex, new HashMap<>()).keySet());
    }

    /**
     * Получение списка всех вершин графа.
     *
     * @return спсиок всех вершин в графе
     */
    @Override
    public List<T> getVertices() {
        return new ArrayList<>(adjacencyList.keySet());
    }

    /**
     * Получение веса ребра.
     *
     * @param vertex1 начальная вершина
     * @param vertex2 конечная вершина
     * @return вес ребра между двумя заданнами вершинами
     */
    @Override
    public double getEdgeWeight(T vertex1, T vertex2) {
        return adjacencyList.getOrDefault(vertex1, new HashMap<>()).getOrDefault(vertex2,
                Double.NaN);
    }

    /**
     * Чтение графа из файла.
     *
     * @param file файл, из которого считываем граф
     * @param isDirected ориентированность графа
     * @param parse функция для преобразования строк в вершины
     * @throws IOException ошибка чтения файла
     */
    @Override
    public void read(File file, boolean isDirected, Function<String, T> parse) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath());
        for (String line : lines) {
            String[] parts = line.split(" ");
            T vertex1 = parse.apply(parts[0]);
            addVertex(vertex1);
            for (int i = 1; i < parts.length - 1; i += 2) {
                T vertex2 = parse.apply(parts[i]);
                double weight = Double.parseDouble(parts[i + 1]);
                addEdge(vertex1, vertex2, weight, isDirected);
            }
        }
    }

    /**
     * Строковое представление графа.
     *
     * @return строковое представление графа
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<T, Map<T, Double>> entry : adjacencyList.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Переопределение сравнения графа.
     *
     * @param obj объект, с которым сравнивается текущий
     * @return равенство объектов
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AdjacencyListGraph)) {
            return false;
        }
        AdjacencyListGraph<?> other = (AdjacencyListGraph<?>) obj;
        return adjacencyList.equals(other.adjacencyList);
    }

    /**
     * Переопределение хеш-кода.
     *
     * @return хеш-код
     */
    @Override public int hashCode() {
        return Objects.hash(adjacencyList);
    }
}