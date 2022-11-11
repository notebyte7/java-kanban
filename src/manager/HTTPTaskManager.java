package manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import server.KVTaskClient;
import tasks.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HTTPTaskManager extends FileBackedTasksManager {

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private ArrayList<Task> history = new ArrayList<>();
    private String url;
    private Gson gson;
    private KVTaskClient kvTaskClient;


    public HTTPTaskManager(String url) {
        this.url = url;
        gson = new Gson();
        kvTaskClient = new KVTaskClient(url);
    }


    public void save() {
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

    public void load() {
        String jsonTasks = kvTaskClient.load("tasks");
        String jsonSubtasks = kvTaskClient.load("subtasks");
        String jsonEpics = kvTaskClient.load("epics");
        String jsonHistory = kvTaskClient.load("history");

        tasks = gson.fromJson(jsonTasks, new TypeToken<HashMap<Integer, Task>>() {
        }.getType());
        subtasks = gson.fromJson(jsonSubtasks, new TypeToken<HashMap<Integer, Subtask>>() {
        }.getType());
        epics = gson.fromJson(jsonEpics, new TypeToken<HashMap<Integer, Epic>>() {
        }.getType());
        history = gson.fromJson(jsonHistory, new TypeToken<ArrayList<Task>>() {
        }.getType());
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