package ru.nsu.shelestov.blackjack.players;

/**
 * данный класс представляет поведение дилера.
 */
public class Dealer extends Person {

    /**
     * конструктор для дилера.
     */
    public Dealer() {
        super.setName("Дилер");
    }

    /**
     * метод выводит руку Дилера до того момента, как начнется ход Дилера.
     */
    public void printAtStart() {
        System.out.print("Карты дилера: ");
        System.out.print("[" + super.getHand().getCard(0) + ", ");
        System.out.println("<закрытая карта>]");
    }

    /**
     * метод сначала выводит какую карту взял дилер.
     * затем выводит последовательно карты на руках у игрока и дилера
     *
     * @param rival используется для того, чтобы выводить карты противника
     */
    public void revealHand(Person rival) {
        System.out.println("Дилер открывает карту "
                + super.getHand().getCard(super.getHand().getSize() - 1));
        rival.printHand();
        super.printHand();
    }
}
