package ru.nsu.shelestov.substring;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
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
     * @return
     */
    public static List<Integer> find(String inputFilePath, String patternString) {
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
        return matches;
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

        if (pattern.isEmpty()) {
            return matches;
        }

        HashMap<Character, Integer> right = new HashMap<>();

        for (int j = 0; j < patternLength; j++) {
            right.put(pattern.charAt(j), j);
        }

        int skip;
        for (int i = 0; i <= textLength - patternLength; i += skip) {
            skip = 0;
            for (int j = patternLength - 1; j >= 0; j--) {
                if (pattern.charAt(j) != text.charAt(i + j)) {
                    skip = Math.max(1, j - right.getOrDefault(text.charAt(i + j), -1));
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
