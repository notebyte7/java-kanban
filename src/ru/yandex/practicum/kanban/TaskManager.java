package ru.yandex.practicum.kanban;

import java.util.ArrayList;
import java.util.HashMap;

class TaskManager {
    protected static int uid;
    HashMap<Integer, Task> tasksHashMap = new HashMap<>();
    HashMap<Integer, Epic> epicsHashMap = new HashMap<>();
    HashMap<Integer, Subtask> subtasksHashMap = new HashMap<>();

    // Для тасков
    void createTask(Task task) {  //создание Таска
        final int id = uid++;
        tasksHashMap.put(id, task);
    }

    HashMap<Integer, Task> getTasksList() { //список всех задач
        return new HashMap<>(tasksHashMap);
    }

    void removeAllTasks() { //удаление всех задач
        tasksHashMap.clear();
    }

    Task getTask(int id) { //получение по идентификатору
        return tasksHashMap.get(id);
    }

    void updateTask(int id, Task task) { //обновление задачи
        tasksHashMap.put(id, task);
    }

    void removeTask(int id) { //удаление по идентификатору
        tasksHashMap.remove(id);
    }

    // Для Эпиков
    void createEpic(Epic epic) { //создание Эпика
        final int id = uid++;
        epic.status = getEpicStatus(epic); //проверка и обновление статуса эпика при его создании
        epicsHashMap.put(id, epic);
    }

    HashMap<Integer, Epic> getEpicsList() { //список всех эпиков
        return new HashMap<>(epicsHashMap);
    }

    void removeAllEpics() { //удаление всех эпиков
        epicsHashMap.clear();
        subtasksHashMap.clear(); //удаление всех эпиков ведет к удалению всех сабтасков
    }

    Epic getEpic(int id) { //получение по идентификатору
        return epicsHashMap.get(id);
    }

    void updateEpic(int id, Epic epic) { //обновление эпика
        epic.status = getEpicStatus(epic); //проверка и обновление статуса эпика при его обновлении
        epicsHashMap.put(id, epic);
    }

    void removeEpic(int id) { //удаление по идентификатору
        ArrayList<Integer> subtasksId = new ArrayList<>(epicsHashMap.get(id).subtaskIds);
        epicsHashMap.remove(id);
        for (Integer subtaskId : subtasksId) {
            subtasksHashMap.remove(Integer.valueOf(subtaskId)); //удаление всех сабтасков удаленного эпика
        }
    }

    HashMap<Integer, Subtask> getEpicSubtask(int id) {
        //получение списка всех подзадач определенного эпика
        HashMap<Integer, Subtask> thisEpicSubtaskHashMap = new HashMap<>();
        for (Integer subtaskId : epicsHashMap.get(id).subtaskIds) {
            thisEpicSubtaskHashMap.put(subtaskId, subtasksHashMap.get(subtaskId));
        }
        return thisEpicSubtaskHashMap;
    }

    private String getEpicStatus(Epic epic) { //проверка и возврат статуса Эпика
        int n = 1; //проверка на условие, аналог опреатора И
        int m = 1;
        String status;
        if (epic.subtaskIds == null) {
            status = "NEW";
        } else {
            for (Integer subtaskId : epic.subtaskIds) {
                if (subtasksHashMap.get(subtaskId).status.equals("NEW")) n *= 1;
                else n *= 0;
                if (subtasksHashMap.get(subtaskId).status.equals("DONE")) m *= 1;
                else m *= 0;
            }
            if (n == 1) status = "NEW";
            else if (m == 1) status = "DONE";
            else status = "IN_PROGRESS";
        }
        return status;
    }

    // Для Сабтасков
    void createSubtask(Subtask subtask) { //создание Сабтаска
        final int id = uid++;
        subtasksHashMap.put(id, subtask);
        epicsHashMap.get(subtask.epicId).subtaskIds.add(id);
        epicsHashMap.get(subtask.epicId).status = getEpicStatus(epicsHashMap.get(subtask.epicId));
    }

    HashMap<Integer, Subtask> getSubtasksList() { //список всех задач
        return new HashMap<>(subtasksHashMap);
    }

    void removeAllSubtasks() {
        subtasksHashMap.clear();
        for (Epic value : epicsHashMap.values()) {
            value.status = "NEW"; //при удалении всех подзадач, у всех эпиков становится статус NEW
        }
    }

    Task getSubtask(int id) {
        return subtasksHashMap.get(id);
    }

    void updateSubtask(int id, Subtask subtask) {
        subtasksHashMap.put(id, subtask);
        epicsHashMap.get(subtask.epicId).status = getEpicStatus(epicsHashMap.get(subtask.epicId));
        //проверка и изменение статуса при обновлении сабтаска
    }

    void removeSubtask(int id) {
        int epicId = subtasksHashMap.get(id).epicId;
        subtasksHashMap.remove(id);
        epicsHashMap.get(epicId).subtaskIds.remove(Integer.valueOf(id));
        epicsHashMap.get(epicId).status = getEpicStatus(getEpic(epicId));
        //проверка и изменение статуса при удалении сабтаска

    }
}

