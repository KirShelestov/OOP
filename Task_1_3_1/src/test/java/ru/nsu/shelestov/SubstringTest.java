package ru.nsu.shelestov;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Класс для тестирования реализации поиска подстроки.
 */
public class SubstringTest {

    /**
     * Метод для создания временного файла для тестирования.
     *
     * @param filePath путь к файлу
     * @param content содержимое файла
     * @throws IOException ошибка при записи содержимого в файл
     */
    private void createTestFile(String filePath, String content) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
        }
    }

    /**
     * Тест, в котором подстрока существует.
     *
     * @throws IOException ошибка при чтении/записи
     */
    @Test
    public void testFindSubstringExists() throws IOException {
        String filePath = "testFile.txt";
        String content = "Hello, this is a test file. This file is for testing.";
        String pattern = "test";

        createTestFile(filePath, content);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Substring.find(filePath, pattern);

        String expectedOutput = "[17, 45]";
        assertTrue(outContent.toString().contains(expectedOutput));
        System.setOut(System.out);

        new File(filePath).delete();
    }

    /**
     * Тест, в котором не существует подстроки.
     *
     * @throws IOException ошибка при чтении/записис
     */
    @Test
    public void testFindSubstringNotExists() throws IOException {
        String filePath = "testFile.txt";
        String content = "Hello, this is a test file. This file is for testing.";
        String pattern = "nonexistent";

        createTestFile(filePath, content);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Substring.find(filePath, pattern);

        String expectedOutput = "Такой подстроки не существует";
        assertTrue(outContent.toString().contains(expectedOutput));

        System.setOut(System.out);

        new File(filePath).delete();
    }
}

