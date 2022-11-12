package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import manager.CrossingTaskException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import manager.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.UTF_8;


public class HttpTaskServer {
    private final TaskManager taskManager;
    private final HttpServer httpServer;
    public static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static Gson gson;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        gson = new Gson();
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/tasks", this::handleTasks);
    }

    public void start() {
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        System.out.println("http://localhost:" + PORT + "/tasks/");
    }

    public void stop() throws IOException {
        httpServer.stop(0);
        System.out.println("HTTP-сервер остановлен на " + PORT + " порту!");
    }

    private void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    private void handleTasks(HttpExchange httpExchange) throws IOException {
        try {
            int id = 0;
            String method = httpExchange.getRequestMethod();
            String path = httpExchange.getRequestURI().getPath();
            Headers headers = httpExchange.getResponseHeaders();
            if (httpExchange.getRequestURI().getQuery() != null) {
                String[] str = httpExchange.getRequestURI().getQuery().split("=");
                if (str.length == 2) {
                    id = Integer.parseInt(str[1]);
                }
                switch (method) {
                    case "GET":
                        headers.set("Content-Type", "application/json");
                        if (path.equals("/tasks/subtask/epic/")) {
                            if (id != 0 && taskManager.getEpicSubtaskList(id) != null) {
                                String response = gson.toJson(taskManager.getEpicSubtaskList(id));
                                sendText(httpExchange, response);
                            } else {
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        } else if (path.equals("/tasks/task/")) {
                            if ((id != 0) && taskManager.getTask(id) != null) {
                                String response = gson.toJson(taskManager.getTask(id));
                                sendText(httpExchange, response);
                            } else {
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        } else if (path.equals("/tasks/epic/")) {
                            if ((id != 0) && taskManager.getEpic(id) != null) {
                                String response = gson.toJson(taskManager.getEpic(id));
                                sendText(httpExchange, response);
                            } else {
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        } else if (path.equals("/tasks/subtask/")) {
                            if ((id != 0) && taskManager.getSubtask(id) != null) {
                                String response = gson.toJson(taskManager.getSubtask(id));
                                sendText(httpExchange, response);
                            } else {
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                        break;
                    case "DELETE":
                        if (path.equals("/tasks/task/")) {
                            if ((id != 0) && taskManager.getTask(id) != null) {
                                taskManager.removeTask(id);
                                httpExchange.sendResponseHeaders(204, 0);
                            } else {
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        } else if (path.equals("/tasks/epic/")) {
                            if ((id != 0) && taskManager.getEpic(id) != null) {
                                taskManager.removeEpic(id);
                                httpExchange.sendResponseHeaders(204, 0);
                            } else {
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        } else if (path.equals("/tasks/subtask/")) {
                            if ((id != 0) && taskManager.getSubtask(id) != null) {
                                taskManager.removeSubtask(id);
                                httpExchange.sendResponseHeaders(204, 0);
                            } else {
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                        break;
                    default:
                        httpExchange.sendResponseHeaders(501, 0);
                }
            } else {
                switch (method) {
                    case "GET":
                        headers.set("Content-Type", "application/json");
                        if (path.equals("/tasks/")) {
                            String response = gson.toJson(taskManager.getPrioritizedTasks());
                            sendText(httpExchange, response);
                        } else if (path.equals("/tasks/history/")) {
                            String response = gson.toJson(taskManager.getHistory());
                            sendText(httpExchange, response);
                        } else if (path.equals("/tasks/task/")) {
                            String response = gson.toJson(taskManager.getTaskList());
                            sendText(httpExchange, response);
                        } else if (path.equals("/tasks/epic/")) {
                            String response = gson.toJson(taskManager.getEpicList());
                            sendText(httpExchange, response);
                        } else if (path.equals("/tasks/subtask/")) {
                            String response = gson.toJson(taskManager.getSubtaskList());
                            sendText(httpExchange, response);
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                        break;
                    case "POST":
                        InputStream is = httpExchange.getRequestBody();
                        String body = new String(is.readAllBytes(), DEFAULT_CHARSET);
                        if (path.equals("/tasks/task/")) {
                            Task newTask = gson.fromJson(body, Task.class);
                            boolean isExist = taskManager.getTaskList().stream()
                                    .anyMatch(task -> task.getId() == newTask.getId());
                            if (isExist) {
                                try {
                                    taskManager.updateTask(newTask);
                                    httpExchange.sendResponseHeaders(204, 0);
                                } catch (CrossingTaskException e) {
                                    httpExchange.sendResponseHeaders(422, 0);
                                }
                            } else {
                                try {
                                    taskManager.createTask(newTask);
                                    httpExchange.sendResponseHeaders(201, 0);
                                } catch (CrossingTaskException e) {
                                    httpExchange.sendResponseHeaders(422, 0);
                                }
                            }
                        } else if (path.equals("/tasks/epic/")) {
                            Epic newEpic = gson.fromJson(body, Epic.class);
                            boolean isExist = taskManager.getEpicList().stream()
                                    .anyMatch(epic -> epic.getId() == newEpic.getId());
                            if (isExist) {
                                taskManager.updateEpic(newEpic);
                                httpExchange.sendResponseHeaders(204, 0);
                            } else {
                                taskManager.createEpic(newEpic);
                                httpExchange.sendResponseHeaders(201, 0);
                            }
                        } else if (path.equals("/tasks/subtask/")) {
                            Subtask newSubtask = gson.fromJson(body, Subtask.class);
                            boolean isExist = taskManager.getSubtaskList().stream()
                                    .anyMatch(subtask -> subtask.getId() == newSubtask.getId());
                            if (isExist) {
                                try {
                                    taskManager.updateSubtask(newSubtask);
                                    httpExchange.sendResponseHeaders(204, 0);
                                } catch (CrossingTaskException e) {
                                    httpExchange.sendResponseHeaders(422, 0);
                                }
                            } else {
                                try {
                                    taskManager.createSubtask(newSubtask);
                                    httpExchange.sendResponseHeaders(201, 0);
                                } catch (CrossingTaskException e) {
                                    httpExchange.sendResponseHeaders(422, 0);
                                }
                            }
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                        break;
                    case "DELETE":
                        if (path.equals("/tasks/task/")) {
                            taskManager.removeAllTasks();
                            httpExchange.sendResponseHeaders(204, 0);
                        } else if (path.equals("/tasks/epic/")) {
                            taskManager.removeAllEpics();
                            httpExchange.sendResponseHeaders(204, 0);
                        } else if (path.equals("/tasks/subtask/")) {
                            taskManager.removeAllSubtasks();
                            httpExchange.sendResponseHeaders(204, 0);
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                        break;
                    default:
                        httpExchange.sendResponseHeaders(501, 0);
                }
            }
        } catch (Exception exception) {
            System.out.println("Сервер упал");
            throw exception;
        } finally {
            httpExchange.close();
        }

    }

}
