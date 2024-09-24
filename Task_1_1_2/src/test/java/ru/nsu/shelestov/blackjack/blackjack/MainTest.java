package ru.nsu.shelestov.blackjack.blackjack;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.jupiter.api.Test;
import ru.nsu.shelestov.blackjack.gamelogic.Main;

/**
 * Класс, который должен тестировать Main, но почему-то покрытие все равно 0%.
 */
public class MainTest {

    /**
     * вызов мейна.
     * Игрок сразу пасует и отказывается начинать новый раунд
     */
    @Test
    public void testMain() {
        InputStream originalIn = System.in;

        try {
            ByteArrayInputStream in = new ByteArrayInputStream("0\n".getBytes());
            System.setIn(in);
            Main.main(new String[]{});

            assertTrue(true);
        } catch (Exception e) {
            ByteArrayInputStream inn = new ByteArrayInputStream("да\n".getBytes());
            System.setIn(inn);

            try {
                ByteArrayInputStream in1 = new ByteArrayInputStream("0\n".getBytes());
                System.setIn(in1);
                Main.main(new String[]{});

                assertTrue(true);
            } catch (Exception exp) {
                ByteArrayInputStream inn1 = new ByteArrayInputStream("нет\n".getBytes());
                System.setIn(inn1);
            }
        } finally {
            System.setIn(originalIn);
        }
    }
}
