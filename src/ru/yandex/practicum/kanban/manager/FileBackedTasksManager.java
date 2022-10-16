package ru.yandex.practicum.kanban.manager;

import ru.yandex.practicum.kanban.history.HistoryManager;
import ru.yandex.practicum.kanban.tasks.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.io.BufferedWriter;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final Map<Integer, Task> tempTasksMap = new HashMap<>();

    public static void main(String[] args) throws ManagerSaveException {
        //Менеджер 1
        FileBackedTasksManager manager1 = new FileBackedTasksManager();
        manager1.fromString("1,TASK,Task1,NEW,Description task1,");
        manager1.fromString("2,EPIC,Epic2,DONE,Description epic2,");
        manager1.fromString("3,SUBTASK,Sub Task3,DONE,Description sub task3,2");
        manager1.fromString("4,TASK,Task4,NEW,Description task4,");
        manager1.fromString("5,EPIC,Epic5,DONE,Description epic5,");
        manager1.fromString("6,SUBTASK,Sub Task6,DONE,Description sub task6,5");
        manager1.fromString("7,EPIC,Epic5,NEW,Description epic7,");
        manager1.fromString("8,SUBTASK,Sub Task6,NEW,Description sub task8,7");
        System.out.println("Менеджер 1");
        System.out.println(manager1.getTaskList());
        System.out.println(manager1.getEpicList());
        System.out.println(manager1.getSubtaskList());
        manager1.getTask(1);
        manager1.getEpic(2);
        manager1.getSubtask(3);
        System.out.println("Менеджер 1 - история");
        System.out.println(manager1.getHistory());

        //Менеджер 2
        FileBackedTasksManager manager = loadFromFile(new File("manager.csv"));
        System.out.println("\nМенеджер 2");
        System.out.println(manager.getTaskList());
        System.out.println(manager.getEpicList());
        System.out.println(manager.getSubtaskList());
        System.out.println("Менеджер 2 - история");
        System.out.println(manager.getHistory());
        manager.getTask(4);
        manager.getEpic(5);
        System.out.println("Менеджер 2 - история");
        System.out.println(manager.getHistory());


    }

    private void save(String path) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write("id,type,name,status,description,epic");
            for (Task task : getTaskList()) {
                writer.write("\n" + task.toString());
            }
            for (Epic epic : getEpicList()) {
                writer.write("\n" + epic.toString());
            }
            for (Subtask subtask : getSubtaskList()) {
                writer.write("\n" + subtask.toString());
            }
            writer.write("\n\n" + historyToString(getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) throws ManagerSaveException {
        FileBackedTasksManager manager = new FileBackedTasksManager();
        List<String> lines;
        try {
            String content = Files.readString(Path.of(file.toURI()));
            lines = Arrays.asList(content.split("\n"));
            int i = 1;
            while (!lines.get(i).equals("")) {
                manager.fromString(lines.get(i));
                i++;
            }
            List<Integer> history = manager.historyFromString(lines.get(i + 1));
            for (Integer id : history) {
                manager.getHistoryManager().add(manager.tempTasksMap.get(id));
            }

        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
        return manager;
    }

    private Task fromString(String line) {
        String[] value = line.split(",");
        Task task = null;

        switch (TaskType.valueOf(value[1])) {
            case TASK:
                task = new Task(value[2], value[4], Integer.parseInt(value[0]), Status.valueOf(value[3]));
                super.createTask(task);
                tempTasksMap.put(Integer.parseInt(value[0]), task);
                break;
            case EPIC:
                task = new Epic(value[2], value[4], Integer.parseInt(value[0]), Status.valueOf(value[3]));
                super.createEpic((Epic) task);
                tempTasksMap.put(Integer.parseInt(value[0]), task);
                break;
            case SUBTASK:
                task = new Subtask(value[2], value[4], Integer.parseInt(value[0]),
                        Status.valueOf(value[3]), Integer.parseInt(value[5]));
                super.createSubtask((Subtask) task);
                tempTasksMap.put(Integer.parseInt(value[0]), task);
                break;
        }

        return task;
    }

    private static String historyToString(HistoryManager manager) {
        List<Task> historyList = manager.getHistory();
        String history = "";
        for (Task task : historyList) {
            history += String.valueOf(task.getId()) + ",";
        }
        return history;
    }

    private static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        String[] historyString = value.split(",");
        for (int i = 0; i < historyString.length; i++) {
            int id = Integer.parseInt(historyString[i]);
            history.add(i, id);
        }
        return history;
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }


    // Для тасков
    @Override
    public void createTask(Task task) {  //создание Таска
        super.createTask(task);
        save("manager.csv");
    }

    @Override
    public ArrayList<Task> getTaskList() { //список всех задач
        ArrayList<Task> taskList = super.getTaskList();
        return taskList;
    }

    @Override
    public void removeAllTasks() { //удаление всех задач
        super.removeAllTasks();
        save("manager.csv");
    }


    @Override
    public Task getTask(int id) { //получение по идентификатору
        Task task = super.getTask(id);
        save("manager.csv");
        return task;
    }


    @Override
    public void updateTask(Task task) { //обновление задачи
        super.updateTask(task);
        save("manager.csv");
    }

    @Override
    public void removeTask(int id) { //удаление по идентификатору
        super.removeTask(id);
        save("manager.csv");
    }

    // Для Эпиков
    @Override
    public void createEpic(Epic epic) { //создание Эпика
        super.createEpic(epic);
        save("manager.csv");
    }

    @Override
    public ArrayList<Epic> getEpicList() { //список всех эпиков
        ArrayList<Epic> epicList = super.getEpicList();
        return epicList;
    }

    @Override
    public void removeAllEpics() { //удаление всех эпиков
        super.removeAllEpics();
        save("manager.csv");
    }

    @Override
    public Epic getEpic(int id) { //получение по идентификатору
        Epic epic = super.getEpic(id);
        save("manager.csv");
        return epic;
    }

    @Override
    public void updateEpic(Epic epic) { //обновление эпика
        super.updateEpic(epic);
        save("manager.csv");
    }

    @Override
    public void removeEpic(int id) { //удаление по идентификатору
        super.removeEpic(id);
        save("manager.csv");
    }

    @Override
    public ArrayList<Subtask> getEpicSubtaskList(int id) {
        ArrayList<Subtask> epicSubtaskList = super.getEpicSubtaskList(id);
        return epicSubtaskList;
    }

    // Для Сабтасков
    @Override
    public void createSubtask(Subtask subtask) { //создание Сабтаска
        super.createSubtask(subtask);
        save("manager.csv");
    }

    @Override
    public ArrayList<Subtask> getSubtaskList() { //список всех задач
        ArrayList<Subtask> subtaskList = super.getSubtaskList();
        return subtaskList;
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save("manager.csv");
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        save("manager.csv");
        return subtask;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save("manager.csv");
    }

    @Override
    public void removeSubtask(int id) {
        super.removeSubtask(id);
        save("manager.csv");
    }
}
