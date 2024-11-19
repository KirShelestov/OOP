package ru.nsu.shelestov.substring;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class SubstringTest {

    private static final String TEST_FILE_PATH = "test_large_file.txt";
    private static final long FILE_SIZE = 16L * 1024 * 1024 * 1024; // 16 ГБ
    private static final String PATTERN = "тест";

    @BeforeAll
    public static void setUp() throws IOException {
        createLargeFile(TEST_FILE_PATH);
    }

    @AfterAll
    public static void tearDown() {
        deleteTestFile(TEST_FILE_PATH);
    }

    @Test
    public void testLargeFileSubstringSearch() {
        List<Integer> matches = Substring.find(TEST_FILE_PATH, PATTERN);
        assertFalse(matches.isEmpty(), "Ожидалось, что совпадения будут найдены в большом файле.");
    }

    @Test
    public void testBasicSubstringSearch() {
        String input = "Это простой тест для поиска подстроки, тестирование алгоритма.";
        List<Integer> matches = Substring.boyerMoore(input, PATTERN);
        assertEquals(2, matches.size(), "Ожидалось, что подстрока 'тест' будет найдена 2 раза.");
    }

    @Test
    public void testNoSubstringFound() {
        String input = "В этой строке нет искомой подстроки.";
        List<Integer> matches = Substring.boyerMoore(input, "не найдется");
        assertTrue(matches.isEmpty(), "Ожидалось, что совпадений не будет.");
    }

    @Test
    public void testMultipleOccurrences() {
        String input = "тест тест тест тест";
        List<Integer> matches = Substring.boyerMoore(input, PATTERN);
        assertEquals(4, matches.size(), "Ожидалось, что подстрока 'тест' будет найдена 4 раза.");
    }

    @Test
    public void testEmptyString() {
        String input = "";
        List<Integer> matches = Substring.boyerMoore(input, PATTERN);
        assertTrue(matches.isEmpty(), "Ожидалось, что совпадений не будет для пустой строки.");
    }

    @Test
    public void testEmptyPattern() {
        String input = "Это строка с некоторым текстом.";
        List<Integer> matches = Substring.boyerMoore(input, "");
        assertTrue(matches.isEmpty(), "Ожидалось, что совпадений не будет для пустой подстроки.");
    }

    private static void createLargeFile(String filePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            byte[] buffer = new byte[1024 * 1024];
            Random random = new Random();

            while (fos.getChannel().size() < FILE_SIZE) {
                random.nextBytes(buffer);
                fos.write(buffer);
            }

            String testPattern = PATTERN + "\n";
            fos.write(testPattern.getBytes(StandardCharsets.UTF_8));
        }
    }

    private static void deleteTestFile(String filePath) {
        try {
            Files.deleteIfExists(Path.of(filePath));
            System.out.println("Тестовый файл удален.");
        } catch (IOException e) {
            System.out.println("Не удалось удалить файл: " + e.getMessage());
        }
    }
}
