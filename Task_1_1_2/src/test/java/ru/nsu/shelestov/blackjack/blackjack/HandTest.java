package ru.nsu.shelestov.blackjack.blackjack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.nsu.shelestov.blackjack.gamelogic.Deck;
import ru.nsu.shelestov.blackjack.players.Hand;

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
        hand.takeCardFromDeck(deck);
        hand.takeCardFromDeck(deck);
        Deck discardDeck = new Deck();
        assertEquals(2, hand.getSize());
        hand.discardHandToDeck(discardDeck);
        assertEquals(2, discardDeck.getCards().size());
        assertEquals(50, deck.cardsLeft());
    }
}
