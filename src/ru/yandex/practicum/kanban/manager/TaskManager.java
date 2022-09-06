package ru.yandex.practicum.kanban.manager;

import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import ru.yandex.practicum.kanban.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    // Для тасков
    void createTask(Task task);

    ArrayList<Task> getTaskList();

    void removeAllTasks();

    Task getTask(int id);

    void updateTask(Task task);

    void removeTask(int id);

    // Для Эпиков
    void createEpic(Epic epic);

    ArrayList<Epic> getEpicList();

    void removeAllEpics();

    Epic getEpic(int id);

    void updateEpic(Epic epic);

    void removeEpic(int id);

    ArrayList<Subtask> getEpicSubtaskList(int id);

    // Для Сабтасков
    void createSubtask(Subtask subtask);

    ArrayList<Subtask> getSubtaskList();

    void removeAllSubtasks();

    Subtask getSubtask(int id);

    void updateSubtask(Subtask subtask);

    void removeSubtask(int id);

    public List<Task> getHistory();
}