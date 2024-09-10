package ru.nsu.shelestov;

import java.util.Scanner;

/**
 * класс представляет логику игры.
 */
public class Game {
    private Deck deck, discarded;
    private Dealer dealer;
    private Player player;
    private int wins, losses, draws, start;
    private int round;

    /**
     * конструктор для инициализации игры.
     */
    public Game() {
        start = 0;
        wins = 0;
        losses = 0;
        draws = 0;
        round = 1;

        deck = new Deck(true);
        discarded = new Deck();

        dealer = new Dealer();
        player = new Player();

        deck.shuffle();
        System.out.println("Добро пожаловать в Блэкджек!");
        startRound();
    }

    /**
     * Запускает новый раунд игры в блэкджек.
     * <p>
     * Сброс карт дилера и игрока в колоду, если это не первый раунд
     * Проверка количества оставшихся карт в колоде и перетасовка колоды из сброшенных карт, если необходимо
     * Раздача карт игроку и дилеру
     * Проверка на наличие блэкджека у игрока
     * Ход игрока с возможностью взять карту или остановиться.
     * Если игрок не превысил 21, ходит дилер
     * Определение результата раунда и обновление счета
     * Запрос на продолжение игры в следующем раунде
     * Если игрок решает не продолжать, выводится итоговый счет.
     */
    public void startRound() {
        if (round > 1) {
            System.out.println("\nРаунд " + round);
        }

        dealer.getHand().discardHandToDeck(discarded);
        player.getHand().discardHandToDeck(discarded);

        if (deck.cardsLeft() < 4) {
            deck.reloadDeckFromDiscard(discarded);
        }

        System.out.println("Дилер раздал карты");
        dealInitialCards();

        player.printHand();
        dealer.printAtStart();

        if (checkBlackjack()) {
            return;
        }

        playerTurn();
        if (player.getHand().getValueOnHand() > 21) {
            updateScore(false);
            return;
        }

        dealerTurn();
        determineOutcome();

        round++;
        System.out.println("Хотите сыграть еще один раунд? (да/нет)");
        Scanner scanner = new Scanner(System.in);
        String response = scanner.nextLine().trim().toLowerCase();

        if (response.equals("да")) {
            startRound();
        } else {
            System.out.println("Спасибо за игру! Ваш счет: " + wins + " побед, " + losses + " поражений, " + draws + " ничьих.");
        }
    }

    /**
     * раздается изначально по две карты дилеру и игроку.
     */
    private void dealInitialCards() {
        dealer.getHand().takeCardFromDeck(deck);
        dealer.getHand().takeCardFromDeck(deck);
        player.getHand().takeCardFromDeck(deck);
        player.getHand().takeCardFromDeck(deck);
    }

    /**
     * проверка на наличие блэкджека у дилера и у игрока с обновлением счета.
     *
     * @return true если хотя бы у кого-то есть блэкджек
     */
    private boolean checkBlackjack() {
        if (dealer.winBlackjack()) {
            dealer.printHand();
            if (player.winBlackjack()) {
                System.out.println("Ничья!");
                draws++;
                return true;
            } else {
                System.out.println("Дилер выиграл с блэкджеком!");
                losses++;
                return true;
            }
        } else if (player.winBlackjack()) {
            System.out.println("Вы выиграли с блэкджеком!");
            wins++;
            return true;
        }
        return false;
    }

    /**
     * Обрабатывает ход игрока в игре блэкджек.
     * <p>
     * Выводит сообщение о текущем ходе и предлагает игроку ввести выбор
     * Если игрок выбирает взять карту (вводит "1")
     * Если это первый ход (start == 0), игрок получает карту и показывает свою руку
     * Если это не первый ход (start == 1), игрок также получает карту, но показывается рука дилера
     * Если игрок выбирает остановиться (вводит "0"), устанавливается флаг для перехода к следующему этапу игры
     * Если введено неверное значение, выводится сообщение об ошибке, и игроку предлагается попробовать снова
     */
    private void playerTurn() {
        while (true) {
            System.out.println("Ваш ход\n-------");
            System.out.println("Введите “1”, чтобы взять карту, и “0”, чтобы остановиться.");
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();

            if ((choice == 1) && (start == 0)) {
                player.hit(deck, discarded);
                player.revealHand();
                dealer.printAtStart();
                if (player.getHand().getValueOnHand() > 21) {
                    System.out.println("Вы превысили 21! Вы проиграли.");
                    break;
                }
            } else if ((choice == 1) && (start == 1)) {
                player.hit(deck, discarded);
                player.revealHand();
                dealer.printHand();
                if (player.getHand().getValueOnHand() > 21) {
                    System.out.println("Вы превысили 21! Вы проиграли.");
                    break;
                }
            } else if (choice == 0) {
                start = 1;
                break;
            } else {
                System.out.println("Неверный ввод. Попробуйте снова.");
            }
        }
    }

    /**
     * Описывает логику хода Дилера.
     * <p>
     * Дилер раскрывает скрытую карту
     * Берет затем карты пока значение на руках меньше 17
     */
    private void dealerTurn() {
        System.out.println("Ход дилера\n-------");
        dealer.printAtStart();
        start = 1;

        dealer.revealHand(player);

        while (dealer.getHand().getValueOnHand() < 17) {
            dealer.hit(deck, discarded);
            dealer.revealHand(player);
        }
    }

    /**
     * определяется исход раунда на основе значений на руках у игрока и дилера.
     * <p>
     * Также обновляется правильно счет игры
     * Затем выводится результат в консоль
     */
    private void determineOutcome() {
        int dealerValue = dealer.getHand().getValueOnHand();
        int playerValue = player.getHand().getValueOnHand();

        if (dealerValue > 21) {
            wins++;
            System.out.println("Дилер перебрал! Вы выиграли раунд! Счет " + wins + ":" + losses);
        } else if (dealerValue > playerValue) {
            losses++;
            System.out.println("Вы проиграли раунд! Счет " + wins + ":" + losses);
        } else if (playerValue > dealerValue) {
            wins++;
            System.out.println("Вы выиграли раунд! Счет " + wins + ":" + losses);
        } else {
            draws++;
            System.out.println("Ничья! Счет " + wins + ":" + losses);
        }
    }

    /**
     * Выводится результат раунда.
     *
     * @param playerWins True если выйграл игрок
     */
    private void updateScore(boolean playerWins) {
        if (playerWins) {
            wins++;
            System.out.println("Вы выиграли раунд! Счет " + wins + ":" + losses);
        } else {
            losses++;
            System.out.println("Вы проиграли раунд! Счет " + wins + ":" + losses);
        }
    }
}
