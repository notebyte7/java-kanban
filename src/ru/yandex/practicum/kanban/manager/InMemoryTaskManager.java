package ru.yandex.practicum.kanban.manager;

import ru.yandex.practicum.kanban.history.HistoryManager;
import ru.yandex.practicum.kanban.tasks.Task;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import ru.yandex.practicum.kanban.tasks.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private static int uid;
    private final HashMap<Integer, Task> tasksHashMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicsHashMap = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasksHashMap = new HashMap<>();

    private HistoryManager historyManager = Managers.getDefaultHistory();

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    // Для тасков
    @Override
    public void createTask(Task task) {  //создание Таска
        final int id;
        if (task.getId() != 0) {
            id = task.getId();
            uid = id;
        } else {
            id = ++uid;
            task.setId(id);
        }
        tasksHashMap.put(id, task);
    }

    @Override
    public ArrayList<Task> getTaskList() { //список всех задач
        return new ArrayList<>(tasksHashMap.values());
    }

    @Override
    public void removeAllTasks() { //удаление всех задач
        tasksHashMap.clear();
    }

    @Override
    public Task getTask(int id) { //получение по идентификатору
        Task task = tasksHashMap.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public void updateTask(Task task) { //обновление задачи
        tasksHashMap.put(task.getId(), task);
    }

    @Override
    public void removeTask(int id) { //удаление по идентификатору
        tasksHashMap.remove(id);
        historyManager.remove(id);
    }

    // Для Эпиков
    @Override
    public void createEpic(Epic epic) { //создание Эпика
        final int id;
        if (epic.getId() != 0) {
            id = epic.getId();
            uid = id;
        } else {
            id = ++uid;
            epic.setId(id);
        }
        epic.setStatus(getEpicStatus(epic)); //проверка и обновление статуса эпика при его создании
        epicsHashMap.put(id, epic);
    }

    @Override
    public ArrayList<Epic> getEpicList() { //список всех эпиков
        return new ArrayList<>(epicsHashMap.values());
    }

    @Override
    public void removeAllEpics() { //удаление всех эпиков
        epicsHashMap.clear();
        subtasksHashMap.clear(); //удаление всех эпиков ведет к удалению всех сабтасков
    }

    @Override
    public Epic getEpic(int id) { //получение по идентификатору
        Epic epic = epicsHashMap.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public void updateEpic(Epic epic) { //обновление эпика
        epic.setStatus(getEpicStatus(epic)); //проверка и обновление статуса эпика при его обновлении
        epicsHashMap.put(epic.getId(), epic);
    }

    @Override
    public void removeEpic(int id) { //удаление по идентификатору
        ArrayList<Integer> subtasksId = new ArrayList<>(epicsHashMap.get(id).getSubtaskIds());
        epicsHashMap.remove(id);
        for (Integer subtaskId : subtasksId) {
            subtasksHashMap.remove(subtaskId);
            historyManager.remove(subtaskId);
            //удаление всех сабтасков удаленного эпика
        }
        historyManager.remove(id);
    }

    @Override
    public ArrayList<Subtask> getEpicSubtaskList(int id) {
        //получение списка всех подзадач определенного эпика
        ArrayList<Subtask> thisEpicSubtaskList = new ArrayList<>();
        for (Integer subtaskId : epicsHashMap.get(id).getSubtaskIds()) {
            thisEpicSubtaskList.add(subtasksHashMap.get(subtaskId));
        }
        return thisEpicSubtaskList;
    }

    private Status getEpicStatus(Epic epic) { //проверка и возврат статуса Эпика
        int n = 1; //проверка на условие, аналог опреатора И
        int m = 1;
        Status status;
        if (epic.getSubtaskIds() == null) {
            status = Status.NEW;
        } else {
            for (Integer subtaskId : epic.getSubtaskIds()) {
                if (subtasksHashMap.get(subtaskId).getStatus().equals(Status.NEW)) {
                    n *= 1;
                } else {
                    n *= 0;
                }
                if (subtasksHashMap.get(subtaskId).getStatus().equals(Status.DONE)) {
                    m *= 1;
                } else {
                    m *= 0;
                }
            }
            if (n == 1) {
                status = Status.NEW;
            } else if (m == 1) {
                status = Status.DONE;
            } else {
                status = Status.IN_PROGRESS;
            }
        }
        return status;
    }

    // Для Сабтасков
    @Override
    public void createSubtask(Subtask subtask) { //создание Сабтаска
        final int id;
        if (subtask.getId() != 0) {
            id = subtask.getId();
            uid = id;
        } else {
            id = ++uid;
            subtask.setId(id);
        }
        subtasksHashMap.put(id, subtask);
        Epic epic = epicsHashMap.get(subtask.getEpicId());
        try {
            epic.getSubtaskIds().add(id);
            epic.setStatus(getEpicStatus(epic));
        } catch (NullPointerException e) {
            System.out.println("Внимание: epicID у сабтаска(id = " + id + ") не соответствует ID эпика");
        }

    }

    @Override
    public ArrayList<Subtask> getSubtaskList() { //список всех задач
        return new ArrayList<>(subtasksHashMap.values());
    }

    @Override
    public void removeAllSubtasks() {
        subtasksHashMap.clear();
        for (Epic value : epicsHashMap.values()) {
            value.getSubtaskIds().clear(); //удаление subtaskIds у каждого эпика
            value.setStatus(getEpicStatus(value)); //при удалении всех подзадач, у всех эпиков становится статус NEW
        }
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasksHashMap.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasksHashMap.put(subtask.getId(), subtask);
        Epic epic = epicsHashMap.get(subtask.getEpicId());
        epic.setStatus(getEpicStatus(epic));
        //проверка и изменение статуса при обновлении сабтаска
    }

    @Override
    public void removeSubtask(int id) {
        int epicId = subtasksHashMap.get(id).getEpicId();
        subtasksHashMap.remove(id);
        Epic epic = epicsHashMap.get(epicId);
        epic.getSubtaskIds().remove(Integer.valueOf(id));
        epic.setStatus(getEpicStatus(epic));
        //проверка и изменение статуса при удалении сабтаска
        historyManager.remove(id);
    }
}

