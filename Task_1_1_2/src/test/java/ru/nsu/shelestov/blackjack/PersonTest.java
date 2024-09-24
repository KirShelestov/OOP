package ru.nsu.shelestov.blackjack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.shelestov.gamelogic.Deck;
import ru.nsu.shelestov.players.Person;
import ru.nsu.shelestov.players.Player;

/**
 * класс для теста Персона.
 */
public class PersonTest {
    private Person player;
    private Deck deck;

    /**
     * инициализация игрока и колоды.
     */
    @BeforeEach
    public void setUp() {
        player = new Player();
        deck = new Deck(true);
        deck.shuffle();
    }

    /**
     * тест для метода hit.
     */
    @Test
    public void testHit() {
        player.hit(deck, new Deck());
        assertEquals(1, player.getHand().getSize());
    }

    /**
     * тест для победы с блэкджеком.
     */
    @Test
    public void testWinBlackjack() {
        player.getHand().takeCardFromDeck(deck);
        player.getHand().takeCardFromDeck(deck);
        assertFalse(player.winBlackjack());
    }
}
