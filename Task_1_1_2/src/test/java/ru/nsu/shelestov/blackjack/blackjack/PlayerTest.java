package ru.nsu.shelestov.blackjack.blackjack;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.shelestov.blackjack.card.Card;
import ru.nsu.shelestov.blackjack.card.CardInfo;
import ru.nsu.shelestov.blackjack.card.Suit;
import ru.nsu.shelestov.blackjack.gamelogic.Deck;
import ru.nsu.shelestov.blackjack.players.Player;

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
        discard = new Deck();
        player = new Player();
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
