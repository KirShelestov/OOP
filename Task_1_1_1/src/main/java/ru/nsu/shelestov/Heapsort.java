package ru.nsu.shelestov;

import java.util.Scanner;

public class Heapsort {

    /**
     * Сортировка массива
     *
     * @param arr массив, который нужно отсортировать
     */
    public void sort(int[] arr) {
        int n = arr.length;
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }
        for (int i = n - 1; i >= 0; i--) {
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;
            heapify(arr, i, 0);
        }
    }

    /**
     * Приводит поддерево с корнем в узле i к свойству кучи
     *
     * @param arr наша куча
     * @param n размер кучи
     * @param i индекс корня поддерева, которое нужно привести к свойству кучи
     */
    void heapify(int[] arr, int n, int i) {
        int largest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;

        if (l < n && arr[l] > arr[largest]) {
            largest = l;
        }
        if (r < n && arr[r] > arr[largest]) {
            largest = r;
        }
        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;

            heapify(arr, n, largest);
        }
    }

    /**
     * Вывод массива
     *
     * @param arr
     */
    static void printArray(int[] arr) {
        for (int j : arr) {
            System.out.print(j + " ");
        }
        System.out.println();
    }

    /**
     * Просто мейн
     *
     * @param args не используется
     */
    public static void main(String[] args) {
        int n;
        Scanner scan = new Scanner(System.in);
        n = scan.nextInt();
        int[] arrayTest = new int[n];
        for (int i = 0; i < n; i++) {
            arrayTest[i] = scan.nextInt();
        }
        Heapsort ob = new Heapsort();
        ob.sort(arrayTest);
        printArray(arrayTest);
    }

}