package manager;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.List;
import java.util.Set;

public interface TaskManager {

    // Для тасков
    int createTask(Task task);

    List<Task> getTaskList();

    void removeAllTasks();

    Task getTask(int id);

    void updateTask(Task task);

    void removeTask(int id);

    // Для Эпиков
    int createEpic(Epic epic);

    List<Epic> getEpicList();

    void removeAllEpics();

    Epic getEpic(int id);

    void updateEpic(Epic epic);

    void removeEpic(int id);

    List<Subtask> getEpicSubtaskList(int id);

    Status getEpicStatus(int id);


    // Для Сабтасков
    int createSubtask(Subtask subtask);

    List<Subtask> getSubtaskList();

    void removeAllSubtasks();

    Subtask getSubtask(int id);

    void updateSubtask(Subtask subtask);

    void removeSubtask(int id);

    Set<Task> getPrioritizedTasks();

    void save(String path);
}