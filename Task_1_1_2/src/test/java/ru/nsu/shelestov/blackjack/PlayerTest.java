package ru.nsu.shelestov.blackjack;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.shelestov.card.Card;
import ru.nsu.shelestov.card.CardInfo;
import ru.nsu.shelestov.card.Suit;
import ru.nsu.shelestov.gamelogic.Deck;
import ru.nsu.shelestov.players.Player;

/**
 * Тестовый класс для проверки функциональности класса Player.
 */
public class PlayerTest {
    private final InputStream originalIn = System.in;
    private Player player;
    private Deck deck;
    private Deck discard;

    /**
     * инициализация.
     */
    @BeforeEach
    public void setUp() {
        player = new Player();
        discard = new Deck();
        deck = new Deck();
    }

    /**
     * Проверяет, что после выбора "0" у игрока не добавляется карта в руку.
     */
    @Test
    public void testRevealHand() {
        Card card = new Card(Suit.DIAMOND, CardInfo.SIX);
        Deck deck = new Deck(true);
        player.getHand().takeCardFromDeck(deck);

        player.revealHand();
        assertEquals(1, player.getHand().getSize());
    }
}
