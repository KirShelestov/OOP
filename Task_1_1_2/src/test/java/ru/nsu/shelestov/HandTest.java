package ru.nsu.shelestov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * класс для теста класса Hand.
 */
class HandTest {

    /**
     * метод проверяет правильность подсчета значений на руке.
     */
    @Test
    void testGetValueOnHand() {
        Hand hand = new Hand();
        Deck deck = new Deck(true);
        hand.takeCardFromDeck(deck);
        hand.takeCardFromDeck(deck);
        assertTrue(hand.getValueOnHand() >= 2 && hand.getValueOnHand() <= 21);
    }

    /**
     * тест для еще одного метода.
     */
    @Test
    void testDiscardHandToDeck() {
        Hand hand = new Hand();
        Deck deck = new Deck(true);
        Deck discardDeck = new Deck();
        hand.takeCardFromDeck(deck);
        hand.takeCardFromDeck(deck);
        hand.discardHandToDeck(discardDeck);
        assertEquals(2, discardDeck.getCards().size());
        assertEquals(50, deck.cardsLeft());
    }
}
