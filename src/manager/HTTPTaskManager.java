package manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import server.KVServer;
import server.KVTaskClient;
import tasks.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HTTPTaskManager extends FileBackedTasksManager {
    private String url;
    private Gson gson;
    private KVTaskClient kvTaskClient;


    public HTTPTaskManager(String url) throws IOException {
        this.url = url;
        gson = new Gson();
        //new KVServer().start();
    }

    public void save() {
        kvTaskClient = new KVTaskClient(url);
        String jsonTasks = gson.toJson(getTaskList());
        kvTaskClient.put("tasks", jsonTasks);

        String jsonEpics = gson.toJson(getEpicList());
        kvTaskClient.put("epics", jsonEpics);

        String jsonSubtasks = gson.toJson(getSubtaskList());
        kvTaskClient.put("subtasks", jsonSubtasks);

        String jsonHistory = gson.toJson(getHistory(), new TypeToken<ArrayList<Task>>() {
        }.getType());
        kvTaskClient.put("history", jsonHistory);
    }

    public void load() throws CrossingTaskException {
        kvTaskClient = new KVTaskClient(url);
        String jsonTasks = kvTaskClient.load("tasks");
        String jsonSubtasks = kvTaskClient.load("subtasks");
        String jsonEpics = kvTaskClient.load("epics");
        String jsonHistory = kvTaskClient.load("history");

        Type type = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> tasks = gson.fromJson(jsonTasks, type);
        List<Task> history = gson.fromJson(jsonHistory, type);
        type = new TypeToken<ArrayList<Subtask>>() {
        }.getType();
        List<Subtask> subtasks = gson.fromJson(jsonSubtasks, type);
        type = new TypeToken<ArrayList<Epic>>() {
        }.getType();
        List<Epic> epics = gson.fromJson(jsonEpics, type);

        if (tasks != null) {
            for (Task task : tasks) {
                createTask(task);
            }
        }
        if (epics != null) {
            for (Epic epic : epics) {
                createEpic(epic);
            }
        }
        if (subtasks != null) {
            for (Epic epic : epics) {
                createEpic(epic);
            }
        }
        if (history != null) {
            for (Epic epic : epics) {
                createEpic(epic);
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }


    // Для тасков
    @Override
    public int createTask(Task task) throws CrossingTaskException {  //создание Таска
        final int id = super.createTask(task);
        save();
        return id;
    }

    @Override
    public ArrayList<Task> getTaskList() { //список всех задач
        ArrayList<Task> taskList = super.getTaskList();
        return taskList;
    }

    @Override
    public void removeAllTasks() { //удаление всех задач
        super.removeAllTasks();
        save();
    }


    @Override
    public Task getTask(int id) { //получение по идентификатору
        Task task = super.getTask(id);
        save();
        return task;
    }


    @Override
    public void updateTask(Task task) throws CrossingTaskException { //обновление задачи
        super.updateTask(task);
        save();
    }

    @Override
    public void removeTask(int id) { //удаление по идентификатору
        super.removeTask(id);
        save();
    }

    // Для Эпиков
    @Override
    public int createEpic(Epic epic) { //создание Эпика
        int id = super.createEpic(epic);
        save();
        return id;
    }

    @Override
    public ArrayList<Epic> getEpicList() { //список всех эпиков
        ArrayList<Epic> epicList = super.getEpicList();
        return epicList;
    }

    @Override
    public void removeAllEpics() { //удаление всех эпиков
        super.removeAllEpics();
        save();
    }

    @Override
    public Epic getEpic(int id) { //получение по идентификатору
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public void updateEpic(Epic epic) { //обновление эпика
        super.updateEpic(epic);
        save();
    }

    @Override
    public void removeEpic(int id) { //удаление по идентификатору
        super.removeEpic(id);
        save();
    }

    @Override
    public ArrayList<Subtask> getEpicSubtaskList(int id) {
        ArrayList<Subtask> epicSubtaskList = super.getEpicSubtaskList(id);
        return epicSubtaskList;
    }

    // Для Сабтасков
    @Override
    public int createSubtask(Subtask subtask) throws CrossingTaskException { //создание Сабтаска
        int id = super.createSubtask(subtask);
        save();
        return id;
    }

    @Override
    public ArrayList<Subtask> getSubtaskList() { //список всех задач
        ArrayList<Subtask> subtaskList = super.getSubtaskList();
        return subtaskList;
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public void updateSubtask(Subtask subtask) throws CrossingTaskException {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeSubtask(int id) {
        super.removeSubtask(id);
        save();
    }
}