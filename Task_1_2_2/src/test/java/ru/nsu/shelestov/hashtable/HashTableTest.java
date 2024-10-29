package ru.nsu.shelestov.hashtable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import org.junit.jupiter.api.Test;

/**
 * Класс для тестирования хеш-таблицы.
 */
public class HashTableTest {

    /**
     * Тест на установления и получения значения по ключу.
     */
    @Test
    public void testPutAndGet() {
        HashTable<String, Integer> hashTable = new HashTable<>();
        hashTable.put("one", 1);
        assertEquals(1, (int) hashTable.get("one"));
    }

    /**
     * Тест на обновление значения по ключу.
     */
    @Test
    public void testUpdate() {
        HashTable<String, Integer> hashTable = new HashTable<>();
        hashTable.put("one", 1);
        hashTable.update("one", 2);
        assertEquals(2, (int) hashTable.get("one"));
    }

    /**
     * Тест на удаление значения по ключу.
     */
    @Test
    public void testRemove() {
        HashTable<String, Integer> hashTable = new HashTable<>();
        hashTable.put("one", 1);
        hashTable.remove("one");
        assertNull(hashTable.get("one"));
    }

    /**
     * Тест на проверку наличия пары ключ-значение.
     */
    @Test
    public void testContainsKey() {
        HashTable<String, Integer> hashTable = new HashTable<>();
        hashTable.put("one", 1);
        assertTrue(hashTable.containsKey("one"));
        assertFalse(hashTable.containsKey("two"));
    }

    /**
     * Тест на правильность работы итератора.
     */
    @Test
    public void testIterator() {
        HashTable<String, Integer> hashTable = new HashTable<>();
        hashTable.put("one", 1);
        hashTable.put("two", 2);
        Iterator<HashTable.Entry<String, Integer>> iterator = hashTable.iterator();
        assertTrue(iterator.hasNext());
        HashTable.Entry<String, Integer> entry = iterator.next();
        assertEquals("two", entry.key);
        assertEquals(2, (int) entry.value);
        assertTrue(iterator.hasNext());
        entry = iterator.next();
        assertEquals("one", entry.key);
        assertEquals(1, (int) entry.value);
        assertFalse(iterator.hasNext());
    }

    /**
     * Тест на правильность реализации сравнения таблиц.
     */
    @Test
    public void testEquals() {
        HashTable<String, Integer> hashTable1 = new HashTable<>();
        hashTable1.put("one", 1);
        hashTable1.put("two", 2);
        HashTable<String, Integer> hashTable2 = new HashTable<>();
        hashTable2.put("one", 1);
        hashTable2.put("two", 2);
        assertTrue(hashTable1.equals(hashTable2));
        hashTable2.put("three", 3);
        assertFalse(hashTable1.equals(hashTable2));
    }

    /**
     * Тест на правильное строковое представления таблицы.
     */
    @Test
    public void testToString() {
        HashTable<String, Integer> hashTable = new HashTable<>();
        hashTable.put("one", 1);
        hashTable.put("two", 2);
        assertEquals("{two=2, one=1}", hashTable.toString());
    }

    /**
     * Тест на обновление размера таблицы.
     */
    @Test
    public void testResize() {
        HashTable<String, Integer> hashTable = new HashTable<>();
        for (int i = 0; i < 100; i++) {
            hashTable.put("key" + i, i);
        }
        assertEquals(100, hashTable.size());
    }
}