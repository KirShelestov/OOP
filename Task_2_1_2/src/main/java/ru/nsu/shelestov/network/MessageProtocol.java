package ru.nsu.shelestov.network;

import java.io.Serializable;

public class MessageProtocol implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum MessageType {
        REQUEST_TASK,
        TASK_RESULT,
        STOP_SIGNAL,
        TASK_RESPONSE,
        ERROR
    }
    
    private final MessageType type;
    private final String workerId;
    private final Serializable payload;
    
    public MessageProtocol(MessageType type, String workerId, Serializable payload) {
        this.type = type;
        this.workerId = workerId;
        this.payload = payload;
    }
    
    public MessageType getType() {
        return type;
    }
    
    public String getWorkerId() {
        return workerId;
    }
    
    public Serializable getPayload() {
        return payload;
    }
}