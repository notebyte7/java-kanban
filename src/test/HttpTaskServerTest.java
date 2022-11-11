package test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import manager.CrossingTaskException;
import manager.*;
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
import static tasks.Status.*;


class HttpTaskServerTest {
    TaskManager taskManager;
    HttpTaskServer taskServer;
    Gson gson;
    Task defaultTask;
    Epic defaultEpic;
    Subtask defaultSubtask;

    KVServer kvServer;

    HttpTaskServerTest() throws IOException {
        this.taskManager = Managers.getDefault();
        this.taskServer = new HttpTaskServer(taskManager);
        this.kvServer = new KVServer();
        this.gson = new Gson();
    }

    @BeforeEach
    void startServer() throws IOException, CrossingTaskException {
        kvServer.start();
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
        kvServer.stop();
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
        assertEquals(1, tasks.size(), "размер списка не совпадает");
        assertEquals(defaultTask, tasks.get(0), "таски не совпадают");
    }

    @Test
    void getTaskbyId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "неправильный код ответа");

        Task task = gson.fromJson(response.body(), Task.class);

        assertNotNull(task, "задач отсутствует");
        assertEquals(task, defaultTask);
    }

    @Test
    void addTask() throws IOException, InterruptedException {
        Task tempTask = new Task("Test addNewTask", "Test addNewTask description", 4, NEW,
                LocalDateTime.of(2022, 11, 3, 12, 00), 30);
        String value = gson.toJson(tempTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(value)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/task/?id=4");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "неправильный код ответа");

        Task task = gson.fromJson(response.body(), Task.class);

        assertEquals(task, tempTask);
    }

    @Test
    void updateTask() throws IOException, InterruptedException {
        Task tempTask = new Task("Test addNewTask", "Test addNewTask description", 4, NEW,
                LocalDateTime.of(2022, 11, 4, 12, 00), 30);
        String value = gson.toJson(tempTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(value)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/task/?id=4");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "неправильный код ответа");

        Task task = gson.fromJson(response.body(), Task.class);

        assertEquals(task, tempTask);
    }

    @Test
    void deleteTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/task/?id=1");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode(), "неправильный код ответа");
    }

    @Test
    void deleteAllTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/task/");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Type task = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> tasks = gson.fromJson(response.body(), task);
        assertEquals(0, tasks.size(), "размер списка не совпадает");
    }

    @Test
    void getAllEpics() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "неправильный код ответа");

        Type epic = new TypeToken<ArrayList<Epic>>() {
        }.getType();
        List<Task> epics = gson.fromJson(response.body(), epic);

        assertNotNull(epics, "список задач пуст");
        assertEquals(1, epics.size(), "размер списка не совпадает");
        assertEquals(defaultEpic, epics.get(0), "таски не совпадают");
    }

    @Test
    void getEpicById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "неправильный код ответа");

        Epic epic = gson.fromJson(response.body(), Epic.class);

        assertNotNull(epic, "эпик пуст");
        assertEquals(epic, defaultEpic, "не совпадают");
    }

    @Test
    void addEpic() throws IOException, InterruptedException {
        Epic testEpic = new Epic("Test epic", "Test tasks.Epic description", 5, NEW);

        String value = gson.toJson(testEpic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(value)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/epic/?id=5");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "неправильный код ответа");

        Epic epic = gson.fromJson(response.body(), Epic.class);

        assertEquals(epic, testEpic);
    }

    @Test
    void updateEpic() throws IOException, InterruptedException {
        Epic testEpic = new Epic("Test epic", "Test tasks.Epic description", 5, NEW);
        String value = gson.toJson(testEpic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(value)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/epic/?id=5");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "неправильный код ответа");

        Epic epic = gson.fromJson(response.body(), Epic.class);

        assertNotNull(epic, "список задач пуст");
        assertEquals(epic, testEpic);
    }

    @Test
    void deleteEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/epic/?id=2");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode(), "неправильный код ответа");
    }

    @Test
    void deleteAllEpics() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/epic/");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Type epic = new TypeToken<ArrayList<Epic>>() {
        }.getType();
        List<Epic> epics = gson.fromJson(response.body(), epic);
        assertEquals(0, epics.size(), "размер списка не совпадает");
    }

    @Test
    void getAllSubtask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "неправильный код ответа");

        Type subtask = new TypeToken<ArrayList<Subtask>>() {
        }.getType();
        List<Task> subtasks = gson.fromJson(response.body(), subtask);

        assertNotNull(subtasks, "список задач пуст");
        assertEquals(1, subtasks.size(), "размер списка не совпадает");
        assertEquals(defaultSubtask, subtasks.get(0), "таски не совпадают");
    }

    @Test
    void getSubtaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "неправильный код ответа");

        Subtask subtask = gson.fromJson(response.body(), Subtask.class);

        assertNotNull(subtask, "сабтаск пуст");
        assertEquals(subtask, defaultSubtask, "не совпадают");
    }

    @Test
    void addSubtask() throws IOException, InterruptedException {
        Subtask testSubtask = new Subtask("Test subtask", "Test subtask description", 6, NEW,
                LocalDateTime.of(2022, 11, 7, 13, 00), 30, 2);

        String value = gson.toJson(testSubtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(value)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/subtask/?id=6");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "неправильный код ответа");

        Subtask subtask = gson.fromJson(response.body(), Subtask.class);

        assertNotNull(subtask, "список задач пуст");
        assertEquals(subtask, testSubtask);
    }

    @Test
    void updateSubtask() throws IOException, InterruptedException {
        Subtask testSubtask = new Subtask("Test subtask", "Test subtask description", 6, NEW,
                LocalDateTime.of(2022, 11, 8, 13, 00), 30, 2);
        String value = gson.toJson(testSubtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(value)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/subtask/?id=6");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "неправильный код ответа");

        Subtask subtask = gson.fromJson(response.body(), Subtask.class);

        assertNotNull(subtask, "список задач пуст");
        assertEquals(subtask, testSubtask);
    }

    @Test
    void deleteSubtask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode(), "неправильный код ответа");
    }

    @Test
    void deleteAllSubtask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/subtask/");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Type subtask = new TypeToken<ArrayList<Subtask>>() {
        }.getType();
        List<Subtask> subtasks = gson.fromJson(response.body(), subtask);
        assertEquals(0, subtasks.size(), "размер списка не совпадает");
    }

    @Test
    void getEpicSubtasksId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "неправильный код ответа");
        Type subtask = new TypeToken<ArrayList<Subtask>>() {
        }.getType();
        ArrayList<Subtask> epicSubtasks = gson.fromJson(response.body(), subtask);

        assertNotNull(epicSubtasks, "сабтаск пуст");
    }

    @Test
    void getHistory() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/history/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "неправильный код ответа");
        Type task = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> history = gson.fromJson(response.body(), task);

        assertNotNull(history, "история пуст");
    }

    @Test
    void getPrioritizedTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "неправильный код ответа");
        Type task = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> history = gson.fromJson(response.body(), task);

        assertNotNull(history, "список пуст");
    }
}