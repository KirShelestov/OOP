package ru.nsu.shelestov;

/**
 * класс, реализующий карты от двойки до туза.
 *
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
    JACK("Валет", 10),
    QUEEN("Дама", 10),
    KING("Король", 10);
    
	String cardName;
    int cardValue;

    /**
     * конструктор для создания "лица" карты.
     *
     * @param cardName название карты
     * @param cardValue достоинство карты
     */
    CardInfo(String cardName, int cardValue){
        this.cardName = cardName;
        this.cardValue = cardValue;
    }

    /**
     * метод нужен для дальнейшего вывода имени карты.
     *
     * @return имя карты
     */
    public String toString(){
        return cardName;
    }
}
