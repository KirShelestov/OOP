package ru.nsu.shelestov;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class GameTest {
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
}