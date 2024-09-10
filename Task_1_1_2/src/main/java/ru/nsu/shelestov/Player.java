package ru.nsu.shelestov;

import java.util.Scanner;

/**
 * Данный класс определяет поведение пользователя.
 */
public class Player extends Person {

    Scanner input = new Scanner(System.in);

    /**
     * конструктор для последующего вывода карт на руке у игрока.
     */
    public Player() {
        super.setName("Ваши карты: ");
    }

    /**
     * функция для определение поведения игрока.
     * Если пользователь выбирает "0", то прекращаем брать карту
     * Иначе берем карту, и сразу проверяем выпал ли нам блэкджек
     * Если да, то заканчиваем игру.
     * Иначе рекурсивно вызываем функцию для определения поведения игрока.
     * Попутно отлавливаем случай, когда пользователь вводит отличное от "0"/"1"
     * В таком кейсе снова будет предложено ввести выбор
     *
     * @param deck
     * @param discard
     */
    public void makeDecision(Deck deck, Deck discard) {
        int decision = 0;
        boolean getNum = true;

        while (getNum) {
            try {
                System.out.println("Введите “1”, чтобы взять карту, и “0”, чтобы остановиться...");
                decision = input.nextInt();
                if ((decision == 1) || (decision == 0)) {
                    getNum = false;
                }
            } catch (Exception e) {
                System.out.println("Введите “1“ или “0“");
                input.next();
            }
        }
        if (decision == 1) {
            this.hit(deck, discard);
            if (this.getHand().getValueOnHand() > 20) {
                return;
            } else {
                this.makeDecision(deck, discard);
            }
        } else {
            System.out.println("");
        }
    }

    /**
     * функция для вывода взятой карты
     */
    public void revealHand() {
        System.out.println("Вы открыли карту " + super.getHand().getCard(super.getHand().getSize() - 1));
        super.printHand();
    }


}
