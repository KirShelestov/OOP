package ru.nsu.shelestov.blackjack.card;

/**
 * класс представляет карту в колоде.
 */
public record Card(Suit suit, CardInfo cardInfo) {
    /**
     * Возвращает значение карты.
     *
     * @return значение карты
     */
    public int getValue() {
        return cardInfo.getCardValue();
    }

    /**
     * Метод для текстового представления карты.
     *
     * @return форматированная строка
     */
    @Override
    public String toString() {
        String adjective = suit.toAdjective(cardInfo.getGender());

        if (cardInfo.getGender() == Gender.MASCULINE || cardInfo.getGender() == Gender.FEMININE) {
            return adjective + cardInfo.getCardName().toLowerCase()
                    + " (" + cardInfo.getCardValue() + ")";
        }
        return cardInfo + " " + adjective + " (" + cardInfo.getCardValue() + ")";
    }
}