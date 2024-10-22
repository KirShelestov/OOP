package ru.nsu.shelestov.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AdjacencyListGraphTest {
    private AdjacencyListGraph<String> graph;

    @BeforeEach
    public void setUp() {
        graph = new AdjacencyListGraph<>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge("A", "B", 5, false);
        graph.addEdge("B", "C", 5, false);
    }

    @Test
    public void testAddVertex() {
        graph.addVertex("D");
        assertTrue(graph.getVertices().contains("D"));
    }

    @Test
    public void testRemoveVertex() {
        graph.removeVertex("C");
        assertFalse(graph.getVertices().contains("C"));
        assertFalse(graph.getNeighbors("B").contains("C"));
    }

    @Test
    public void testAddEdge() {
        graph.addEdge("A", "C", 5, false);
        assertTrue(graph.getNeighbors("A").contains("C"));
        assertTrue(graph.getNeighbors("C").contains("A"));
    }

    @Test
    public void testRemoveEdge() {
        graph.removeEdge("A", "B", false);
        assertFalse(graph.getNeighbors("A").contains("B"));
        assertFalse(graph.getNeighbors("B").contains("A"));
    }

    @Test
    public void testGetNeighbors() {
        List<String> neighbors = graph.getNeighbors("B");
        assertEquals(2, neighbors.size());
        assertTrue(neighbors.contains("A"));
        assertTrue(neighbors.contains("C"));
    }

    @Test public void testReadGraph() throws IOException, URISyntaxException {
        Path
                tempFile = Path.of(Objects.requireNonNull(getClass().getResource("/test2.txt")).toURI());

        AdjacencyListGraph<String> newGraph = new AdjacencyListGraph<>();
        newGraph.read(tempFile.toFile(), false, Function.identity());
        assertTrue(newGraph.getVertices().contains("A"));
        assertTrue(newGraph.getVertices().contains("B"));
        assertTrue(newGraph.getVertices().contains("C"));
        assertTrue(newGraph.getNeighbors("A").contains("B"));
        assertTrue(newGraph.getNeighbors("B").contains("C"));
    }

    @Test
    void testToString() {
        graph.addEdge("A", "B", 1.0, true);
        graph.addEdge("B", "C", 2.0, true);

        String expected = "A: {B=1.0}\nB: {A=5.0, C=2.0}\nC: {B=5.0}\n";
        assertEquals(expected, graph.toString());
    }
    @Test
    void testEqualsAndHashCode() {
        AdjacencyListGraph<String> anotherGraph = new AdjacencyListGraph<>();
        anotherGraph.addVertex("A");
        anotherGraph.addVertex("B");
        anotherGraph.addVertex("C");
        anotherGraph.addEdge("A", "B", 5, false);
        anotherGraph.addEdge("B", "C", 5, false);

        assertEquals(graph, anotherGraph);
        assertEquals(graph.hashCode(), anotherGraph.hashCode());
        anotherGraph.addEdge("B", "C", 2.0, true);
        assertNotEquals(graph, anotherGraph);
    }
}
