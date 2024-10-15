package ru.nsu.shelestov.graph;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;

/**
 * Класс для хранения графа в виде списка смежности.
 */
public class AdjacencyListGraph implements Graph {
    final Map<String, List<String>> adjacencyList;

    /**
     * Создание изначально пустого списка смежности в виде хеш-мапы.
     */
    public AdjacencyListGraph() {
        adjacencyList = new HashMap<>();
    }

    /**
     * Добавлние вершины как пары название вершины - список соседних вершин.
     *
     * @param vertex вершина, которую добавляем в граф.
     */
    @Override
    public void addVertex(String vertex) {
        adjacencyList.putIfAbsent(vertex, new ArrayList<>());
    }

    /**
     * Удаление вершины, сначала саму вершину удаляем, затем вершины из списка смежности.
     *
     * @param vertex вершина, которую удаляем.
     */
    @Override
    public void removeVertex(String vertex) {
        adjacencyList.remove(vertex);
        for (List<String> neighbors : adjacencyList.values()) {
            neighbors.remove(vertex);
        }
    }

    /**
     * Добавляем ребро с учетом ориентированности.
     *
     * @param vertex1 начальная вершина
     * @param vertex2 конечная вершина
     * @param isDirected ориентированность ребра
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
     * Удаляем ребро, перед этим проверяем существуют ли вершины у ребра.
     *
     * @param vertex1 начальная вершина
     * @param vertex2 конечная вершина
     * @param isDirected ориентированность ребра, от этого зависит удаление конечной вершины
     */
    @Override
    public void removeEdge(String vertex1, String vertex2, boolean isDirected) {
        List<String> neighbors1 = adjacencyList.get(vertex1);
        if (neighbors1 != null) {
            neighbors1.remove(vertex2);
        }
        if (!isDirected) {
            List<String> neighbors2 = adjacencyList.get(vertex2);
            if (neighbors2 != null) {
                neighbors2.remove(vertex1);
            }
        }
    }

    /**
     * Получаем соседей вершины в виде списка.
     *
     * @param vertex вершина, для которой хотим найти соседей
     * @return список соседей вершины
     */
    @Override
    public List<String> getNeighbors(String vertex) {
        return adjacencyList.getOrDefault(vertex, new ArrayList<>());
    }

    /**
     * Чтение графа из файла, сначала идет вершина в строке, затем все ее соседи.
     *
     * @param file путь, по котору находится файл с графом
     * @param isDirected ориентированность всего графа
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
     * Строковое представление графа, как ключ:значение в хеш-мапе.
     *
     * @return отформатированная строка
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
     * Переопределение сравнения.
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
        AdjacencyListGraph other = (AdjacencyListGraph) obj;
        return this.adjacencyList.equals(other.adjacencyList);
    }

    /**
     * Переопределение хеш-кода.
     *
     * @return хеш-код графа
     */
    @Override
    public int hashCode() {
        return Objects.hash(adjacencyList);
    }

    /**
     * Топологическая сортировка.
     *
     * @return  окончательный порядок топологической сортировки, если сортировка возможна
     */
    @Override
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
            throw new IllegalStateException("Граф содержит цикл; сортировка невозможна.");
        }

        return sortedOrder;
    }

}
