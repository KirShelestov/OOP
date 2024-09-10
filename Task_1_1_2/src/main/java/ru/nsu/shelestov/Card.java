package ru.nsu.shelestov;

public class Card {
    Suit suit;
    CardInfo cardInfo;

    public Card(Suit suit, CardInfo cardInfo) {
        this.suit = suit;
        this.cardInfo = cardInfo;
    }

    public int getValue() {
        return cardInfo.cardValue;
    }

    public Suit getSuit() {
        return suit;
    }

    public CardInfo getCardInfo() {
        return cardInfo;
    }

    public Card(Card card) {
        this.suit = card.getSuit();
        this.cardInfo = card.getCardInfo();
    }

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
