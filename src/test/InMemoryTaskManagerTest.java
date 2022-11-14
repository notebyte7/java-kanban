package test;

import manager.CrossingTaskException;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static tasks.Status.*;

public class InMemoryTaskManagerTest extends TaskManagerTest<TaskManager> {

    public InMemoryTaskManagerTest() {
        super(new InMemoryTaskManager());
    }

    @Test
    void getEpicStatus() throws CrossingTaskException {
        Epic epic = new Epic("Test tasks.Epic", "Test tasks.Epic description", NEW);
        final int epicId = taskManager.createEpic(epic);
        assertEquals(taskManager.getEpicStatus(epicId), NEW, "Статусы не совпадают.");

        Subtask subtask1 = new Subtask("Test tasks.Subtask", "Test tasks.Subtask description", NEW, epicId);
        final int subtaskId1 = taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Test tasks.Subtask", "Test tasks.Subtask description", NEW, epicId);
        final int subtaskId2 = taskManager.createSubtask(subtask2);
        assertEquals(taskManager.getEpicStatus(epicId), NEW, "Статусы не совпадают.");

        subtask1 = new Subtask("Test Subtask1", "Test tasks.Subtask description",
                subtaskId1, DONE, epicId);
        subtask2 = new Subtask("Test Subtask2", "Test tasks.Subtask description",
                subtaskId2, DONE, epicId);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        assertEquals(taskManager.getEpicStatus(epicId), DONE, "Статусы не совпадают.");

        subtask2 = new Subtask("Test Subtask2", "Test tasks.Subtask description",
                subtaskId2, IN_PROGRESS, epicId);
        taskManager.updateSubtask(subtask2);
        assertEquals(taskManager.getEpicStatus(epicId), IN_PROGRESS, "Статусы не совпадают.");

        subtask1 = new Subtask("Test Subtask1", "Test tasks.Subtask description",
                subtaskId1, IN_PROGRESS, epicId);
        taskManager.updateSubtask(subtask1);
        assertEquals(taskManager.getEpicStatus(epicId), IN_PROGRESS, "Статусы не совпадают.");
    }
}
