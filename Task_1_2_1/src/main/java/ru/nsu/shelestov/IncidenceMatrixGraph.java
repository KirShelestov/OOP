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
 * Класс для хранеия графа в виде матрицы инцидентности.
 */
public class IncidenceMatrixGraph implements Graph {
    Map<String, Integer> vertexIndexMap;
    private final List<String> edges;
    boolean[][] incidenceMatrix;
    private int edgeCount;

    /**
     * Конструктор для инициализации графа.
     *
     * @param maxVertices максимальное количество возможных вершин в графе
     * @param maxEdges максимальное количество возможных ребер в графе
     */
    public IncidenceMatrixGraph(int maxVertices, int maxEdges) {
        vertexIndexMap = new HashMap<>();
        edges = new ArrayList<>();
        incidenceMatrix = new boolean[maxVertices][maxEdges];
        edgeCount = 0;
    }

    /**
     * Метод для добавления вершины в граф.
     *
     * @param vertex вершина, которую нужно добавить в граф
     */
    @Override
    public void addVertex(String vertex) {
        if (!vertexIndexMap.containsKey(vertex)) {
            vertexIndexMap.put(vertex, vertexIndexMap.size());
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
            for (int i = 0; i < edgeCount; i++) {
                incidenceMatrix[index][i] = false;
            }
        }
    }

    /**
     * Метод для добавления ребра в граф.
     *
     * @param vertex1 начальная вершина
     * @param vertex2 конечная вершина
     * @param isDirected ориентированный ли граф
     */
    @Override
    public void addEdge(String vertex1, String vertex2, boolean isDirected) {
        addVertex(vertex1);
        addVertex(vertex2);

        edges.add(vertex1 + "-" + vertex2);
        int edgeIndex = edgeCount++;

        int index1 = vertexIndexMap.get(vertex1);
        int index2 = vertexIndexMap.get(vertex2);

        incidenceMatrix[index1][edgeIndex] = true;
        if (!isDirected) {
            incidenceMatrix[index2][edgeIndex] = true;
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
        String edgeToRemove = vertex1 + "-" + vertex2;

        for (int i = 0; i < edgeCount; i++) {
            if (edges.get(i).equals(edgeToRemove)) {
                edges.remove(i);
                for (int j = 0; j < incidenceMatrix.length; j++) {
                    incidenceMatrix[j][i] = false;
                }
                break;
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
            for (int i = 0; i < edgeCount; i++) {
                if (incidenceMatrix[index][i]) {
                    String[] vertices = edges.get(i).split("-");
                    if (vertices[0].equals(vertex)) {
                        neighbors.add(vertices[1]);
                    } else {
                        neighbors.add(vertices[0]);
                    }
                }
            }
        }

        return neighbors;
    }

    /**
     * Метод для считывания графа из файла и составления графа на основе полученных данных.
     *
     * @param file путь, по которому находится файл
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

        for (String edge : edges) {
            sb.append(edge).append("\n");
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
        if (!(obj instanceof IncidenceMatrixGraph)) {
            return false;
        }
        IncidenceMatrixGraph other = (IncidenceMatrixGraph) obj;

        if (this.edgeCount != other.edgeCount
                || this.vertexIndexMap.size() != other.vertexIndexMap.size()) {
            return false;
        }

        for (int i = 0; i < incidenceMatrix.length; i++) {
            for (int j = 0; j < edgeCount; j++) {
                if (this.incidenceMatrix[i][j] != other.incidenceMatrix[i][j]) {
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
        List<String> sortedList = new ArrayList<>();
        Map<String, Integer> inDegree = new HashMap<>();

        for (String vertex : vertexIndexMap.keySet()) {
            inDegree.put(vertex, 0);
        }

        for (int j = 0; j < edgeCount; j++) {
            String[] vertices = edges.get(j).split("-");
            inDegree.put(vertices[1], inDegree.get(vertices[1]) + 1);
        }

        Queue<String> queue = new LinkedList<>();
        for (String vertex : inDegree.keySet()) {
            if (inDegree.get(vertex) == 0) {
                queue.add(vertex);
            }
        }

        while (!queue.isEmpty()) {
            String currentVertex = queue.poll();
            sortedList.add(currentVertex);

            for (int j = 0; j < edgeCount; j++) {
                String[] vertices = edges.get(j).split("-");
                if (vertices[0].equals(currentVertex)) {
                    String neighbor = vertices[1];
                    inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                    if (inDegree.get(neighbor) == 0) {
                        queue.add(neighbor);
                    }
                }
            }
        }

        if (sortedList.size() != vertexIndexMap.size()) {
            throw new IllegalStateException("Граф содержит цикл, "
                   + "топологическая сортировка невозможна.");
        }

        return sortedList;
    }

}
