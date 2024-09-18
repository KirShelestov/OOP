package ru.nsu.shelestov;

/**
 * класс, реализующий карты от двойки до туза.
 */
public enum CardInfo {
    ACE("Туз", 11, Gender.NEUTRAL),
    TWO("Двойка", 2, Gender.NEUTRAL),
    THREE("Тройка", 3, Gender.NEUTRAL),
    FOUR("Четверка", 4, Gender.NEUTRAL),
    FIVE("Пятерка", 5, Gender.NEUTRAL),
    SIX("Шестерка", 6, Gender.NEUTRAL),
    SEVEN("Семерка", 7, Gender.NEUTRAL),
    EIGHT("Восьмерка", 8, Gender.NEUTRAL),
    NINE("Девятка", 9, Gender.NEUTRAL),
    TEN("Десятка", 10, Gender.NEUTRAL),
    JACK("Валет", 10, Gender.MASCULINE),
    QUEEN("Дама", 10, Gender.FEMININE),
    KING("Король", 10, Gender.MASCULINE);

    String cardName;
    int cardValue;
    Gender gender;

    CardInfo(String cardName, int cardValue, Gender gender) {
        this.cardName = cardName;
        this.cardValue = cardValue;
        this.gender = gender;
    }

    public Gender gender() {
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
