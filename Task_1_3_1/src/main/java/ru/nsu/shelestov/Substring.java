package ru.nsu.shelestov;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для поиска подстроки.
 */
public class Substring {
    /**
     * Метод считывает строку из файла и ищет подстроку с помощью Бойера-Мура.
     *
     * @param inputFilePath путь к файлу
     * @param patternString нужная подстрока
     */
    public static void find(String inputFilePath, String patternString) {
        List<Integer> matches = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(inputFilePath)) {

            StringBuilder batchBuilder = new StringBuilder();
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                batchBuilder.append(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));

                if (batchBuilder.length() > 10000) {
                    matches.addAll(boyerMoore(batchBuilder.toString(), patternString));
                    batchBuilder.setLength(0);
                }
            }

            if (batchBuilder.length() > 0) {
                matches.addAll(boyerMoore(batchBuilder.toString(), patternString));
            }

            if (!matches.isEmpty()) {
                System.out.print("[");
                for (int i = 0; i < matches.size(); i++) {
                    System.out.print(matches.get(i));
                    if (i < matches.size() - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println("]");
            } else {
                System.out.println("Такой подстроки не существует");
            }

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Реализация Бойера-Мура для поиска подстроки.
     *
     * @param text строка
     * @param pattern подстрока
     * @return массив начал вхождений подстроки в строке
     */
    public static List<Integer> boyerMoore(String text, String pattern) {
        List<Integer> matches = new ArrayList<>();
        int patternLength = pattern.length();
        int textLength = text.length();
        int[] right = new int[256];

        for (int c = 0; c < 256; c++) {
            right[c] = -1;
        }
        for (int j = 0; j < patternLength; j++) {
            right[pattern.charAt(j)] = j;
        }

        int skip;
        for (int i = 0; i <= textLength - patternLength; i += skip) {
            skip = 0;
            for (int j = patternLength - 1; j >= 0; j--) {
                if (pattern.charAt(j) != text.charAt(i + j)) {
                    skip = Math.max(1, j - right[text.charAt(i + j)]);
                    break;
                }
            }
            if (skip == 0) {
                matches.add(i);
                skip = 1;
            }
        }

        return matches;
    }
}
