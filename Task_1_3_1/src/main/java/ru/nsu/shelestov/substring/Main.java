package ru.nsu.shelestov.substring;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите путь к входному файлу: ");
        String inputFilePath = scanner.nextLine();

        System.out.print("Введите подстроку для поиска: ");
        String patternString = scanner.nextLine();

        Substring.find(inputFilePath, patternString);

        System.out.println("Поиск завершен.");
    }
}
