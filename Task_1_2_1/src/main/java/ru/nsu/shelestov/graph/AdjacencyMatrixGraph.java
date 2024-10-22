package ru.nsu.shelestov.graph;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Класс, представляющий граф с использованием матрицы смежности.
 *
 * @param <T> тип вершин графа
 */
public class AdjacencyMatrixGraph<T> implements Graph<T> {
    private final List<T> vertices;
    private final double[][] adjacencyMatrix;

    /**
     * Инициализация матрицы смежности.
     * Создание списка для вершин.
     * Создание матрицы и заполняем ее.
     *
     * @param size размер матрицы.
     */
    public AdjacencyMatrixGraph(int size) {
        vertices = new ArrayList<>(size);
        adjacencyMatrix = new double[size][size];
        for (int i = 0; i < size; i++) {
            Arrays.fill(adjacencyMatrix[i], Double.NaN);
        }
    }

    /**
     * Добавление вершины, если ее не было.
     *
     * @param vertex вершина, которуб хотим добавить.
     */
    @Override
    public void addVertex(T vertex) {
        if (!vertices.contains(vertex)) {
            vertices.add(vertex);
        }
    }

    /**
     * Удаление вершины.
     * По индексу проходимся по матрице и зануляем значения.
     *
     * @param vertex вершина, которую хотим удалить
     */
    @Override
    public void removeVertex(T vertex) {
        int index = vertices.indexOf(vertex);
        if (index != -1) {
            vertices.remove(index);
            for (int i = 0; i < adjacencyMatrix.length; i++) {
                adjacencyMatrix[index][i] = Double.NaN;
                adjacencyMatrix[i][index] = Double.NaN;
            }
        }
    }

    /**
     * Добавление ребра.
     * По индексам вершин заполняем матрицу.
     *
     * @param vertex1 начальная вершина
     * @param vertex2 конечная вершина
     * @param weight вес ребра
     * @param isDirected ориентированность ребра
     */
    @Override
    public void addEdge(T vertex1, T vertex2, double weight, boolean isDirected) {
        int index1 = vertices.indexOf(vertex1);
        int index2 = vertices.indexOf(vertex2);
        if (index1 != -1 && index2 != -1) {
            adjacencyMatrix[index1][index2] = weight;
            if (!isDirected) {
                adjacencyMatrix[index2][index1] = weight;
            }
        }
    }

    /**
     * Удаление ребра.
     * По индексам проходимся по матрице и зануляем значения.
     *
     * @param vertex1 начальная вершина
     * @param vertex2 конечная вершина
     * @param isDirected ориентированность ребра
     */
    @Override
    public void removeEdge(T vertex1, T vertex2, boolean isDirected) {
        int index1 = vertices.indexOf(vertex1);
        int index2 = vertices.indexOf(vertex2);
        if (index1 != -1 && index2 != -1) {
            adjacencyMatrix[index1][index2] = Double.NaN;
            if (!isDirected) {
                adjacencyMatrix[index2][index1] = Double.NaN;
            }
        }
    }

    /**
     * Получение соседей.
     *
     * @param vertex веришна, для которой ищем соседей.
     * @return список соседей вершины
     */
    @Override
    public List<T> getNeighbors(T vertex) {
        List<T> neighbors = new ArrayList<>();
        int index = vertices.indexOf(vertex);
        if (index != -1) {
            for (int i = 0; i < adjacencyMatrix.length; i++) {
                if (!Double.isNaN(adjacencyMatrix[index][i])) {
                    neighbors.add(vertices.get(i));
                }
            }
        }
        return neighbors;
    }

    /**
     * Получение спискаа всех вершин в графе.
     *
     * @return список всех вершин
     */
    @Override
    public List<T> getVertices() {
        return new ArrayList<>(vertices);
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
        int index1 = vertices.indexOf(vertex1);
        int index2 = vertices.indexOf(vertex2);
        return (index1 != -1 && index2 != -1) ? adjacencyMatrix[index1][index2] : Double.NaN;
    }

    /**
     * Чтение графа из файла
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
        for (int i = 0; i < vertices.size(); i++) {
            sb.append(vertices.get(i)).append(": ");
            for (int j = 0; j < vertices.size(); j++) {
                if (!Double.isNaN(adjacencyMatrix[i][j])) {
                    sb.append(vertices.get(j)).append(" (").append(adjacencyMatrix[i][j]).append(") ");
                }
            }
            sb.append("\n");
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
        if (!(obj instanceof AdjacencyMatrixGraph)) {
            return false;
        }
        AdjacencyMatrixGraph<?> other = (AdjacencyMatrixGraph<?>) obj;
        return Arrays.deepEquals(adjacencyMatrix, other.adjacencyMatrix) && vertices.equals(other.vertices);
    }

    /**
     * Переопределение хеш-кода.
     *
     * @return хеш-код
     */
    @Override public int hashCode() {
        return Objects.hash(vertices, Arrays.deepHashCode(adjacencyMatrix));
    }
}