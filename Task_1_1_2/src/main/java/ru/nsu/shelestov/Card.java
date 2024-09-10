package ru.nsu.shelestov;

/**
 * класс представляет карту в колоде.
 */
public class Card {
    Suit suit;
    CardInfo cardInfo;

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

    /**
     * Возвращает строку с правильеым выводом карт в консоль.
     *
     * @return строка, описывающая карту
     */
    public String toString() {
        if (cardInfo == cardInfo.QUEEN) {
            if (suit == suit.CLUB) {
                return ("Трефовая дама (" + this.getValue() + ")");
            }
            if (suit == suit.DIAMOND) {
                return ("Бубновая дама (" + this.getValue() + ")");
            }
            if (suit == suit.HEART) {
                return ("Червовая дама (" + this.getValue() + ")");
            }
            if (suit == suit.SPADE) {
                return ("Пиковая дама (" + this.getValue() + ")");
            }
        }
        if (cardInfo == cardInfo.KING) {
            if (suit == suit.CLUB) {
                return ("Трефовый король (" + this.getValue() + ")");
            }
            if (suit == suit.DIAMOND) {
                return ("Бубновый король (" + this.getValue() + ")");
            }
            if (suit == suit.HEART) {
                return ("Червовый король (" + this.getValue() + ")");
            }
            if (suit == suit.SPADE) {
                return ("Пиковый король (" + this.getValue() + ")");
            }
        }
        if (cardInfo == cardInfo.JACK) {
            if (suit == suit.CLUB) {
                return ("Трефовый валет (" + this.getValue() + ")");
            }
            if (suit == suit.DIAMOND) {
                return ("Бубновый валет (" + this.getValue() + ")");
            }
            if (suit == suit.HEART) {
                return ("Червовый валет (" + this.getValue() + ")");
            }
            if (suit == suit.SPADE) {
                return ("Пиковый валет (" + this.getValue() + ")");
            }
        }
        return ("" + cardInfo + " " + suit + " (" + this.getValue() + ")");
    }
}
