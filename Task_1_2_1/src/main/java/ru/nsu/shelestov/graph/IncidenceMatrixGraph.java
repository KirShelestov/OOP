package ru.nsu.shelestov.graph;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;

/**
 * Класс для хранеия графа в виде матрицы инцидентности.
 */
public class IncidenceMatrixGraph implements Graph {
    final Map<String, Integer> vertexIndexMap;
    private final List<String> edges;
    final boolean[][] incidenceMatrix;
    private int edgeCount;

    /**
     * Для инициализации матрицы инцидентности создаем хеш-мапу для хранения вершин.
     * Создаем также список для ребер и матрицу инцидентности.
     *
     * @param maxVertices
     * @param maxEdges
     */
    public IncidenceMatrixGraph(int maxVertices, int maxEdges) {
        vertexIndexMap = new HashMap<>();
        edges = new ArrayList<>();
        incidenceMatrix = new boolean[maxVertices][maxEdges];
        edgeCount = 0;
    }

    /**
     * Добавление вершины, если раньше ее не добавляли.
     *
     * @param vertex вершину, которую хотим добавить в граф
     */
    @Override
    public void addVertex(String vertex) {
        if (!vertexIndexMap.containsKey(vertex)) {
            vertexIndexMap.put(vertex, vertexIndexMap.size());
        }
    }

    /**
     * Удаление вершины из хеш-мапы, затем удаление значений из матрицы инцидентности.
     *
     * @param vertex вершина которую хотим удалить
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
     * Добавление ребра с учетом ориентированности.
     * Добавляем ребро в список ребер.
     * Затем заполняем матрицу инцидентности по индексам вершин.
     *
     * @param vertex1 начальная вершина
     * @param vertex2 конечная вершина
     * @param isDirected ориентированность ребра
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
     * Удаление ребра с учетом ориентированности.
     * Удаляем ребро из списка ребер.
     * Удаляем значения из матрицы инцидентности.
     *
     * @param vertex1 начальная вершина
     * @param vertex2 конечная вершина
     * @param isDirected ориентированность ребра
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
     * Получение соседей вершины.
     * Проходимя по хеш-мапе.
     * Затем по индексам вершин проходимся по матрице инцидентности.
     *
     * @param vertex вершина, для которой хотим найти соседей
     * @return список соседей
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
     * Строковое представление графа.
     *
     * @return отформатированная строка
     */
    @Override public String toString() {
        StringBuilder sb = new StringBuilder();

        for (String edge : edges) {
            sb.append(edge).append("\n");
        }

        return sb.toString();
    }

    /**
     * Переопределение сравнения.
     *
     * @param obj объект, с которым сравнивается текущий
     * @return равенство объектов
     */
    @Override public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof IncidenceMatrixGraph)) {
            return false;
        }
        IncidenceMatrixGraph other = (IncidenceMatrixGraph) obj;

        if (this.edgeCount != other.edgeCount || this.vertexIndexMap.size() != other.vertexIndexMap.size()) {
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
     * Переопределение хеш-кода.
     *
     * @return хеш-код графа
     */
    @Override
    public int hashCode() {
        return Objects.hash(vertexIndexMap, edges, Arrays.deepHashCode(incidenceMatrix));
    }

    /**
     * Топологическая сортировка.
     *
     * @return  окончательный порядок топологической сортировки, если сортировка возможна
     */
    @Override public List<String> topologicalSort() {
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
            throw new IllegalStateException("Граф содержит цикл, топологическая сортировка невозможна.");
        }

        return sortedList;
    }
}
