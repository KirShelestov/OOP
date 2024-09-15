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
 * Класс для хранения графа в виде матрицы смежности.
 */
public class AdjacencyMatrixGraph implements Graph {
    Map<String, Integer> vertexIndexMap;
    boolean[][] adjacencyMatrix;
    private int vertexCount;

    /**
     * Конструктор для инизиализации графа.
     *
     * @param maxVertices максимальное количество возможных вершин в графе.
     */
    public AdjacencyMatrixGraph(int maxVertices) {
        vertexIndexMap = new HashMap<>();
        adjacencyMatrix = new boolean[maxVertices][maxVertices];
        vertexCount = 0;
    }

    /**
     * Метод для добавления вершины в граф.
     *
     * @param vertex вершина, которую нужно добавить в граф
     */
    @Override
    public void addVertex(String vertex) {
        if (!vertexIndexMap.containsKey(vertex)) {
            vertexIndexMap.put(vertex, vertexCount);
            vertexCount++;
        }
    }

    /**
     * Метод для удаления вершины из графа.
     *
     * @param vertex вершина, которую нужно удалить из графа
     */
    @Override
    public void removeVertex(String vertex) {
        if (vertexIndexMap.containsKey(vertex)) {
            int index = vertexIndexMap.remove(vertex);
            for (int i = 0; i < vertexCount; i++) {
                adjacencyMatrix[index][i] = false;
                adjacencyMatrix[i][index] = false;
            }
        }
    }

    /**
     * Метод для добавления ребра в граф.
     *
     * @param vertex1 начальная вершина
     * @param vertex2 конечнаяя вершина
     * @param isDirected ориентированный ли граф
     */
    @Override
    public void addEdge(String vertex1, String vertex2, boolean isDirected) {
        if (vertexIndexMap.containsKey(vertex1) && vertexIndexMap.containsKey(vertex2)) {
            int index1 = vertexIndexMap.get(vertex1);
            int index2 = vertexIndexMap.get(vertex2);
            adjacencyMatrix[index1][index2] = true;
            if (!isDirected) {
                adjacencyMatrix[index2][index1] = true;
            }
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
        if (vertexIndexMap.containsKey(vertex1) && vertexIndexMap.containsKey(vertex2)) {
            int index1 = vertexIndexMap.get(vertex1);
            int index2 = vertexIndexMap.get(vertex2);
            adjacencyMatrix[index1][index2] = false;
            adjacencyMatrix[index2][index1] = false;
            if (!isDirected) {
                adjacencyMatrix[index2][index1] = false;
            }
        }
    }

    /**
     * Метод для получения соседних вершин.
     *
     * @param vertex вершина, для которой ищутся соседние
     * @return список соседний вершин
     */
    @Override
    public List<String> getNeighbors(String vertex) {
        List<String> neighbors = new ArrayList<>();
        if (vertexIndexMap.containsKey(vertex)) {
            int index = vertexIndexMap.get(vertex);
            for (int i = 0; i < vertexCount; i++) {
                if (adjacencyMatrix[index][i]) {
                    neighbors.add(getVertexByIndex(i));
                }
            }
        }
        return neighbors;
    }

    /**
     * Метод для получения вершины по индексу.
     *
     * @param index индекс вершины
     * @return вершина по заданному индексу
     */
    private String getVertexByIndex(int index) {
        for (Map.Entry<String, Integer> entry : vertexIndexMap.entrySet()) {
            if (entry.getValue() == index) {
                return entry.getKey();
            }
        }
        return null;
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
            if (parts.length > 1) {
                for (int i = 1; i < parts.length; i++) {
                    addVertex(parts[i]);
                    addEdge(parts[0], parts[i], isDirected);
                }
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
        for (String vertex : vertexIndexMap.keySet()) {
            sb.append(vertex).append(": ").append(getNeighbors(vertex)).append("\n");
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
        if (!(obj instanceof AdjacencyMatrixGraph)) {
            return false;
        }
        AdjacencyMatrixGraph other = (AdjacencyMatrixGraph) obj;

        if (this.vertexCount != other.vertexCount) {
            return false;
        }

        for (int i = 0; i < vertexCount; i++) {
            for (int j = 0; j < vertexCount; j++) {
                if (this.adjacencyMatrix[i][j] != other.adjacencyMatrix[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Метод для топологической сортировки.
     *
     * @return отсортированный граф
     */
    public List<String> topologicalSort() {
        int[] inDegree = new int[vertexCount];

        for (int i = 0; i < vertexCount; i++) {
            for (int j = 0; j < vertexCount; j++) {
                if (adjacencyMatrix[i][j]) {
                    inDegree[j]++;
                }
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < vertexCount; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        List<String> sortedList = new ArrayList<>();
        while (!queue.isEmpty()) {
            int current = queue.poll();
            sortedList.add(getVertexByIndex(current));

            for (int i = 0; i < vertexCount; i++) {
                if (adjacencyMatrix[current][i]) {
                    inDegree[i]--;
                    if (inDegree[i] == 0) {
                        queue.add(i);
                    }
                }
            }
        }

        if (sortedList.size() != vertexCount) {
            throw new IllegalStateException("Граф содержит цикл," +
                    " топологическая сортировка невозможна.");
        }

        return sortedList;
    }
}

