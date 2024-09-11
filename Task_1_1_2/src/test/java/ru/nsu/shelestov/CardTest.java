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
     * тест для проверки метода toString для KING.
     */
    @Test
    public void testCardToStringKING() {
        Card card1 = new Card(Suit.SPADE, CardInfo.KING);
        assertEquals("Пиковый король (10)", card1.toString());
        Card card2 = new Card(Suit.CLUB, CardInfo.KING);
        assertEquals("Трефовый король (10)", card2.toString());
        Card card3 = new Card(Suit.HEART, CardInfo.KING);
        assertEquals("Червовый король (10)", card3.toString());
        Card card4 = new Card(Suit.DIAMOND, CardInfo.KING);
        assertEquals("Бубновый король (10)", card4.toString());
    }

    /**
     * тест для проверки метода toString для KING.
     */
    @Test
    public void testCardToStringQUEEN() {
        Card card1 = new Card(Suit.SPADE, CardInfo.QUEEN);
        assertEquals("Пиковая дама (10)", card1.toString());
        Card card2 = new Card(Suit.CLUB, CardInfo.QUEEN);
        assertEquals("Трефовая дама (10)", card2.toString());
        Card card3 = new Card(Suit.HEART, CardInfo.QUEEN);
        assertEquals("Червовая дама (10)", card3.toString());
        Card card4 = new Card(Suit.DIAMOND, CardInfo.QUEEN);
        assertEquals("Бубновая дама (10)", card4.toString());
    }

    /**
     * тест для проверки метода toString для JACK.
     */
    @Test
    public void testCardToStringJACK() {
        Card card1 = new Card(Suit.SPADE, CardInfo.JACK);
        assertEquals("Пиковый валет (10)", card1.toString());
        Card card2 = new Card(Suit.CLUB, CardInfo.JACK);
        assertEquals("Трефовый валет (10)", card2.toString());
        Card card3 = new Card(Suit.HEART, CardInfo.JACK);
        assertEquals("Червовый валет (10)", card3.toString());
        Card card4 = new Card(Suit.DIAMOND, CardInfo.JACK);
        assertEquals("Бубновый валет (10)", card4.toString());
    }
    /**
     * тест для проверки метода toString для остальных.
     */
    @Test
    public void testCardToString() {
        Card card1 = new Card(Suit.SPADE, CardInfo.ACE);
        assertEquals("Туз Пики (11)", card1.toString());
    }

    /**
     * тест для проверки метода getValue.
     */
    @Test
    public void testGetValue() {
        Card card = new Card(Suit.SPADE, CardInfo.KING);
        assertEquals(10, card.getValue());
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
