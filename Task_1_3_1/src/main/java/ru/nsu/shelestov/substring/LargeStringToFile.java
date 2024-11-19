package ru.nsu.shelestov.substring;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class LargeStringToFile {
    public static void main(String[] args) {
        String filePath = "test.txt";

        long stringSize = 2L * 1024 * 1024 * 1024;

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

            long bytesWritten = 0;
            while (bytesWritten < stringSize) {
                int chunkSize = (int) Math.min(10 * 1024 * 1024, stringSize - bytesWritten);
                String chunk = generateChunk(chunkSize);
                writer.write(chunk);
                bytesWritten += chunk.getBytes(StandardCharsets.UTF_8).length;
            }
            writer.close();


        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    private static String generateChunk(int size) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append((char) ('a' + Math.random() * 26));
        }
        return sb.toString();
    }
}

