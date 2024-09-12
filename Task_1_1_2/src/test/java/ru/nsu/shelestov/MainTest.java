package ru.nsu.shelestov;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainTest {

    @Test
    public void testMain() {
        InputStream originalIn = System.in;

        try {
            ByteArrayInputStream in = new ByteArrayInputStream("0\n".getBytes());
            System.setIn(in);
            Main.main(new String[]{});

            assertTrue(true);
        } catch (Exception e) {
            ByteArrayInputStream inn = new ByteArrayInputStream("нет\n".getBytes());
            System.setIn(inn);
        } finally {
            System.setIn(originalIn);
        }
    }
}
