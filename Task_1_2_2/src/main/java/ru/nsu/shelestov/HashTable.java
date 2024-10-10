package ru.nsu.shelestov;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Класс представляет хеш-таблицу, которая хранит пары ключ-значение.
 *
 * @param <K> ключ
 * @param <V> значение
 */
public class HashTable<K, V> implements Iterable<HashTable.Entry<K, V>> {
    private static final int INITIAL_CAPACITY = 100;
    private static final float LOAD_FACTOR = 0.75f;

    private Entry<K, V>[] table;
    private int size;
    private int threshold;
    private int modCount;

    /**
     * Конструктор для хеш-таблицы.
     */
    public HashTable() {
        this.table = new Entry[INITIAL_CAPACITY];
        this.threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
        this.size = 0;
        this.modCount = 0;
    }

    /**
     * Метод для установления пары ключ-значение в таблицу.
     *
     * @param key ключ
     * @param value значение
     */
    public void put(K key, V value) {
        int index = getIndex(key);
        Entry<K, V> newEntry = new Entry<>(key, value);

        if (table[index] == null) {
            table[index] = newEntry;
            size++;
            modCount++;
        } else {
            Entry<K, V> current = table[index];
            while (current != null) {
                if (current.key.equals(key)) {
                    current.value = value; // Обновляем значение
                    return;
                }
                if (current.next == null) {
                    current.next = newEntry; // Добавляем в конец цепочки
                    size++;
                    modCount++;
                    break;
                }
                current = current.next;
            }
        }

        if (size >= threshold) {
            resize();
        }
    }

    /**
     * Метод для получения значения по ключу.
     *
     * @param key ключ
     * @return значение по ключу
     */
    public V get(K key) {
        int index = getIndex(key);
        Entry<K, V> current = table[index];

        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    /**
     * Удаление пары ключа-значения.
     *
     * @param key ключ
     */
    public void remove(K key) {
        int index = getIndex(key);
        Entry<K, V> current = table[index];
        Entry<K, V> previous = null;

        while (current != null) {
            if (current.key.equals(key)) {
                if (previous == null) {
                    table[index] = current.next; // Удаляем первый элемент
                } else {
                    previous.next = current.next; // Удаляем элемент из цепочки
                }
                size--;
                modCount++;
                return;
            }
            previous = current;
            current = current.next;
        }
    }

    /**
     * Метод для обновления значения по ключу.
     *
     * @param key ключ
     * @param value значение
     */
    public void update(K key, V value) {
        put(key, value);
    }

    /**
     * Метод для проверки, существует ли пара ключ-значение в таблице.
     *
     * @param key ключ
     * @return существует ли пара ключ-значение в таблице
     */
    public boolean containsKey(K key) {
        return get(key) != null;
    }


    /**
     * Возвращает итератор для перебора элементов хеш-таблицы.
     *
     * @return итератор, позволяющий перебор элементов хеш-таблицы
     * @throws ConcurrentModificationException если хеш-таблица была изменена
     *         после создания итератора, кроме как через сам итератор
     */
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new Iterator<Entry<K, V>>() {
            private int currentIndex = 0;
            private Entry<K, V> currentEntry = null;
            private int expectedModCount = modCount;

            /**
             * Метод для проверки есть ли следующий элемент в итераторе.
             *
             * @return существует ли следующий элемент
             */
            @Override
            public boolean hasNext() {
                if (currentEntry != null && currentEntry.next != null) {
                    return true;
                }
                while (currentIndex < table.length) {
                    if (table[currentIndex] != null) {
                        return true;
                    }
                    currentIndex++;
                }
                return false;
            }

            /**
             * Метод для возвращения следющего элемента итератора.
             *
             * @return след элемент итератора
             */
            @Override
            public Entry<K, V> next() {
                if (expectedModCount != modCount) {
                    throw new ConcurrentModificationException();
                }
                if (currentEntry != null && currentEntry.next != null) {
                    currentEntry = currentEntry.next;
                } else {
                    while (currentIndex < table.length && table[currentIndex] == null) {
                        currentIndex++;
                    }
                    if (currentIndex >= table.length) {
                        throw new NoSuchElementException();
                    }
                    currentEntry = table[currentIndex++];
                }
                return currentEntry;
            }
        };
    }

    /**
     * Метод для увелечения размера таблицы.
     */
    private void resize() {
        Entry<K, V>[] oldTable = table;
        table = new Entry[oldTable.length * 2];
        threshold = (int) (table.length * LOAD_FACTOR);
        size = 0;

        for (Entry<K, V> entry : oldTable) {
            while (entry != null) {
                put(entry.key, entry.value);
                entry = entry.next;
            }
        }
    }

    /**
     * Метод для получения индекса в хеш-таблицы.
     *
     * @param key ключ
     * @return индекс
     */
    private int getIndex(K key) {
        return Math.abs(key.hashCode()) % table.length;
    }

    /**
     * Метод для сравнения между собой хеш-таблиц.
     *
     * @param other хеш-таблица, с которой сравнивается текущая хеш-таблица
     * @return равны ли таблицы между собой
     */
    public boolean equals(HashTable<K, V> other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }

        if (size != other.size) {
            return false;
        }

        for (Entry<K, V> entry : this) {
            K key = entry.key;
            V otherValue = other.get(key);
            if (!Objects.equals(entry.value, otherValue)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Метод для представления хеш-таблицы в строковом формате.
     *
     * @return отформатированная строка
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (Entry<K, V> entry : this) {
            sb.append(entry.key).append("=").append(entry.value).append(", ");
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * Метод для получения размера хеш-таблицы.
     *
     * @return размер таблицы
     */
    public int size() {
        return size;
    }

    /**
     * Класс представляет ключ-значение в хеш-таблице.
     *
     * @param <K> ключ
     * @param <V> значение
     */
    public static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;

        /**
         * Конструктор записи ключ-значение.
         *
         * @param key ключ
         * @param value значение
         */
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}

