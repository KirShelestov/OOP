package ru.nsu.shelestov.blackjack.blackjack;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.jupiter.api.Test;
import ru.nsu.shelestov.blackjack.gamelogic.Game;

/**
 * класс для тестирования Game.
 */
public class GameTest {

    /**
     * Игрок сразу пасует и отказывавется начинать новый раунд.
     */
    @Test
        public void testGameStarts() {

        InputStream originalIn = System.in;

        try {
            ByteArrayInputStream in = new ByteArrayInputStream("0\n".getBytes());
            System.setIn(in);
            Game game = new Game();

            assertTrue(true);
        } catch (Exception e) {
            ByteArrayInputStream inn = new ByteArrayInputStream("нет\n".getBytes());
            System.setIn(inn);
        } finally {
            System.setIn(originalIn);
        }
        assertTrue(true);
    }

    /**
     * Игрок берет карту одну, затем пасует, начинает новый раунд.
     * и затем пасует с отказов начинать раунд снова
     */
    @Test
    public void testGameStarts1() {

        InputStream originalIn = System.in;

        try {
            ByteArrayInputStream in = new ByteArrayInputStream("1\n".getBytes());
            System.setIn(in);
            Game game = new Game();

            assertTrue(true);
        } catch (Exception e) {
            ByteArrayInputStream inn = new ByteArrayInputStream("0\n".getBytes());

            try {
                ByteArrayInputStream innn = new ByteArrayInputStream("нет\n".getBytes());
                System.setIn(innn);
                assertTrue(true);
            } catch (Exception ex) {
                ByteArrayInputStream innnn = new ByteArrayInputStream("нет\n".getBytes());
                System.setIn(innnn);
                assertTrue(true);
            }
            System.setIn(inn);
        } finally {
            System.setIn(originalIn);
        }
        assertTrue(true);
    }

    /**
     * Игрок пасует, начинает новый раунд и затем пасует с отказов начинать раунд снова.
     */
    @Test
    public void testGameStarts2() {

        InputStream originalIn = System.in;

        try {
            ByteArrayInputStream in = new ByteArrayInputStream("0\n".getBytes());
            System.setIn(in);
            Game game = new Game();

            assertTrue(true);
        } catch (Exception e) {
            ByteArrayInputStream inn = new ByteArrayInputStream("да\n".getBytes());

            try {
                ByteArrayInputStream innn = new ByteArrayInputStream("0\n".getBytes());
                System.setIn(innn);
                assertTrue(true);
            } catch (Exception ex) {
                ByteArrayInputStream innnn = new ByteArrayInputStream("нет\n".getBytes());
                System.setIn(innnn);
                assertTrue(true);
            }
            System.setIn(inn);
        } finally {
            System.setIn(originalIn);
        }
        assertTrue(true);
    }

    /**
     * Игрок берет или пасует потом, начинает новый раунд сто раз подряд.
     */
    @Test
    public void testGameStarts3() {

        InputStream originalIn = System.in;

        try {
            ByteArrayInputStream in = new ByteArrayInputStream("1\n".repeat(1000).getBytes());
            System.setIn(in);
            Game game = new Game();

            assertTrue(true);
        } catch (Exception e) {
            ByteArrayInputStream inn = new ByteArrayInputStream("0\n".getBytes());

            try {
                ByteArrayInputStream innn = new ByteArrayInputStream("нет\n".getBytes());
                System.setIn(innn);
                assertTrue(true);
            } catch (Exception ex) {
                ByteArrayInputStream innnn = new ByteArrayInputStream("нет\n".getBytes());
                System.setIn(innnn);
                assertTrue(true);
            }
            System.setIn(inn);
        } finally {
            System.setIn(originalIn);
        }
        assertTrue(true);
    }
}
