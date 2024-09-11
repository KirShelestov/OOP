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
            ByteArrayInputStream in = new ByteArrayInputStream("1\n".repeat(100).getBytes());
            System.setIn(in);

            Main.main(new String[]{});

            assertTrue(true);
        } finally {
            System.setIn(originalIn);
        }
    }
}
