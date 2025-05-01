package ru.nsu.shelestov.network;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum Type {
        TASK, RESULT, HEARTBEAT, REGISTER, SHUTDOWN
    }
    
    private final Type type;
    private final int[] data;
    private final int startIndex;
    private final int endIndex;
    private final boolean result;
    
    public Message(Type type, int[] data, int startIndex, int endIndex) {
        this.type = type;
        this.data = data;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.result = false;
    }
    
    public Message(Type type, boolean result) {
        this.type = type;
        this.data = null;
        this.startIndex = 0;
        this.endIndex = 0;
        this.result = result;
    }
    
    public Type getType() { return type; }
    public int[] getData() { return data; }
    public int getStartIndex() { return startIndex; }
    public int getEndIndex() { return endIndex; }
    public boolean getResult() { return result; }
}