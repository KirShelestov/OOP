package ru.nsu.shelestov;

/**
 * Класс для демонстрации работоспособности хеш-таблицы.
 */
public class Main {
    public static void main(String[] args) {
        HashTable<String, Number> hashTable = new HashTable<>();

        hashTable.put("one", 1);
        hashTable.put("two", 2);
        hashTable.put("three", 3);

        hashTable.update("one", 1.0);

        System.out.println(hashTable.get("one"));

        System.out.println(hashTable.containsKey("two"));

        hashTable.remove("three");

        System.out.println(hashTable.containsKey("three"));

        for (HashTable.Entry<String, Number> entry : hashTable) {
            System.out.println(entry.key + "=" + entry.value);
        }

        HashTable<String, Number> otherHashTable = new HashTable<>();
        otherHashTable.put("one", 1.0);
        otherHashTable.put("two", 2);
        System.out.println(hashTable.equals(otherHashTable));

        HashTable<String, Number> differentHashTable = new HashTable<>();
        differentHashTable.put("one", 1.0);
        differentHashTable.put("two", 3);
        System.out.println(hashTable.equals(differentHashTable));

        System.out.println(hashTable.equals(hashTable));

        System.out.println(hashTable.equals(null));

        System.out.println(hashTable.toString());
    }
}

