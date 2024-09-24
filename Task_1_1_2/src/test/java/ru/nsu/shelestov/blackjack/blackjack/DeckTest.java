package ru.nsu.shelestov.blackjack.blackjack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.shelestov.blackjack.card.Card;
import ru.nsu.shelestov.blackjack.card.CardInfo;
import ru.nsu.shelestov.blackjack.card.Suit;
import ru.nsu.shelestov.blackjack.gamelogic.Deck;

/**
 * класс для теста класса Deck.
 */
class DeckTest {

    private Deck deck;

    /**
     * Создаем полную колоду карт.
     */
    @BeforeEach
    void setUp() {
        deck = new Deck(true);
    }

    /**
     * проверяем, что в колоде при инициализации действительно 52 карты находится.
     */
    @Test
    void testDeckInitialization() {
        assertNotNull(deck.getCards());
        assertEquals(52, deck.cardsLeft());
    }

    /**
     * проверка на добавление карты в колоду.
     * должно увеличиться на 1 карту в колоде
     */
    @Test
    void testAddCard() {
        Card card = new Card(Suit.HEART, CardInfo.ACE);
        deck.addCard(card);
        assertEquals(53, deck.cardsLeft());
    }

    /**
     * Проверяем, что колода перемешана.
     */
    @Test
    void testShuffle() {
        ArrayList<Card> originalDeck = new ArrayList<>(deck.getCards());
        deck.shuffle();
        assertNotEquals(originalDeck, deck.getCards());
    }

    /**
     * проверяем, что при взятии карты в колоде стало на одну карту меньше.
     */
    @Test
    void testTakeCard() {
        Card takenCard = deck.takeCard();
        assertEquals(51, deck.cardsLeft());
        assertNotNull(takenCard);
    }

    @Test
    void testHasCards() {
        assertTrue(deck.hasCards()); // Проверяем, что колода содержит карты
        for (int i = 0; i < 52; i++) {
            deck.takeCard(); // Берем все карты из колоды
        }
        assertFalse(deck.hasCards()); // Проверяем, что колода пуста
    }

    /**
     * Проверяем, что после очистки колоды в ней нет карт.
     */
    @Test
    void testEmptyDeck() {
        deck.emptyDeck();
        assertEquals(0, deck.cardsLeft());
    }

    /**
     * Проверяем, что добавление списка карт увеличивает количество карт.
     */
    @Test
    void testAddCards() {
        ArrayList<Card> cardsToAdd = new ArrayList<>();
        cardsToAdd.add(new Card(Suit.SPADE, CardInfo.KING));
        deck.addCards(cardsToAdd);
        assertEquals(53, deck.cardsLeft());
    }

    /**
     * проверка на перетасовку колоды после взятие карт из стопки отброса.
     */
    @Test
    void testReloadDeckFromDiscard() {
        Deck discard = new Deck(false);
        discard.addCard(new Card(Suit.DIAMOND, CardInfo.QUEEN));
        deck.reloadDeckFromDiscard(discard);

        assertEquals(53, deck.cardsLeft());
        assertEquals(0, discard.cardsLeft());
    }
}
