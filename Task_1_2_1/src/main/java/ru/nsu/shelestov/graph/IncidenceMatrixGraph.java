package ru.nsu.shelestov.graph;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Класс, представляющий граф с использованием матрицы инцидентности.
 *
 * @param <T> тип вершин графа
 */
public class IncidenceMatrixGraph<T> implements Graph<T> {
    private final List<T> vertices;
    private final List<List<Boolean>> incidenceMatrix;
    private final List<Double> edgeWeights;

    /**
     * Инициализация матрицы инцедентности.
     *
     * @param size размер матрицы
     */
    public IncidenceMatrixGraph(int size) {
        vertices = new ArrayList<>(size);
        incidenceMatrix = new ArrayList<>();
        edgeWeights = new ArrayList<>();
    }

    /**
     * Добавление вершины и заполнение матрицы смежности.
     *
     * @param vertex вершина, которую хотим добавить
     */
    @Override
    public void addVertex(T vertex) {
        if (!vertices.contains(vertex)) {
            vertices.add(vertex);
            for (List<Boolean> row : incidenceMatrix) {
                row.add(false);
            }
        }
    }

    /**
     * Удаление вершины.
     *
     * @param vertex вершина, которую хотим удалить
     */
    @Override
    public void removeVertex(T vertex) {
        int index = vertices.indexOf(vertex);
        if (index != -1) {
            vertices.remove(index);
            incidenceMatrix.remove(index - 1);
            for (List<Boolean> row : incidenceMatrix) {
                row.remove(index);
            }
        }
    }

    /**
     * Добавление ребра между вершинами.
     *
     * @param vertex1 начальная вершина
     * @param vertex2 конечная вершина
     * @param weight вес ребра
     * @param isDirected ориентированность ребра
     */
    @Override public void addEdge(T vertex1, T vertex2, double weight, boolean isDirected) {
        int index1 = vertices.indexOf(vertex1);
        int index2 = vertices.indexOf(vertex2);
        if (index1 != -1 && index2 != -1) {
            List<Boolean> edge = new ArrayList<>(Collections.nCopies(vertices.size(), false));
            edge.set(index1, true);
            edge.set(index2, true);
            incidenceMatrix.add(edge);
            edgeWeights.add(weight);
            if (isDirected) {
                edge.set(index2, false);
            }
        }
    }

    /**
     * Удаление ребра между указанными вершинами.
     *
     * @param vertex1 начальная вершина
     * @param vertex2 конечная вершина
     * @param isDirected ориентированность ребра
     */
    @Override
    public void removeEdge(T vertex1, T vertex2, boolean isDirected) {
        int index1 = vertices.indexOf(vertex1);
        int index2 = vertices.indexOf(vertex2);
        incidenceMatrix.removeIf(edge -> edge.get(index1) && edge.get(index2));
        edgeWeights.removeIf(weight -> weight == getEdgeWeight(vertex1, vertex2));
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
        for (List<Boolean> edge : incidenceMatrix) {
            if (edge.get(index)) {
                for (int i = 0; i < edge.size(); i++) {
                    if (edge.get(i) && i != index) {
                        neighbors.add(vertices.get(i));
                    }
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
        for (int i = 0; i < incidenceMatrix.size(); i++) {
            List<Boolean> edge = incidenceMatrix.get(i);
            if (edge.get(index1) && edge.get(index2)) {
                return edgeWeights.get(i);
            }
        }
        return Double.NaN;
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
        for (int i = 0; i < incidenceMatrix.size(); i++) {
            sb.append("Edge ").append(i).append(": ");
            for (int j = 0; j < vertices.size(); j++) {
                if (incidenceMatrix.get(i).get(j)) {
                    sb.append(vertices.get(j)).append(" (").append(edgeWeights.get(i)).append(") ");
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
        if (!(obj instanceof IncidenceMatrixGraph)) {
            return false;
        }
        IncidenceMatrixGraph<?> other = (IncidenceMatrixGraph<?>) obj;
        return vertices.equals(other.vertices) && incidenceMatrix.equals(other.incidenceMatrix)
                && edgeWeights.equals(other.edgeWeights);
    }

    /**
     * Переопределение хеш-кода.
     *
     * @return хеш-код
     */
    @Override public int hashCode() {
        return Objects.hash(vertices, incidenceMatrix, edgeWeights);
    }
}