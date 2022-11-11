package test;

import manager.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import server.KVServer;

import java.io.IOException;

class HttpTaskManagerTest extends TaskManagerTest<TaskManager> {
    private HttpTaskServer taskServer;
    private KVServer kvServer = new KVServer();

    public HttpTaskManagerTest() throws IOException, CrossingTaskException {
        super(Managers.getDefault());
    }

    @BeforeEach
    void setUp() throws IOException, CrossingTaskException {
        kvServer.start();
        taskServer = new HttpTaskServer(taskManager);
        taskManager.load();
        taskServer.start();
    }

    @AfterEach
    void tearDown() throws IOException {
        taskServer.stop();
        kvServer.stop();
    }

    @Test
    void test() throws CrossingTaskException {
    }
}