package ru.nsu.shelestov;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Интерфейс для последующей реализации раздичных способов хранения графа.
 */
public interface Graph {
    void addVertex(String vertex);
    void removeVertex(String vertex);
    void addEdge(String vertex1, String vertex2, boolean isDirected);
    void removeEdge(String vertex1, String vertex2, boolean isDirected);
    List<String> getNeighbors(String vertex);
    void readFromFile(File file, boolean isDirected) throws IOException;
}

