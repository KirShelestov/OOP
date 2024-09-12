package ru.nsu.shelestov;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        deck = new Deck();
        discard = new Deck();
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
