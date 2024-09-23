package ru.nsu.shelestov.gamelogic;

import java.util.ArrayList;
import java.util.List;
import ru.nsu.shelestov.card.Card;
import ru.nsu.shelestov.card.CardInfo;
import ru.nsu.shelestov.card.Suit;

/**
 * класс представляет колоду игральных карт.
 */
public class Deck {
    ArrayList<Card> deck;

    /**ArrayList
     * создает пустую колоду кард.
     */
    public Deck() {
        deck = new ArrayList<>();
    }

    /**
     * возвращает список карт в колоде.
     *
     * @return List содержащий карты в колоде
     */
    public List<Card> getCards() {
        return deck;
    }

    /**
     * добавляет список карт в колоду.
     *
     * @param cards List карты, которые нужно добавить в колоду
     */
    public void addCards(List<Card> cards) {
        deck.addAll(cards);
    }

    /**
     * Метод используется для вывода строкового представления колоды.
     *
     * @return строка, содержащая все карты в колоде
     */
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        for (Card card : deck) {
            output.append(card);
            output.append("\n");
        }

        return output.toString();
    }

    /**
     * конструктор колоды, при необходимости также заполняет колоду картами.
     *
     * @param createDeck при True колода заполняется полным наборм карт
     */
    public Deck(boolean createDeck) {
        deck = new ArrayList<>();
        if (createDeck) {
            for (Suit suit : Suit.values()) {
                for (CardInfo cardInfo : CardInfo.values()) {
                    deck.add(new Card(suit, cardInfo));
                }
            }
        }
    }

    /**
     * Перемещивает карты в колоде.
     */
    public void shuffle() {
        ArrayList<Card> shuffled = new ArrayList<>();
        while (!deck.isEmpty()) {
            int cardToPull = (int) (Math.random() * (deck.size() - 1));
            shuffled.add(deck.get(cardToPull));
            deck.remove(cardToPull);
        }
        deck = shuffled;
    }

    /**
     * Карта на вершине колоды берется в руки и удаляется из вершины колоды.
     *
     * @return карту, которую взяли
     */
    public Card takeCard() {
        if (deck.isEmpty()) {
            throw new IllegalStateException("Колода пуста, невозможно взять карту.");
        }
        Card cardToTake = new Card(deck.get(0));
        deck.remove(0);
        return cardToTake;
    }

    /**
     * Проверяет наличие карты в колоде.
     *
     * @return True если есть карты, иначе False
     */
    public boolean hasCards() {
        return !deck.isEmpty();
    }

    /**
     * удаляет карты из колоды.
     */
    public void emptyDeck() {
        deck.clear();
    }

    /**
     * Берет колоду из стопки сброса, перемешивает ее и очищает стопку сброса.
     * Метод нужен для обновления колоды
     *
     * @param discard стопка сброса, откуда берем карты для обновления колоды
     */
    public void reloadDeckFromDiscard(Deck discard) {
        this.addCards(discard.getCards());
        this.shuffle();
        discard.emptyDeck();
        System.out.println("Закончились карты");
    }

    /**
     * метод для отображения сколько карт осталось в колоде.
     *
     * @return количество карт в колоде
     */
    public int cardsLeft() {
        return deck.size();
    }

    /**
     * добавляет карту в колоду.
     *
     * @param card карта, которую нужно добавить в колоду
     */
    public void addCard(Card card) {
        deck.add(card);
    }
}
