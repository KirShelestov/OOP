package ru.nsu.shelestov.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Класс для тестирования матрицы смежности.
 */
public class AdjacencyMatrixGraphTest {

    /**
     * Тест на добавления вершины.
     */
    @Test
    public void testAddVertex() {
        AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph(10);
        graph.addVertex("A");
        assertTrue(graph.vertexIndexMap.containsKey("A"));
    }

    /**
     * Тест на удаления вершины.
     */
    @Test
    public void testRemoveVertex() {
        AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph(10);
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B", false);
        graph.removeVertex("A");
        assertFalse(graph.vertexIndexMap.containsKey("A"));
        assertFalse(graph.adjacencyMatrix[0][1]);
        assertFalse(graph.adjacencyMatrix[1][0]);
    }

    /**
     * Тест на добавление ребра.
     */
    @Test
    public void testAddEdge() {
        AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph(10);
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B", false);
        assertTrue(graph.adjacencyMatrix[0][1]);
        assertTrue(graph.adjacencyMatrix[1][0]);
    }

    /**
     * Тест на удаление ребра.
     */
    @Test
    public void testRemoveEdge() {
        AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph(10);
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B", false);
        graph.removeEdge("A", "B", false);
        assertFalse(graph.adjacencyMatrix[0][1]);
        assertFalse(graph.adjacencyMatrix[1][0]);
    }

    /**
     * Тест на поиск соседних вершин.
     */
    @Test
    public void testGetNeighbors() {
        AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph(10);
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B", false);
        List<String> neighbors = graph.getNeighbors("A");
        assertEquals(1, neighbors.size());
        assertTrue(neighbors.contains("B"));
    }

    /**
     * Тест на считывание графа из файла.
     *
     * @throws Exception ошибка при считывании из файла
     */
    @Test
    public void testReadFromFile() throws Exception {
        AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph(10);
        File file = new File("test.txt");
        graph.readFromFile(file, false);
        assertEquals(5, graph.vertexIndexMap.size());
        assertTrue(graph.adjacencyMatrix[1][0]);
        assertTrue(graph.adjacencyMatrix[0][2]);
        assertTrue(graph.adjacencyMatrix[1][3]);
        assertTrue(graph.adjacencyMatrix[2][4]);
    }

    /**
     * Тест на правильное форматирование строки, которая представляет собой граф.
     */
    @Test
    public void testToString() {
        AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph(10);
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B", false);
        String expected = "A: [B]\nB: [A]\n";
        assertEquals(expected, graph.toString());
    }

    /**
     * Тест на сравнение графов.
     */
    @Test
    public void testEquals() {
        AdjacencyMatrixGraph graph1 = new AdjacencyMatrixGraph(10);
        graph1.addVertex("A");
        graph1.addVertex("B");
        graph1.addEdge("A", "B", false);
        AdjacencyMatrixGraph graph2 = new AdjacencyMatrixGraph(10);
        graph2.addVertex("A");
        graph2.addVertex("B");
        graph2.addEdge("A", "B", false);
        assertEquals(graph1, graph2);
    }

    /**
     * Тест на правильность топологической сортировки.
     */
    @Test
    public void testTopologicalSort() {
        AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph(10);
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");
        graph.addVertex("E");
        graph.addEdge("A", "B", true);
        graph.addEdge("A", "C", true);
        graph.addEdge("B", "D", true);
        graph.addEdge("C", "E", true);
        List<String> sortedOrder = graph.topologicalSort();
        assertEquals(5, sortedOrder.size());
        assertEquals("A", ((List<?>) sortedOrder).get(0));
        assertEquals("B", sortedOrder.get(1));
        assertEquals("C", sortedOrder.get(2));
        assertEquals("D", sortedOrder.get(3));
        assertEquals("E", sortedOrder.get(4));
    }

    /**
     * Тест на нахождения цикла при топологической сортировке.
     */
    @Test
    public void testTopologicalSortWithCycle() {
        AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph(10);
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge("A", "B", true);
        graph.addEdge("B", "C", true);
        graph.addEdge("C", "A", true);
        assertThrows(IllegalStateException.class, graph::topologicalSort);
    }
}
