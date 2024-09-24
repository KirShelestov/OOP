package ru.nsu.shelestov.blackjack.card;

/**
 * Класс для мастей карт.
 */
public enum Suit {
    DIAMOND("Бубны"),
    CLUB("Трефы"),
    HEART("Черви"),
    SPADE("Пики");

    private final String suitName;

    /**
     * конструктор для масти.
     *
     * @param suitName масть карты
     */
    Suit(String suitName) {
        this.suitName = suitName;
    }

    /**
     * Правильно форматирует строку.
     *
     * @param gender род карты
     * @return правильная строка
     */
    public String toAdjective(Gender gender) {
        String adjective = this.toString();
        if (Gender.MASCULINE == gender) {
            return this.toString().substring(0, this.toString().length() - 1) + "овый ";
        }
        if (Gender.FEMININE == gender) {
            return this.toString().substring(0, this.toString().length() - 1) + "овая ";
        }
        return adjective;
    }

    /**
     * Метод для каста в строку.
     *
     * @return строка отформатированная правильно
     */
    @Override
    public String toString() {
        return suitName;
    }
}

