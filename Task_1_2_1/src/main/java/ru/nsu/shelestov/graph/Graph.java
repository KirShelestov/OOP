package ru.nsu.shelestov.graph;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

/**
 * Интерфейс для представления графа.
 */
public interface Graph<T> {

    void addVertex(T vertex);

    void removeVertex(T vertex);

    void addEdge(T vertex1, T vertex2, double weight, boolean isDirected);

    void removeEdge(T vertex1, T vertex2, boolean isDirected);

    List<T> getNeighbors(T vertex);

    List<T> getVertices();

    double getEdgeWeight(T vertex1, T vertex2);

    void read(File file, boolean isDirected, Function<String, T> parse) throws IOException;

}