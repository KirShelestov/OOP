package ru.nsu.shelestov.graph;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AdjacencyMatrixGraphTest {
    private AdjacencyMatrixGraph<String> graph;

    @BeforeEach
    public void setUp() {
        graph = new AdjacencyMatrixGraph<>(3);
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge("A", "B", 5,false);
        graph.addEdge("B", "C", 5,false);
    }

    @Test
    public void testAddVertex() {
        graph.addVertex("D");
        assertTrue(graph.getVertices().contains("D"));
    }

    @Test public void testRemoveVertex() {
        graph.removeVertex("C");
        assertFalse(graph.getVertices().contains("C"));
    }

    @Test public void testAddEdge() {
        graph.addEdge("A", "C", 5,false);
        assertTrue(graph.getNeighbors("A").contains("C"));
        assertTrue(graph.getNeighbors("C").contains("A"));
    }

    @Test public void testRemoveEdge() {
        graph.removeEdge("A", "B", false);
        assertFalse(graph.getNeighbors("A").contains("B"));
        assertFalse(graph.getNeighbors("B").contains("A"));
    }

    @Test public void testGetNeighbors() {
        List<String> neighbors = graph.getNeighbors("B");
        assertEquals(2, neighbors.size());
        assertTrue(neighbors.contains("A"));
        assertTrue(neighbors.contains("C"));
    }
}

