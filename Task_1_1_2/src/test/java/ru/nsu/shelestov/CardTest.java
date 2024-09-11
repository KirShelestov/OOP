package ru.nsu.shelestov;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 *  класс для теста класса Card.
 */
public class CardTest {

    @Test
    public void testCardValue() {
        Card card = new Card(Suit.HEART, CardInfo.ACE);
        assertEquals(11, card.getValue());
    }

    /**
     * тест для проверки метода toString.
     */
    @Test
    public void testCardToString() {
        Card card = new Card(Suit.SPADE, CardInfo.KING);
        assertEquals("Пиковый король (10)", card.toString());
    }

    /**
     * тест для конструктора внутри класса.
     */
    @Test
    public void testCardCopyConstructor() {
        Card original = new Card(Suit.CLUB, CardInfo.QUEEN);
        Card copy = new Card(original);
        assertEquals(original.getSuit(), copy.getSuit());
        assertEquals(original.getCardInfo(), copy.getCardInfo());
    }
}
