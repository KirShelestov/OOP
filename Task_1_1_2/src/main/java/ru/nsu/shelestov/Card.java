package ru.nsu.shelestov;

/**
 * класс представляет карту в колоде.
 */
public class Card {
    private final Suit suit;
    private final CardInfo cardInfo;

    /**
     * конструктор карты.
     *
     * @param suit     масть карты
     * @param cardInfo достоинство карты
     */
    public Card(Suit suit, CardInfo cardInfo) {
        this.suit = suit;
        this.cardInfo = cardInfo;
    }

    /**
     * возваращает значение карты.
     *
     * @return значение карты
     */
    public int getValue() {
        return cardInfo.cardValue;
    }

    /**
     * возвращает масть карты.
     *
     * @return масть карты
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Возвращает информацию о карте.
     *
     * @return информацию о карте
     */
    public CardInfo getCardInfo() {
        return cardInfo;
    }

    /**
     * Создает новую карту как копию выбранной.
     *
     * @param card карту, которую нужно скопировать
     */
    public Card(Card card) {
        this.suit = card.getSuit();
        this.cardInfo = card.getCardInfo();
    }

    @Override
    public String toString() {

        String adjective = suit.toAdjective(cardInfo.gender());

        if (cardInfo.gender() == Gender.MASCULINE || cardInfo.gender() == Gender.FEMININE) {
            return adjective + cardInfo.cardName.toLowerCase() + " (" + cardInfo.cardValue + ")";
        }
        return cardInfo + " " + adjective + " (" + cardInfo.cardValue + ")";
    }
}
