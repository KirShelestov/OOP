package ru.nsu.shelestov.card;

/**
 * класс, реализующий карты от двойки до туза.
 */
public enum CardInfo {
    ACE("Туз", 11),
    TWO("Двойка", 2),
    THREE("Тройка", 3),
    FOUR("Четверка", 4),
    FIVE("Пятерка", 5),
    SIX("Шестерка", 6),
    SEVEN("Семерка", 7),
    EIGHT("Восьмерка", 8),
    NINE("Девятка", 9),
    TEN("Десятка", 10),
    JACK("Валет", 10, Gender.MASCULINE),
    QUEEN("Дама", 10, Gender.FEMININE),
    KING("Король", 10, Gender.MASCULINE);

    final String cardName;
    final int cardValue;
    final Gender gender;

    /**
     * Конструктор с двумя параметрами, устанавливающий род карты по умолчанию в NEUTRAL.
     *
     * @param cardName имя карты
     * @param cardValue значение карты
     */
    CardInfo(String cardName, int cardValue) {
        this(cardName, cardValue, Gender.NEUTRAL);
    }

    /**
     * Конструктор с тремя параметрами.
     *
     * @param cardName имя карты
     * @param cardValue значение карты
     * @param gender   род карты
     */
    CardInfo(String cardName, int cardValue, Gender gender) {
        this.gender = gender;
        this.cardName = cardName;
        this.cardValue = cardValue;
    }

    /**
     * Геттер для имени.
     *
     * @return имя карты
     */
    public String getCardName() {
        return cardName;
    }

    /**
     * Геттер для значения.
     *
     * @return значение карты
     */
    public int getCardValue() {
        return cardValue;
    }

    /**
     * Геттер для рода карты.
     *
     * @return род карты
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * метод нужен для дальнейшего вывода имени карты.
     *
     * @return имя карты
     */
    @Override
    public String toString() {
        return cardName;
    }
}
