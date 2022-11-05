package ru.yandex.practicum.kanban.manager;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.yandex.practicum.kanban.tasks.Status.*;

public class InMemoryTaskManagerTest extends TaskManagerTest<TaskManager> {

    private final static TaskManager taskManager = new InMemoryTaskManager();

    public InMemoryTaskManagerTest() {
        super(taskManager);
    }

    @Test
    void createTask() {
        super.createTask();
    }

    @Test
    void getTaskList() {
        super.getTaskList();
    }

    @Test
    void removeAllTasks() {
        super.removeAllTasks();
    }

    @Test
    void getTask() {
        super.getTask();
    }

    @Test
    void updateTask() {
        super.updateTask();
    }

    @Test
    void removeTask() {
        super.removeTask();
    }

    // Для Эпиков
    @Test
    void createEpic() {
        super.createEpic();
    }

    @Test
    void getEpicList() {
        super.getEpicList();
    }

    @Test
    void removeAllEpics() {
        super.removeAllEpics();
    }

    @Test
    void getEpic() {
        super.getEpic();
    }

    @Test
    void updateEpic() {
        super.updateEpic();
    }

    @Test
    void removeEpic() {
        super.removeEpic();
    }

    @Test
    void getEpicSubtaskList() {
        super.getEpicSubtaskList();
    }

    //Сабтаски
    @Test
    void createSubtask() {
        super.createSubtask();
    }

    @Test
    void getSubtaskList() {
        super.getSubtaskList();
    }

    @Test
    void removeAllSubtasks() {
        super.removeAllSubtasks();
    }

    @Test
    void getSubtask() {
        super.getSubtask();
    }

    @Test
    void updateSubtask() {
        super.updateSubtask();
    }

    @Test
    void removeSubtask() {
        super.removeSubtask();
    }

    @Test
    void getEpicStatus() {
        Epic epic = new Epic("Test Epic", "Test Epic description", NEW);
        final int epicId = taskManager.createEpic(epic);
        assertEquals(taskManager.getEpicStatus(epicId), NEW, "Статусы не совпадают.");

        Subtask subtask1 = new Subtask("Test Subtask", "Test Subtask description", NEW, epicId);
        final int subtaskId1 = taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Test Subtask", "Test Subtask description", NEW, epicId);
        final int subtaskId2 = taskManager.createSubtask(subtask2);
        assertEquals(taskManager.getEpicStatus(epicId), NEW, "Статусы не совпадают.");

        subtask1 = new Subtask("Test Subtask1", "Test Subtask description",
                subtaskId1, DONE, epicId);
        subtask2 = new Subtask("Test Subtask2", "Test Subtask description",
                subtaskId2, DONE, epicId);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        assertEquals(taskManager.getEpicStatus(epicId), DONE, "Статусы не совпадают.");

        subtask2 = new Subtask("Test Subtask2", "Test Subtask description",
                subtaskId2, IN_PROGRESS, epicId);
        taskManager.updateSubtask(subtask2);
        assertEquals(taskManager.getEpicStatus(epicId), IN_PROGRESS, "Статусы не совпадают.");

        subtask1 = new Subtask("Test Subtask1", "Test Subtask description",
                subtaskId1, IN_PROGRESS, epicId);
        taskManager.updateSubtask(subtask1);
        assertEquals(taskManager.getEpicStatus(epicId), IN_PROGRESS, "Статусы не совпадают.");
    }
}
