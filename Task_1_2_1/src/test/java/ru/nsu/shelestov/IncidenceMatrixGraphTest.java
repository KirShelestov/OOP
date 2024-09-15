package ru.nsu.shelestov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.List;

/**
 * Класс для тестирования матрица инцедентности.
 */
public class IncidenceMatrixGraphTest {

    /**
     * Тест на добавления вершины.
     */
    @Test
    public void testAddVertex() {
        IncidenceMatrixGraph graph = new IncidenceMatrixGraph(10, 10);
        graph.addVertex("A");
        assertTrue(graph.vertexIndexMap.containsKey("A"));
    }

    /**
     * Тест на удаления вершины.
     */
    @Test
    public void testRemoveVertex() {
        IncidenceMatrixGraph graph = new IncidenceMatrixGraph(10, 10);
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B", false);
        graph.removeVertex("A");
        assertFalse(graph.vertexIndexMap.containsKey("A"));
        assertFalse(graph.incidenceMatrix[0][0]);
    }

    /**
     * Тест на добавление ребра.
     */
    @Test
    public void testAddEdge() {
        IncidenceMatrixGraph graph = new IncidenceMatrixGraph(10, 10);
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B", false);
        assertTrue(graph.incidenceMatrix[0][0]);
        assertTrue(graph.incidenceMatrix[1][0]);
    }

    /**
     * Тест на удаление ребра.
     */
    @Test
    public void testRemoveEdge() {
        IncidenceMatrixGraph graph = new IncidenceMatrixGraph(10, 10);
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B", false);
        graph.removeEdge("A", "B", false);
        assertFalse(graph.incidenceMatrix[0][0]);
        assertFalse(graph.incidenceMatrix[1][0]);
    }

    /**
     * Тест на поиск соседних вершин.
     */
    @Test
    public void testGetNeighbors() {
        IncidenceMatrixGraph graph = new IncidenceMatrixGraph(10, 10);
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B", false);
        List<String> neighbors = graph.getNeighbors("A");
        assertEquals(1, ((List<?>) neighbors).size());
        assertTrue(neighbors.contains("B"));
    }

    /**
     * Тест на считывание графа из файла.
     *
     * @throws Exception ошибка при считывании из файла
     */
    @Test
    public void testReadFromFile() throws Exception {
        IncidenceMatrixGraph graph = new IncidenceMatrixGraph(10, 10);
        File file = new File("test.txt");
        graph.readFromFile(file, false);
        assertEquals(5, graph.vertexIndexMap.size());
        assertTrue(graph.incidenceMatrix[0][0]);
        assertTrue(graph.incidenceMatrix[0][1]);
        assertTrue(graph.incidenceMatrix[1][2]);
        assertTrue(graph.incidenceMatrix[2][3]);
    }

    /**
     * Тест на правильное форматирование строки, которая представляет собой граф.
     */
    @Test
    public void testToString() {
        IncidenceMatrixGraph graph = new IncidenceMatrixGraph(10, 10);
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B", false);
        String expected = "A-B\n";
        assertEquals(expected, graph.toString());
    }

    /**
     * Тест на сравнение графов.
     */
    @Test
    public void testEquals() {
        IncidenceMatrixGraph graph1 = new IncidenceMatrixGraph(10, 10);
        graph1.addVertex("A");
        graph1.addVertex("B");
        graph1.addEdge("A", "B", false);
        IncidenceMatrixGraph graph2 = new IncidenceMatrixGraph(10, 10);
        graph2.addVertex("A");
        graph2.addVertex("B");
        graph2.addEdge("A", "B", false);
        assertTrue(graph1.equals(graph2));
    }

    /**
     * Тест на правильность топологической сортировки.
     */
    @Test
    public void testTopologicalSort() {
        IncidenceMatrixGraph graph = new IncidenceMatrixGraph(10, 10);
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
        assertEquals("A", sortedOrder.get(0));
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
        IncidenceMatrixGraph graph = new IncidenceMatrixGraph(10, 10);
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge("A", "B", true);
        graph.addEdge("B", "C", true);
        graph.addEdge("C", "A", true);
        assertThrows(IllegalStateException.class, graph::topologicalSort);
    }
}
