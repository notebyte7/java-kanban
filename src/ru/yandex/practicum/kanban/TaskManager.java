package ru.yandex.practicum.kanban;

import ru.yandex.practicum.kanban.tasks.Task;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import ru.yandex.practicum.kanban.tasks.Status;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private static int uid;
    private HashMap<Integer, Task> tasksHashMap = new HashMap<>();
    private HashMap<Integer, Epic> epicsHashMap = new HashMap<>();
    private HashMap<Integer, Subtask> subtasksHashMap = new HashMap<>();

    // Для тасков
    public void createTask(Task task) {  //создание Таска
        final int id = uid++;
        task.setId(id);
        tasksHashMap.put(id, task);
    }

    public ArrayList<Task> getTaskList() { //список всех задач
        return new ArrayList<>(tasksHashMap.values());
    }

    public void removeAllTasks() { //удаление всех задач
        tasksHashMap.clear();
    }

    public Task getTask(int id) { //получение по идентификатору
        return tasksHashMap.get(id);
    }

    public void updateTask(Task task) { //обновление задачи
        tasksHashMap.put(task.getId(), task);
    }

    public void removeTask(int id) { //удаление по идентификатору
        tasksHashMap.remove(id);
    }

    // Для Эпиков
    public void createEpic(Epic epic) { //создание Эпика
        final int id = uid++;
        epic.setId(id);
        epic.setStatus(getEpicStatus(epic)); //проверка и обновление статуса эпика при его создании
        epicsHashMap.put(id, epic);
    }

    public ArrayList<Epic> getEpicList() { //список всех эпиков
        return new ArrayList<>(epicsHashMap.values());
    }

    public void removeAllEpics() { //удаление всех эпиков
        epicsHashMap.clear();
        subtasksHashMap.clear(); //удаление всех эпиков ведет к удалению всех сабтасков
    }

    public Epic getEpic(int id) { //получение по идентификатору
        return epicsHashMap.get(id);
    }

    public void updateEpic(Epic epic) { //обновление эпика
        epic.setStatus(getEpicStatus(epic)); //проверка и обновление статуса эпика при его обновлении
        epicsHashMap.put(epic.getId(), epic);
    }

    public void removeEpic(int id) { //удаление по идентификатору
        ArrayList<Integer> subtasksId = new ArrayList<>(epicsHashMap.get(id).getSubtaskIds());
        epicsHashMap.remove(id);
        for (Integer subtaskId : subtasksId) {
            subtasksHashMap.remove(subtaskId); //удаление всех сабтасков удаленного эпика
        }
    }

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
            } else status = Status.IN_PROGRESS;
        }
        return status;
    }

    // Для Сабтасков
    public void createSubtask(Subtask subtask) { //создание Сабтаска
        final int id = uid++;
        Epic epic;
        subtask.setId(id);
        subtasksHashMap.put(id, subtask);
        epic = epicsHashMap.get(subtask.getEpicId());
        epic.getSubtaskIds().add(id);
        epic.setStatus(getEpicStatus(epic));
    }

    public ArrayList<Subtask> getSubtaskList() { //список всех задач
        return new ArrayList<>(subtasksHashMap.values());
    }

    public void removeAllSubtasks() {
        subtasksHashMap.clear();
        for (Epic value : epicsHashMap.values()) {
            value.getSubtaskIds().clear(); //удаление subtaskIds у каждого эпика
            value.setStatus(getEpicStatus(value)); //при удалении всех подзадач, у всех эпиков становится статус NEW
        }
    }

    public Subtask getSubtask(int id) {
        return subtasksHashMap.get(id);
    }

    public void updateSubtask(Subtask subtask) {
        subtasksHashMap.put(subtask.getId(), subtask);
        Epic epic;
        epic = epicsHashMap.get(subtask.getEpicId());
        epic.setStatus(getEpicStatus(epic));
        //проверка и изменение статуса при обновлении сабтаска
    }

    public void removeSubtask(int id) {
        int epicId = subtasksHashMap.get(id).getEpicId();
        subtasksHashMap.remove(id);
        Epic epic;
        epic = epicsHashMap.get(epicId);
        epic.getSubtaskIds().remove(Integer.valueOf(id));
        epic.setStatus(getEpicStatus(epic));
        //проверка и изменение статуса при удалении сабтаска
    }
}

