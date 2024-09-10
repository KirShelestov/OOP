package ru.nsu.shelestov;
import java.util.ArrayList;

/**
 *класс представляет колоду игральных карт.
 */
public class Deck {
	ArrayList<Card> deck;

	/**
	 * создает пустую колоду кард.
	 */
	public Deck() {
		deck = new ArrayList<Card>();
	}

	/**
	 * возвращает список карт в колоде.
	 *
	 * @return ArrayList содержащий карты в колоде
	 */
	public ArrayList<Card> getCards() {
		return deck;
	}

	/**
	 * добавляет карту в колоду.
	 *
	 * @param card карта, которую нужно добавить в колоду
	 */
	public void addCard(Card card) {
		deck.add(card);
	}

	/**
	 * Метод используется для вывода строкового представления колоды.
	 *
	 * @return строка, содержащая все карты в колоде
	 */
	public String toString() {
		String output = "";
		
		for (Card card : deck) {
			output += card;
			output += "\n";
		}
		
		return output;
	}

	/**
	 * конструктор колоды, при необходимости также заполняет колоду картами.
	 *
	 * @param createDeck при True колода заполняется полным наборм карт
	 */
	public Deck(boolean createDeck) {
		deck = new ArrayList<Card>();
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
	public void shuffle(){
	    ArrayList<Card> shuffled = new ArrayList<Card>();
	    while(deck.size() > 0){
	        int cardToPull = (int)(Math.random()*(deck.size()-1));
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
		Card cardToTake = new Card(deck.get(0));
		deck.remove(0);
		return cardToTake;
	}

	/**
	 *Проверяет наличие карты в колоде.
	 *
	 * @return True если есть карты, иначе False
	 */
	public boolean hasCards() {
		return (deck.size() > 0);
	}

	/**
	 * удаляет карты из колоды.
	 */
	public void emptyDeck(){
	    deck.clear();
	}

	/**
	 * добавляет список карт в колоду.
	 *
	 * @param cards ArrayList карты, которые нужно добавить в колоду
	 */
	public void addCards(ArrayList<Card> cards){
	    deck.addAll(cards);
	}

	/**
	 * Берет колоду из стопки сброса, перемешивает ее и очищает стопку сброса.
	 * Метод нужен для обновления колоды
	 *
	 * @param discard стопка сброса, откуда берем карты для обновления колоды
	 */
	public void reloadDeckFromDiscard(Deck discard){
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
	public int cardsLeft(){
        return deck.size();
    }
}
