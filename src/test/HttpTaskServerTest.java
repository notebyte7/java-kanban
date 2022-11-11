package test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import manager.CrossingTaskException;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.*;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static tasks.Status.NEW;


class HttpTaskServerTest {
    private Gson gson = new Gson();
    private HttpTaskServer taskServer;
    private TaskManager taskManager;
    private Task defaultTask;
    private Epic defaultEpic;

    private Subtask defaultSubtask;

    @BeforeEach
    void startServer() throws IOException, CrossingTaskException {
        new KVServer().start();

        taskManager = Managers.getDefault();
        taskServer = new HttpTaskServer(taskManager);

        defaultTask = new Task("Test addNewTask", "Test addNewTask description", 1, NEW,
                LocalDateTime.of(2022, 11, 1, 12, 00), 30);
        taskManager.createTask(defaultTask);

        defaultEpic = new Epic("Test epic", "Test tasks.Epic description", 2, NEW);
        taskManager.createEpic(defaultEpic);

        defaultSubtask = new Subtask("Test subtask", "Test subtask description", 3, NEW,
                LocalDateTime.of(2022, 11, 1, 13, 00), 30, 2);
        taskManager.createSubtask(defaultSubtask);
        taskServer.start();

    }

    @AfterEach
    void stopServer() throws IOException {
        taskServer.stop();

    }

    @Test
    void save() {
        //taskManager = load();
    }

    @Test
    void load() {

    }

    @Test
    void getAllTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "неправильный код ответа");

        Type task = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> tasks = gson.fromJson(response.body(), task);

        assertNotNull(tasks, "список задач пуст");
        assertEquals(1, tasks.size());
        assertEquals(defaultTask, tasks.get(0));
    }
}