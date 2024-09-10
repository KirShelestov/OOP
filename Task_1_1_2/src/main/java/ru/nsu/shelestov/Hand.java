package ru.nsu.shelestov;

import java.util.ArrayList;

/**
 * класс представляет "руку".
 */
public class Hand {
    ArrayList<Card> hand;

    /**
     * конструктор пустой руки.
     */
    public Hand() {
        hand = new ArrayList<Card>();
    }

    /**
     * добавляет карту в руку.
     *
     * @param deck колода, из которой берется карта в руки
     */
    public void takeCardFromDeck(Deck deck) {
        hand.add(deck.takeCard());
    }

    /**
     * подсчитывается значение на руке.
     * Если значение > 21 и тузов > 1, то достоинство туза становится 1
     *
     * @return значение на руке
     */
    public int getValueOnHand() {
        int value = 0;
        int acesInNumber = 0;

        for (Card card : hand) {
            value += card.getValue();
            if (card.getValue() == 11) {
                acesInNumber++;
            }
        }
        if ((value > 21) && (acesInNumber > 0)) {
            while ((acesInNumber > 0) && (value > 21)) {
                acesInNumber -= 1;
                value -= 10;
            }
        }
        return value;
    }

    /**
     * возвращает определенную карту из руки.
     *
     * @param index индекс карты в руке
     * @return карту по индексу
     */
    public Card getCard(int index) {
        return hand.get(index);
    }

    /**
     * возвращает количество карт в руке.
     *
     * @return количество колод в руке
     */
    public int getSize() {
        return hand.size();
    }

    /**
     * выводит в консоль отформатированную строку, в которой содержатся карты в руке у персона.
     *
     * @return строку, отформатированную с учетом тз
     */
    public String toString() {
        String output = "[";
        for (int i = 0; i < hand.size(); i++) {
            output += hand.get(i);
            if (i < hand.size() - 1) {
                output += ", ";
            }
        }
        output += "]";
        return output;
    }

    /**
     * Сбрасывает карты из руки игрока в колоду сброса.
     *
     * @param discardDeck колода сброса
     */
    public void discardHandToDeck(Deck discardDeck) {
        discardDeck.addCards(hand);
        hand.clear();
    }
}
