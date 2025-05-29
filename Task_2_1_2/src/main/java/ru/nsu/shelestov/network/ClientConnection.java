package ru.nsu.shelestov.network;

import ru.nsu.shelestov.task.Task;
import ru.nsu.shelestov.task.TaskResult;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class ClientConnection implements AutoCloseable {
    private static final Logger logger = Logger.getLogger(ClientConnection.class.getName());
    
    private final String host;
    private final int port;
    private final String workerId;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    
    public ClientConnection(String host, int port, String workerId) {
        this.host = host;
        this.port = port;
        this.workerId = workerId;
    }
    
    public void connect() throws IOException {
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        logger.info("Connected to server: " + host + ":" + port);
    }
    
    public Task requestTask() throws IOException, ClassNotFoundException {
        sendMessage(MessageProtocol.MessageType.REQUEST_TASK, null);
        MessageProtocol response = (MessageProtocol) in.readObject();
        
        if (response.getType() == MessageProtocol.MessageType.TASK_RESPONSE) {
            return (Task) response.getPayload();
        } else {
            throw new IOException("Unexpected response type: " + response.getType());
        }
    }
    
    public void sendResult(TaskResult result) throws IOException {
        sendMessage(MessageProtocol.MessageType.TASK_RESULT, result);
    }
    
    public void stop() throws IOException {
        sendMessage(MessageProtocol.MessageType.STOP_SIGNAL, null);
    }
    
    private void sendMessage(MessageProtocol.MessageType type, Serializable payload) throws IOException {
        MessageProtocol message = new MessageProtocol(type, workerId, payload);
        out.writeObject(message);
        out.flush();
    }
    
    @Override
    public void close() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            logger.warning("Error closing connection: " + e.getMessage());
        }
    }
}