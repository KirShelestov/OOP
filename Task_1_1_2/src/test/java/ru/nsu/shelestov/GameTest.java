package ru.nsu.shelestov;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameTest {

    @Test
    public void testGameStarts() {

        InputStream originalSystemIn = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("1\n".repeat(100).getBytes());
        System.setIn(in);
        Game game = new Game();

        System.setIn(originalSystemIn);


        assertTrue(true);
    }
}

