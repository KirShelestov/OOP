package ru.nsu.shelestov.network;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class MessageProtocolTest {
    @Test
    void testMessageCreation() {
        String workerId = "worker1";
        String payload = "test-payload";
        MessageProtocol message = new MessageProtocol(
            MessageProtocol.MessageType.REQUEST_TASK, 
            workerId, 
            payload
        );

        assertEquals(MessageProtocol.MessageType.REQUEST_TASK, message.getType());
        assertEquals(workerId, message.getWorkerId());
        assertEquals(payload, message.getPayload());
    }

    @Test
    void testSerialization() throws Exception {
        MessageProtocol original = new MessageProtocol(
            MessageProtocol.MessageType.TASK_RESULT,
            "worker1",
            "result-data"
        );

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(original);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        MessageProtocol deserialized = (MessageProtocol) ois.readObject();

        assertEquals(original.getType(), deserialized.getType());
        assertEquals(original.getWorkerId(), deserialized.getWorkerId());
        assertEquals(original.getPayload(), deserialized.getPayload());
    }
}