package manager;

import tasks.*;
import history.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.io.BufferedWriter;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final String path;
    private final File file;

    public FileBackedTasksManager() {
        this.path = "manager.csv";
        this.file = new File("manager.csv");
    }

    private final Map<Integer, Task> tempTasksMap = new HashMap<>();

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write("id,type,name,status,description,startTime,duration,epic");
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

    public FileBackedTasksManager load() throws ManagerSaveException, CrossingTaskException {
        FileBackedTasksManager manager = new FileBackedTasksManager();
        List<String> lines;
        try {
            String content = Files.readString(Path.of(file.toURI()));
            lines = Arrays.asList(content.split("\n"));
            if (lines.size() > 1) {
                for (int i = 1; i < lines.size(); i++) {
                    if (!lines.get(i).equals("")) {
                        manager.fromString(lines.get(i));
                    } else {
                        if (i + 1 < lines.size()) {
                            List<Integer> history = manager.historyFromString(lines.get(i + 1));
                            for (Integer id : history) {
                                manager.getHistoryManager().add(manager.tempTasksMap.get(id));
                            }
                            break;
                        }
                    }
                }
            }
        } catch (IOException | CrossingTaskException e) {
            throw new ManagerSaveException();
        }
        return manager;
    }

    private Task fromString(String line) throws CrossingTaskException {
        String[] value = line.split(",");
        Task task = null;
        switch (TaskType.valueOf(value[1])) {
            case TASK:
                try {
                    task = new Task(value[2], value[4], Integer.parseInt(value[0]), Status.valueOf(value[3]),
                            LocalDateTime.parse(value[5]), Integer.parseInt(value[6]));
                } catch (IndexOutOfBoundsException e) {
                    task = new Task(value[2], value[4], Integer.parseInt(value[0]), Status.valueOf(value[3]));
                }
                super.createTask(task);
                tempTasksMap.put(Integer.parseInt(value[0]), task);
                return task;
            case EPIC:
                try {
                    task = new Epic(value[2], value[4], Integer.parseInt(value[0]), Status.valueOf(value[3]),
                            LocalDateTime.parse(value[5]), Integer.parseInt(value[6]));
                } catch (IndexOutOfBoundsException e) {
                    task = new Epic(value[2], value[4], Integer.parseInt(value[0]), Status.valueOf(value[3]));
                }
                super.createEpic((Epic) task);
                tempTasksMap.put(Integer.parseInt(value[0]), task);
                break;
            case SUBTASK:
                try {
                    task = new Subtask(value[2], value[4], Integer.parseInt(value[0]),
                            Status.valueOf(value[3]), LocalDateTime.parse(value[5]), Integer.parseInt(value[6]),
                            Integer.parseInt(value[7]));
                } catch (IndexOutOfBoundsException | DateTimeParseException e) {
                    task = new Subtask(value[2], value[4], Integer.parseInt(value[0]),
                            Status.valueOf(value[3]), Integer.parseInt(value[7]));
                }
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
