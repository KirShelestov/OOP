package ru.nsu.shelestov;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.List;

/**
 * Класс для тестирования списка смежности.
 */
public class AdjacencyListGraphTest {

    /**
     * Тест на добавления вершины.
     */
    @Test
    public void testAddVertex() {
        AdjacencyListGraph graph = new AdjacencyListGraph();
        graph.addVertex("A");
        assertTrue(graph.adjacencyList.containsKey("A"));
    }

    /**
     * Тест на удаление вершины.
     */
    @Test
    public void testRemoveVertex() {
        AdjacencyListGraph graph = new AdjacencyListGraph();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B", false);
        graph.removeVertex("A");
        assertFalse(graph.adjacencyList.containsKey("A"));
        assertFalse(graph.adjacencyList.get("B").contains("A"));
    }

    /**
     * Тест на добавление ребра.
     */
    @Test
    public void testAddEdge() {
        AdjacencyListGraph graph = new AdjacencyListGraph();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B", false);
        assertTrue(graph.adjacencyList.get("A").contains("B"));
        assertTrue(graph.adjacencyList.get("B").contains("A"));
    }

    /**
     * Тест на удаление ребра.
     */
    @Test
    public void testRemoveEdge() {
        AdjacencyListGraph graph = new AdjacencyListGraph();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B", false);
        graph.removeEdge("A", "B", false);
        assertFalse(graph.adjacencyList.get("A").contains("B"));
        assertFalse(graph.adjacencyList.get("B").contains("A"));
    }

    /**
     * Тест на поиск соседних вершин.
     */
    @Test
    public void testGetNeighbors() {
        AdjacencyListGraph graph = new AdjacencyListGraph();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B", false);
        assertEquals(1, graph.getNeighbors("A").size());
        assertTrue(graph.getNeighbors("A").contains("B"));
    }

    /**
     * Тест на чтение из файла графа.
     *
     * @throws Exception ошибка при считывания файла
     */
    @Test
    public void testReadFromFile() throws Exception {
        AdjacencyListGraph graph = new AdjacencyListGraph();
        File file = new File("test.txt");

        graph.readFromFile(file, false);
        assertEquals(5, graph.adjacencyList.size());
        assertTrue(graph.adjacencyList.get("A").contains("B"));
        assertTrue(graph.adjacencyList.get("A").contains("C"));
        assertTrue(graph.adjacencyList.get("B").contains("D"));
        assertTrue(graph.adjacencyList.get("C").contains("E"));
    }

    /**
     * Тест на правильное форматирование строки, которая представляет собой граф.
     */
    @Test
    public void testToString() {
        AdjacencyListGraph graph = new AdjacencyListGraph();
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
        AdjacencyListGraph graph1 = new AdjacencyListGraph();
        graph1.addVertex("A");
        graph1.addVertex("B");
        graph1.addEdge("A", "B", false);
        AdjacencyListGraph graph2 = new AdjacencyListGraph();
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
        AdjacencyListGraph graph = new AdjacencyListGraph();
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
        AdjacencyListGraph graph = new AdjacencyListGraph();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge("A", "B", true);
        graph.addEdge("B", "C", true);
        graph.addEdge("C", "A", true);
        assertThrows(IllegalStateException.class, graph::topologicalSort);
    }
}
