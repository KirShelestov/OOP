package ru.nsu.shelestov;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Считывание происходит в следующем формате.
 *
 * На первом месте в каждой строке стоит вершина, после нее следуют ее соседи
 * Каждая строка представляет одну вершину и ее связи с другими вершинами
 */
public class Main {

    /**
     * Метод для проверки ключевых методов.
     *
     * @param args не используется
     */
    public static void main(String[] args) {

        System.out.println("Список смежности");

        AdjacencyListGraph graphFromFileAdjacencyList = new AdjacencyListGraph();

        File file = new File("test1.txt");

        try {
            graphFromFileAdjacencyList.readFromFile(file, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Граф_1, представленный в виде списка смежности и считанный из файла: ");
        System.out.println(graphFromFileAdjacencyList);

        System.out.println("Соседи A: " + graphFromFileAdjacencyList.getNeighbors("A"));
        System.out.println("Соседи B: " + graphFromFileAdjacencyList.getNeighbors("B"));
        System.out.println("Соседи C: " + graphFromFileAdjacencyList.getNeighbors("C"));
        System.out.println("Соседи D: " + graphFromFileAdjacencyList.getNeighbors("D"));

        try {
            List<String> sortedOrder = graphFromFileAdjacencyList.topologicalSort();
            System.out.println("Топологическая сортировка для графа_1: " + sortedOrder);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        graphFromFileAdjacencyList.removeEdge("A", "C", true);
        System.out.println("Граф_1 после удаления ребра A-C:");
        System.out.println(graphFromFileAdjacencyList);

        graphFromFileAdjacencyList.removeVertex("D");
        System.out.println("Граф_1 после удаления вершины D:");
        System.out.println(graphFromFileAdjacencyList);

        AdjacencyListGraph graphAdjacencyList = new AdjacencyListGraph();

        graphAdjacencyList.addVertex("A");
        graphAdjacencyList.addVertex("B");
        graphAdjacencyList.addVertex("C");
        graphAdjacencyList.addVertex("D");

        graphAdjacencyList.addEdge("A", "B", true);
        graphAdjacencyList.addEdge("A", "C", true);
        graphAdjacencyList.addEdge("B", "D", true);
        graphAdjacencyList.addEdge("C", "D", true);

        System.out.println("Граф_2, представленный в виде списка смежности: ");
        System.out.println(graphAdjacencyList);

        System.out.println("Соседи A: " + graphAdjacencyList.getNeighbors("A"));
        System.out.println("Соседи B: " + graphAdjacencyList.getNeighbors("B"));
        System.out.println("Соседи C: " + graphAdjacencyList.getNeighbors("C"));
        System.out.println("Соседи D: " + graphAdjacencyList.getNeighbors("D"));

        try {
            List<String> sortedOrder = graphAdjacencyList.topologicalSort();
            System.out.println("Топологическая сортировка для графа_2: " + sortedOrder);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        graphAdjacencyList.removeEdge("A", "C", true);
        System.out.println("Граф_2 после удаления ребра A-C:");
        System.out.println(graphAdjacencyList);

        graphAdjacencyList.removeVertex("D");
        System.out.println("Граф_2 после удаления вершины D:");
        System.out.println(graphAdjacencyList);


        System.out.println("Матрица смежности");

        AdjacencyMatrixGraph graphFromFileAdjacencyMatrix = new AdjacencyMatrixGraph(5);

        try {
            graphFromFileAdjacencyMatrix.readFromFile(file, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Граф_1, представленный в виде матрицы смежности "
                + "и считанный из файла: ");
        System.out.println(graphFromFileAdjacencyMatrix.toString());

        System.out.println("Соседи A: " + graphFromFileAdjacencyMatrix.getNeighbors("A"));
        System.out.println("Соседи B: " + graphFromFileAdjacencyMatrix.getNeighbors("B"));
        System.out.println("Соседи C: " + graphFromFileAdjacencyMatrix.getNeighbors("C"));
        System.out.println("Соседи D: " + graphFromFileAdjacencyMatrix.getNeighbors("D"));

        try {
            List<String> sortedOrder = graphFromFileAdjacencyMatrix.topologicalSort();
            System.out.println("Топологическая сортировка для графа_1: " + sortedOrder);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        graphFromFileAdjacencyMatrix.removeEdge("A", "C", true);
        System.out.println("Граф_1 после удаления ребра A-C:");
        System.out.println(graphFromFileAdjacencyMatrix);

        graphFromFileAdjacencyMatrix.removeVertex("D");
        System.out.println("Граф_1 после удаления вершины D:");
        System.out.println(graphFromFileAdjacencyMatrix);

        AdjacencyMatrixGraph graph1 = new AdjacencyMatrixGraph(15);

        graph1.addVertex("A");
        graph1.addVertex("B");
        graph1.addVertex("C");
        graph1.addVertex("D");
        graph1.addVertex("E");

        graph1.addEdge("A", "B", true); // A -> B
        graph1.addEdge("A", "C", true); // A -> C
        graph1.addEdge("B", "D", true); // B -> D
        graph1.addEdge("C", "D", true); // C -> D
        graph1.addEdge("D", "E", true); // D -> E
        System.out.println(graph1);

        try {
            List<String> sortedList = graph1.topologicalSort();
            System.out.println("Топологическая сортировка : " + sortedList);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Матрица инцедентности");

        IncidenceMatrixGraph graphFromFileIncidenceMatrix = new IncidenceMatrixGraph(10, 10);

        try {
            graphFromFileIncidenceMatrix.readFromFile(file, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Граф_1, представленный в виде"
               + " матрицы инцедентности и считанный из файла: ");
        System.out.println(graphFromFileIncidenceMatrix.toString());

        try {
            List<String> sortedList1 = graphFromFileIncidenceMatrix.topologicalSort();
            System.out.println("Топологическая сортировка: " + sortedList1);
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        }

        IncidenceMatrixGraph graphIncidenceMatrix = new IncidenceMatrixGraph(10, 10);

        graphIncidenceMatrix.addVertex("A");
        graphIncidenceMatrix.addVertex("B");
        graphIncidenceMatrix.addVertex("C");
        graphIncidenceMatrix.addVertex("D");
        graphIncidenceMatrix.addVertex("E");

        graphIncidenceMatrix.addEdge("A", "B", true);
        graphIncidenceMatrix.addEdge("A", "C", true);
        graphIncidenceMatrix.addEdge("B", "D", true);
        graphIncidenceMatrix.addEdge("C", "D", true);
        graphIncidenceMatrix.addEdge("D", "E", true);


    }
}
