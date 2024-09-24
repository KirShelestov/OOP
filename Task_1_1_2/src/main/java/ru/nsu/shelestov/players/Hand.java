package ru.nsu.shelestov.players;

import java.util.ArrayList;
import ru.nsu.shelestov.card.Card;
import ru.nsu.shelestov.gamelogic.Deck;

/**
 * класс представляет "руку".
 */
public class Hand {
    private ArrayList<Card> hand;

    /**
     * конструктор пустой руки.
     */
    public Hand() {
        hand = new ArrayList<>();
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
     * Если значение больше 21 и тузов больше 1, то достоинство туза становится 1
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
        if (value > 21 && acesInNumber > 0) {
            while (acesInNumber > 0 && value > 21) {
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
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("[");
        for (int i = 0; i < hand.size(); i++) {
            output.append(hand.get(i));
            if (i < hand.size() - 1) {
                output.append(", ");
            }
        }
        output.append("]");
        return output.toString();
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
