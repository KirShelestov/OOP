package ru.nsu.shelestov;

import java.util.Objects;

/**
 * класс для определения общих характеристик как Дилера, так и Игрока.
 */
public abstract class Person {

    String name;
    Hand hand;

    /**
     * конструктор  для персона.
     */
    public Person() {
        this.name = "";
        this.hand = new Hand();
    }

    /**
     * геттер для персона.
     *
     * @return возвращаем имя персона
     */
    public String getName() {
        return this.name;
    }

    /**
     * сеттер для персона.
     *
     * @param name устанавливаем имя персона
     */
    public void setName(String name) {
        this.name = name;

    }

    /**
     * геттер для "руки" - карт у персона.
     *
     * @return карты на руке
     */
    public Hand getHand() {
        return this.hand;
    }

    /**
     * сеттер руки.
     *
     * @param hand рука, которой устанавливают значение
     */
    public void setHand(Hand hand) {
        this.hand = hand;
    }

    /**
     * печатает карты на руке у персона.
     */
    public void printHand() {
        if (Objects.equals(this.name, "Дилер")) {
            System.out.print("Карты дилера: ");
        } else {
            System.out.print("Ваши карты: ");
        }
        System.out.println(this.hand + " => " + this.hand.getValueOnHand());
    }

    /**
     * проверяет, если на руках у нас "21", то значит у нас блэкджек.
     *
     * @return True если есть блэкджек
     */
    public boolean winBlackjack() {
        if (this.getHand().getValueOnHand() == 21) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Берем карту из колоды.
     * Добавляем ее в руку
     * Если колода пустая, то обновляем колоду из стопки сброса
     *
     * @param deck    колода откуда берем карту
     * @param discard стпока сброса
     */
    public void hit(Deck deck, Deck discard) {
        if (!deck.hasCards()) {
            deck.reloadDeckFromDiscard(discard);
        }
        this.hand.takeCardFromDeck(deck);
    }
}
