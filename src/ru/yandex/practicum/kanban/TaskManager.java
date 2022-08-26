package ru.yandex.practicum.kanban;
import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    static int uid;
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

    void removeAllTasks(){ //удаление всех задач
        tasksHashMap.clear();
    }

    Task getTask(int id){ //получение по идентификатору
        return tasksHashMap.get(id);
    }

    void updateTask(int id, Task task){ //обновление задачи
        tasksHashMap.put(id, task);
    }

    void removeTask(int id) { //удаление по идентификатору
        tasksHashMap.remove(id);
    }

    // Для Эпиков
    void createEpic(Epic epic) { //создание Эпика
        final int id = uid++;
        int n = 1; //проверка на условие, аналог опреатора И
        int m = -1;
        for (Integer subtaskId : epic.subtaskIds) {
            if (subtasksHashMap.get(subtaskId).status == "NEW") n *= 1;
            else n *= 0;
            if (subtasksHashMap.get(subtaskId).status == "DONE") m *= 1;
            else m *= 0;
        }
        if ((epic.subtaskIds == null) || (n == 1)) epic.status = "NEW";
        else if (m == -1) epic.status = "DONE";
        else epic.status = "IN_PROGRESS";
        epicsHashMap.put(id, epic);

    }
    HashMap<Integer, Epic> getEpicsList() { //список всех эпиков
        return new HashMap<>(epicsHashMap);
    }

    void removeAllEpics(){ //удаление всех эпиков
        epicsHashMap.clear();
    }

    Epic getEpic(int id){ //получение по идентификатору
        return epicsHashMap.get(id);
    }

    void updateEpic(int id, Epic epic){ //обновление эпика
        int n = 1; //проверка на условие, аналог опреатора И
        int m = -1;
        for (Integer subtaskId : epic.subtaskIds) {
            if (subtasksHashMap.get(subtaskId).status == "NEW") n *= 1;
            else n *= 0;
            if (subtasksHashMap.get(subtaskId).status == "DONE") m *= 1;
            else m *= 0;
        }
        if ((epic.subtaskIds == null) || (n == 1)) epic.status = "NEW";
        else if (m == -1) epic.status = "DONE";
        else epic.status = "IN_PROGRESS";
        epicsHashMap.put(id, epic);
    }

    void removeEpic(int id) { //удаление по идентификатору
        epicsHashMap.remove(id);
    }

    HashMap<Integer, Subtask> getEpicSubtask(int id) { //получение списка всех подзадач определенного эпика
        HashMap<Integer, Subtask> thisEpicSubtaskHashMap = new HashMap<>();
        for (Integer subtaskId : epicsHashMap.get(id).subtaskIds) {
            thisEpicSubtaskHashMap.put(subtaskId, subtasksHashMap.get(subtaskId));
        }
        return thisEpicSubtaskHashMap;
    }

    // Для Сабтасков
    void createSubtask(Subtask subtask) { //создание Сабтаска
        final int id = uid++;
        subtasksHashMap.put(id, subtask);
        epicsHashMap.get(subtask.epicId).subtaskIds.add(id);
    }
    HashMap<Integer, Subtask> getSubtasksList() { //список всех задач
        return new HashMap<>(subtasksHashMap);
    }

    void removeAllSubtasks(){
        subtasksHashMap.clear();
    }

    Task getSubtask(int id){
        return subtasksHashMap.get(id);
    }

    void updateSubtask(int id, Subtask subtask){
        subtasksHashMap.put(id, subtask);
    }

    void removeSubtask(int id) {
        subtasksHashMap.remove(id);
    }

}

