package ru.nsu.shelestov;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Класс для хранения графа в виде списка смежности.
 */
public class AdjacencyListGraph implements Graph {
    final Map<String, List<String>> adjacencyList;
    private int vertexCount;

    /**
     * Конструктор для списка смежности.
     */
    public AdjacencyListGraph() {
        adjacencyList = new HashMap<>();
    }

    /**
     * Метод для добавления вершины в граф.
     *
     * @param vertex вершина, которую нужно добавить в граф
     */
    @Override
    public void addVertex(String vertex) {
        adjacencyList.putIfAbsent(vertex, new ArrayList<>());
    }

    /**
     * Метод для удаления вершины из графа.
     *
     * @param vertex вершина, которую нужно удалить из графа
     */
    @Override
    public void removeVertex(String vertex) {
        adjacencyList.remove(vertex);
        for (List<String> neighbors : adjacencyList.values()) {
            neighbors.remove(vertex);
        }
    }

    /**
     * Метод для добавления неориентированного ребра в граф.
     *
     * @param vertex1 начальная веришна
     * @param vertex2 конечная вершина
     * @param isDirected ориентированный ли граф
     */
    @Override
    public void addEdge(String vertex1, String vertex2, boolean isDirected) {
        addVertex(vertex1);
        addVertex(vertex2);
        adjacencyList.get(vertex1).add(vertex2);
        if (!isDirected) {
            adjacencyList.get(vertex2).add(vertex1);
        }
    }

    /**
     * Метод для удаления ребра из графа.
     *
     * @param vertex1 начальная вершина
     * @param vertex2 конечная вершина
     * @param isDirected ориентированный ли граф
     */
    @Override
    public void removeEdge(String vertex1, String vertex2, boolean isDirected) {
        List<String> neighbors1 = adjacencyList.get(vertex1);
        List<String> neighbors2 = adjacencyList.get(vertex2);
        if (neighbors1 != null) {
            neighbors1.remove(vertex2);
        }
        if (!isDirected) {
            if (neighbors2 != null) {
                neighbors2.remove(vertex1);
            }
        }
    }

    /**
     * Метод для получения всех соседей вершины.
     *
     * @param vertex вершина, для которой мы хотим найти соседей
     * @return список соседей вершины
     */
    @Override
    public List<String> getNeighbors(String vertex) {
        return adjacencyList.getOrDefault(vertex, new ArrayList<>());
    }

    /**
     * Метод для считывания графа из файла и составления графа на основе полученных данных.
     *
     * @param file путь, по которому находится файл
     * @param isDirected ориентированный ли граф
     * @throws IOException ошибка ввода-вывода, если указан неверный путь
     */
    @Override
    public void readFromFile(File file, boolean isDirected) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath());
        for (String line : lines) {
            String[] parts = line.split(" ");
            addVertex(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                addEdge(parts[0], parts[i], isDirected);
            }
        }
    }

    /**
     * Метод для строкового представления графа.
     *
     * @return строка в отформатированном виде
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : adjacencyList.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Метод для переопределения равенства между объектами.
     *
     * @param obj объект, с которым сравнивается текущим объект класса
     * @return true, если объекты равны, иначе false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AdjacencyListGraph)) {
            return false;
        }
        AdjacencyListGraph other = (AdjacencyListGraph) obj;

        return this.adjacencyList.equals(other.adjacencyList);
    }

    /**
     * Метод для топологической сортировки.
     *
     * @return отсортированный граф
     */
    public List<String> topologicalSort() {
        Map<String, Integer> inDegree = new HashMap<>();
        for (String vertex : adjacencyList.keySet()) {
            inDegree.put(vertex, 0);
        }

        for (String vertex : adjacencyList.keySet()) {
            for (String neighbor : adjacencyList.get(vertex)) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
        }

        Queue<String> queue = new LinkedList<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        List<String> sortedOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            String current = queue.poll();
            sortedOrder.add(current);

            for (String neighbor : adjacencyList.get(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        if (sortedOrder.size() != adjacencyList.size()) {
            throw new IllegalStateException("Граф содержит цикл; " +
                    "топологическая сортировка невозможна.");
        }

        return sortedOrder;
    }

}


