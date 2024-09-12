package ru.nsu.shelestov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * класс тестирующий класс Dealer.
 */
class DealerTest {
    private Dealer dealer;
    private Player player;
    private Deck deck;

    /**
     * Конструктор класса Dealer.
     * Инициализирует имя дилера как "Дилер"
     */
    @BeforeEach
    void setUp() {
        dealer = new Dealer();
        player = new Player();
        deck = new Deck(true);
        deck.takeCard();
        player.getHand().takeCardFromDeck(deck);
        deck.takeCard();
        player.getHand().takeCardFromDeck(deck);
    }

    /**
     * Тест инициализации дилера.
     */
    @Test
    void testDealerInitialization() {
        assertEquals("Дилер", dealer.getName(), "Имя дилера должно быть 'Дилер'");
    }

    /**
     * Печатает карты дилера в начале игры.
     * Выводит информацию о картах дилера на экран, не раскрывая их значения
     */
    @Test
    void testPrintAtStart() {
        deck.takeCard();
        dealer.getHand().takeCardFromDeck(deck);
        deck.takeCard();
        dealer.getHand().takeCardFromDeck(deck);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        dealer.printAtStart();

        System.setOut(originalOut);

        assertTrue(outContent.toString().contains("Карты дилера:"),
                "Вывод должен содержать информацию о картах дилера");
    }

    /**
     * Раскрывает карты дилера и сравнивает их с картами игрока.
     *
     * @param player Игрок, карты которого будут сравнены с картами дилера
     */
    @Test
    void testRevealHand() {
        deck.takeCard();
        dealer.getHand().takeCardFromDeck(deck);
        deck.takeCard();
        dealer.getHand().takeCardFromDeck(deck);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        dealer.revealHand(player);

        // Восстанавливаем стандартный вывод
        System.setOut(originalOut);

        // Проверяем, что вывод содержит информацию о картах игрока и дилера
        assertTrue(outContent.toString().contains("Дилер открывает карту"),
                "Вывод должен содержать сообщение о том, что дилер открывает карту");
    }
}
