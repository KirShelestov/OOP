package ru.nsu.shelestov.network;

import ru.nsu.shelestov.prime.PrimeChecker;
import java.net.Socket;
import java.io.*;

public class WorkerNode {
    private static final int PORT = 8080;
    private static final String HOST = "localhost";
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    
    public void start() {
        try {
            socket = new Socket(HOST, PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            
            System.out.println("Worker successfully connected to master");
            
            // Register with master
            sendMessage(new Message(Message.Type.REGISTER, false));
            
            // Main processing loop
            while (true) {
                Message message = (Message) in.readObject();
                
                if (message.getType() == Message.Type.SHUTDOWN) {
                    System.out.println("Worker received shutdown command");
                    break;
                }
                
                if (message.getType() == Message.Type.TASK) {
                    System.out.println("Worker processing chunk from " + message.getStartIndex() + " to " + message.getEndIndex());
                    processTask(message);
                }
            }
        } catch (Exception e) {
            System.err.println("Worker error: " + e.getMessage());
        } finally {
            cleanup();
        }
    }
    
    private void processTask(Message message) throws IOException {
        int[] data = message.getData();
        int start = message.getStartIndex();
        int end = message.getEndIndex();
        
        boolean hasComposite = false;
        for (int i = start; i < end; i++) {
            boolean isPrime = PrimeChecker.isPrime(data[i]);
            System.out.println("Worker checking number " + data[i] + ": " + 
                             (isPrime ? "prime" : "composite"));
            if (!isPrime) {
                hasComposite = true;
                break;
            }
        }
        
        System.out.println("Worker sending result: " + hasComposite + 
                         " for chunk " + start + " to " + end);
        sendMessage(new Message(Message.Type.RESULT, hasComposite));
    }
    
    private void sendMessage(Message message) throws IOException {
        out.writeObject(message);
        out.flush();
    }
    
    private void cleanup() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}