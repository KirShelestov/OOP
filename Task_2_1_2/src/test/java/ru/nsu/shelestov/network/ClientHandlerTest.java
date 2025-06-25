package ru.nsu.shelestov.network;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.nsu.shelestov.task.Task;
import ru.nsu.shelestov.task.TaskManager;
import ru.nsu.shelestov.task.TaskResult;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientHandlerTest {
    @Mock private Socket socket;
    @Mock private TaskManager taskManager;
    @Mock private Task mockTask;
    
    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream inputStream;
    private ClientHandler handler;

    @BeforeEach
    void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(outputStream);
        
        ByteArrayOutputStream initialInput = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(initialInput);
        oos.writeObject(new MessageProtocol(
            MessageProtocol.MessageType.REQUEST_TASK,
            "worker1",
            null
        ));
        oos.flush(); 
        
        inputStream = new ByteArrayInputStream(initialInput.toByteArray());
        when(socket.getInputStream()).thenReturn(inputStream);
        
        handler = new ClientHandler(socket, taskManager);
    }

    @Test
    void testHandleRequestTask() throws Exception {
        Task realTask = new Task(new int[]{1, 2, 3}, 0, 2);
        when(taskManager.getNextTask(anyString())).thenReturn(realTask);
        
        handler.run();
        
        verify(taskManager).getNextTask("worker1");
        
        System.out.println("Output stream size: " + outputStream.size());
        
        ObjectInputStream responseStream = new ObjectInputStream(
            new ByteArrayInputStream(outputStream.toByteArray())
        );
        MessageProtocol response = (MessageProtocol) responseStream.readObject();
        
        assertNotNull(response, "Response should not be null");
        assertEquals(MessageProtocol.MessageType.TASK_RESPONSE, response.getType(), 
            "Response type should be TASK_RESPONSE");
        assertEquals("worker1", response.getWorkerId(), 
            "Worker ID should match");
        
        Object payload = response.getPayload();
        assertNotNull(payload, "Response payload should not be null");
        assertTrue(payload instanceof Task, "Payload should be a Task");
        
        Task receivedTask = (Task) payload;
        assertArrayEquals(new int[]{1, 2, 3}, receivedTask.getNumbers(), 
            "Task numbers should match");
        assertEquals(0, receivedTask.getStartIndex(), 
            "Start index should match");
        assertEquals(2, receivedTask.getEndIndex(), 
            "End index should match");
    }
}