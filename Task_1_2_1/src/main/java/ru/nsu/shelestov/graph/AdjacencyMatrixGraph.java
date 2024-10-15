package ru.nsu.shelestov.graph;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;

/**
 * Класс для хранения графа в виде матрицы смежности.
 */
public class AdjacencyMatrixGraph implements Graph {
    final Map<String, Integer> vertexIndexMap;
    final boolean[][] adjacencyMatrix;
    private int vertexCount;

    /**
     * Создаем хеш-мапу для хранения вершин и матрицу смежности.
     *
     * @param maxVertices максимальное количество возможных вершин
     */
    public AdjacencyMatrixGraph(int maxVertices) {
        vertexIndexMap = new HashMap<>();
        adjacencyMatrix = new boolean[maxVertices][maxVertices];
        vertexCount = 0;
    }

    /**
     * Добавление вершины в хеш-мапу, если такой вершины еще нету.
     *
     * @param vertex вершина, которую хотим добавить
     */
    @Override public void addVertex(String vertex) {
        if (!vertexIndexMap.containsKey(vertex)) {
            vertexIndexMap.put(vertex, vertexCount);
            vertexCount++;
        }
    }

    /**
     * Удаление вершины из хеш-мапы и связнности этой вершины с другими в матрице.
     *
     * @param vertex вершина, которую хотим удалить
     */
    @Override public void removeVertex(String vertex) {
        if (vertexIndexMap.containsKey(vertex)) {
            int index = vertexIndexMap.remove(vertex);
            for (int i = 0; i < vertexCount; i++) {
                adjacencyMatrix[index][i] = false;
                adjacencyMatrix[i][index] = false;
            }
        }
    }

    /**
     * Добавление ребра с учетом ориентированности.
     * Сначала получаем индексы вершины из хеш-мапы, затем с учетом ориентированности заполняем матрицу.
     *
     * @param vertex1 начальная вершина
     * @param vertex2 конечная вершина
     * @param isDirected ориентированность ребра
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
     * Удаление ребра из графа с учетом ориентированности.
     * Сначала получаем индексы вершины из хеш-мапы.
     * Затем с учетом ориентированности удаляем значения из матрицы.
     *
     * @param vertex1 начальная вершина
     * @param vertex2 конечная вершина
     * @param isDirected ориентированность ребра
     */
    @Override
    public void removeEdge(String vertex1, String vertex2, boolean isDirected) {
        if (vertexIndexMap.containsKey(vertex1) && vertexIndexMap.containsKey(vertex2)) {
            int index1 = vertexIndexMap.get(vertex1);
            int index2 = vertexIndexMap.get(vertex2);
            adjacencyMatrix[index1][index2] = false;
            if (!isDirected) {
                adjacencyMatrix[index2][index1] = false;
            }
        }
    }

    /**
     * Получаем соседей вершины в виде списка.
     *
     * @param vertex вершина, для которой хотим найти соседей
     * @return список соседей вершины
     */
    @Override public List<String> getNeighbors(String vertex) {
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
     * Получение вершины по индексу в хеш-мапе.
     *
     * @param index индекс вершины
     * @return вершина, если существует по заданному индексу
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
            if (parts.length > 1) {
                for (int i = 1; i < parts.length; i++) {
                    addVertex(parts[i]);
                    addEdge(parts[0], parts[i], isDirected);
                }
            }
        }
    }

    /**
     * Строковое представление графа, как ключ:значение.
     *
     * @return отформатированная строка
     */
    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String vertex : vertexIndexMap.keySet()) {
            sb.append(vertex).append(": ").append(getNeighbors(vertex)).append("\n");
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
     * Переопределение хеш-кода.
     *
     * @return хеш-код графа
     */
    @Override public int hashCode() {
        return Objects.hash(vertexIndexMap, Arrays.deepHashCode(adjacencyMatrix));
    }

    /**
     * Топологическая сортировка.
     *
     * @return  окончательный порядок топологической сортировки, если сортировка возможна
     */
    @Override public List<String> topologicalSort() {
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
            throw new IllegalStateException("Граф содержит цикл, топологическая сортировка невозможна.");
        }

        return sortedList;
    }
}
