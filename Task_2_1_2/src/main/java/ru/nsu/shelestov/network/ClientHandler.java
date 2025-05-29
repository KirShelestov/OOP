package ru.nsu.shelestov.network;

import ru.nsu.shelestov.task.Task;
import ru.nsu.shelestov.task.TaskManager;
import ru.nsu.shelestov.task.TaskResult;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class ClientHandler implements Runnable {
    private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());
    
    private final Socket clientSocket;
    private final TaskManager taskManager;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    
    public ClientHandler(Socket socket, TaskManager taskManager) {
        this.clientSocket = socket;
        this.taskManager = taskManager;
    }
    
    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
            
            while (!clientSocket.isClosed()) {
                MessageProtocol message = (MessageProtocol) in.readObject();
                handleMessage(message);
            }
            
        } catch (IOException | ClassNotFoundException e) {
            logger.warning("Error handling client: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }
    
    private void handleMessage(MessageProtocol message) throws IOException {
        switch (message.getType()) {
            case REQUEST_TASK:
                Task task = taskManager.getNextTask(message.getWorkerId());
                sendMessage(MessageProtocol.MessageType.TASK_RESPONSE, message.getWorkerId(), task);
                break;
                
            case TASK_RESULT:
                TaskResult result = (TaskResult) message.getPayload();
                taskManager.processResult(result);
                break;
                
            case STOP_SIGNAL:
                closeConnection();
                break;
                
            default:
                sendMessage(MessageProtocol.MessageType.ERROR, message.getWorkerId(), 
                    "Unknown message type: " + message.getType());
        }
    }
    
    private void sendMessage(MessageProtocol.MessageType type, String workerId, Serializable payload) 
            throws IOException {
        MessageProtocol response = new MessageProtocol(type, workerId, payload);
        out.writeObject(response);
        out.flush();
    }
    
    private void closeConnection() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            logger.warning("Error closing client connection: " + e.getMessage());
        }
    }
}