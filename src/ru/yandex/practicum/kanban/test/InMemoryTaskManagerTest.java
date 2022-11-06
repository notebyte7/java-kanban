import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.manager.InMemoryTaskManager;
import ru.yandex.practicum.kanban.manager.TaskManager;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.yandex.practicum.kanban.tasks.Status.*;

public class InMemoryTaskManagerTest extends TaskManagerTest<TaskManager> {

    public InMemoryTaskManagerTest() {
        super(new InMemoryTaskManager());
    }

    @Test
    void getEpicStatus() {
        Epic epic = new Epic("Test Epic", "Test Epic description", NEW);
        final int epicId = getTaskManager().createEpic(epic);
        assertEquals(getTaskManager().getEpicStatus(epicId), NEW, "Статусы не совпадают.");

        Subtask subtask1 = new Subtask("Test Subtask", "Test Subtask description", NEW, epicId);
        final int subtaskId1 = getTaskManager().createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Test Subtask", "Test Subtask description", NEW, epicId);
        final int subtaskId2 = getTaskManager().createSubtask(subtask2);
        assertEquals(getTaskManager().getEpicStatus(epicId), NEW, "Статусы не совпадают.");

        subtask1 = new Subtask("Test Subtask1", "Test Subtask description",
                subtaskId1, DONE, epicId);
        subtask2 = new Subtask("Test Subtask2", "Test Subtask description",
                subtaskId2, DONE, epicId);
        getTaskManager().updateSubtask(subtask1);
        getTaskManager().updateSubtask(subtask2);
        assertEquals(getTaskManager().getEpicStatus(epicId), DONE, "Статусы не совпадают.");

        subtask2 = new Subtask("Test Subtask2", "Test Subtask description",
                subtaskId2, IN_PROGRESS, epicId);
        getTaskManager().updateSubtask(subtask2);
        assertEquals(getTaskManager().getEpicStatus(epicId), IN_PROGRESS, "Статусы не совпадают.");

        subtask1 = new Subtask("Test Subtask1", "Test Subtask description",
                subtaskId1, IN_PROGRESS, epicId);
        getTaskManager().updateSubtask(subtask1);
        assertEquals(getTaskManager().getEpicStatus(epicId), IN_PROGRESS, "Статусы не совпадают.");
    }
}
