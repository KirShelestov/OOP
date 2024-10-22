package ru.nsu.shelestov.graph;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TopologicalSorterTest {

    private TopologicalSorter<String> sorter;

    @BeforeEach
    void setUp() {
        sorter = new TopologicalSorter<>();
    }

    @Test
    void testSimpleGraph() {
        Graph<String> graph = new AdjacencyListGraph<>();
        graph.addEdge("A", "B", 1.0, true);
        graph.addEdge("B", "C", 1.0, true);

        List<String> sorted = sorter.sort(graph);
        List<String> expected = Arrays.asList("A", "B", "C");

        assertEquals(expected, sorted);
    }

    @Test
    void testGraphWithMultipleValidSorts() {
        Graph<String> graph = new AdjacencyListGraph<>();
        graph.addEdge("A", "B", 1.0, true);
        graph.addEdge("A", "C", 1.0, true);
        graph.addEdge("B", "D", 1.0, true);
        graph.addEdge("C", "D", 1.0, true);

        List<String> sorted = sorter.sort(graph);
        assert (sorted.contains("B"));
        assert (sorted.contains("C"));
        assert (sorted.contains("D"));
    }

    @Test
    void testEmptyGraph () {
        Graph<String> graph = new AdjacencyListGraph<>();

        List<String> sorted = sorter.sort(graph);
        List<String> expected = Arrays.asList();
        assertEquals(expected, sorted);
    }
}
