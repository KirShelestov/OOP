package ru.nsu.shelestov;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * Класс для тестирования Main, где демонстрируется работоспособность хеш-таблиц.
 */
public class MainTest {
    public static void main(String[] args) {
        PrintStream originalOut = System.out;

        String input = "one=1\ntwo=2\nthree=3\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        HashTable<String, Number> hashTable = new HashTable<>();

        hashTable.put("one", 1);
        hashTable.put("two", 2);
        hashTable.put("three", 3);

        assertTrue(hashTable.get("one").equals(1));
        assertTrue(hashTable.get("two").equals(2));
        assertTrue(hashTable.get("three").equals(3));

        hashTable.update("one", 1.0);
        assertTrue(hashTable.get("one").equals(1.0));

        assertTrue(hashTable.containsKey("two"));

        hashTable.remove("three");
        assertFalse(hashTable.containsKey("three"));

        for (HashTable.Entry<String, Number> entry : hashTable) {
            System.out.println(entry.key + "=" + entry.value);
        }

        HashTable<String, Number> otherHashTable = new HashTable<>();
        otherHashTable.put("one", 1.0);
        otherHashTable.put("two", 2);
        assertTrue(hashTable.equals(otherHashTable));

        HashTable<String, Number> differentHashTable = new HashTable<>();
        differentHashTable.put("one", 1.0);
        differentHashTable.put("two", 3);

        assertFalse(hashTable.equals(differentHashTable));

        assertTrue(hashTable.equals(hashTable));

        assertFalse(hashTable.equals(null));

        System.out.println(hashTable.toString());

        System.setOut(originalOut);
    }
}

