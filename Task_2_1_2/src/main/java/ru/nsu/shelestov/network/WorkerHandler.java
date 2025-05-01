package ru.nsu.shelestov.network;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;

public class WorkerHandler {
    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    public WorkerHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    public CompletableFuture<Boolean> processChunk(int[] data, int start, int end) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Message task = new Message(Message.Type.TASK, data, start, end);
                sendMessage(task);
                Message response = (Message) in.readObject();
                return response.getResult();
            } catch (Exception e) {
                throw new RuntimeException("Error processing chunk", e);
            }
        });
    }

    public void sendMessage(Message message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error sending message", e);
        }
    }

    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}