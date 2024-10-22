package ru.nsu.shelestov.graph;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        // Создание графа через список смежности
        Graph<String> adjacencyListGraph = new AdjacencyListGraph<>();
        adjacencyListGraph.addVertex("A");
        adjacencyListGraph.addVertex("B");
        adjacencyListGraph.addVertex("C");
        adjacencyListGraph.addEdge("A", "B", 5, true);
        adjacencyListGraph.addEdge("B", "C", 5, true);
        System.out.println("Список смежности:");
        System.out.println(adjacencyListGraph);

        // Создание графа через матрицу смежности
        Graph<String> adjacencyMatrixGraph = new AdjacencyMatrixGraph<>(3);
        adjacencyMatrixGraph.addVertex("A");
        adjacencyMatrixGraph.addVertex("B");
        adjacencyMatrixGraph.addVertex("C");
        adjacencyMatrixGraph.addEdge("A", "B", 5,true);
        adjacencyMatrixGraph.addEdge("B", "C", 5,true);
        System.out.println("Матрица смежности:");
        System.out.println(adjacencyMatrixGraph);

        // Создание графа через матрицу инцидентности
        Graph<String> incidenceMatrixGraph = new IncidenceMatrixGraph<>(3);
        incidenceMatrixGraph.addVertex("A");
        incidenceMatrixGraph.addVertex("B");
        incidenceMatrixGraph.addVertex("C");
        incidenceMatrixGraph.addEdge("A", "B", 5,true);
        incidenceMatrixGraph.addEdge("B", "C", 5,true);
        System.out.println("Матрица инцидентности:");
        System.out.println(incidenceMatrixGraph);

        // Топологическая сортировка
        TopologicalSorter<String> sorter = new TopologicalSorter<>();
        List<String> sortedList = sorter.sort(adjacencyListGraph);
        System.out.println("Топологическая сортировка (список смежности): " + sortedList);


        TopologicalSorter<String> sorter1 = new TopologicalSorter<>();
        List<String> sortedList1 = sorter1.sort(adjacencyMatrixGraph);
        System.out.println("Топологическая сортировка (матрица смежности): " + sortedList1);


        try {
           File file = new File(Objects.requireNonNull(Main.class.getResource("/test.txt")).toURI());

            // Пример чтения графа из файла в формате: A B C
            Graph<String> fileGraph = new AdjacencyListGraph<>();
            fileGraph.read(file, true, String::trim);
            System.out.println("Граф из файла (список смежности):");
            System.out.println(fileGraph);

            TopologicalSorter<String> sorter3 = new TopologicalSorter<>();
            List<String> fileSortedList = sorter3.sort(fileGraph);
            System.out.println("Топологическая сортировка (граф из файла): " + fileSortedList);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.err.println("Ошибка получения URI файла: " + e.getMessage());
        }
    }
}