package ru.nsu.shelestov;

/**
 * Класс для мастей карт.
 *
 */
public enum Suit {
    CLUB("Трефы"),
    DIAMOND("Бубны"),
    HEART("Черви"),
    SPADE("Пики");

    String suitName;

    /**
     * конструктор для масти.
     *
     * @param suitName масть карты
     */
    Suit(String suitName) {
        this.suitName = suitName;
    }

    /**
     * функция для последующего вывода масти карты в консоль.
     *
     * @return масть карты
     */
    public String toString() {
        return suitName;
    }
}
