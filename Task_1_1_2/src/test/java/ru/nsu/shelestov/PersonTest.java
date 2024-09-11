package ru.nsu.shelestov;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {
    private Person player;
    private Deck deck;

    @BeforeEach
    public void setUp() {
        player = new Player();
        deck = new Deck(true);
        deck.shuffle();
    }

    @Test
    public void testHit() {
        player.hit(deck, new Deck());
        assertEquals(1, player.getHand().getSize());
    }

    @Test
    public void testWinBlackjack() {
        player.getHand().takeCardFromDeck(deck);
        player.getHand().takeCardFromDeck(deck);
        assertEquals(false, player.winBlackjack());
    }
}
