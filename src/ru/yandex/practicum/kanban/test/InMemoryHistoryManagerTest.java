import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.history.InMemoryHistoryManager;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import ru.yandex.practicum.kanban.tasks.Task;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.kanban.tasks.Status.*;

public class InMemoryHistoryManagerTest {

    private final static InMemoryHistoryManager historyManager = new InMemoryHistoryManager();


    @Test
    void HistoryTest() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", 0, NEW);
        historyManager.add(task);
        assertNotNull(historyManager.getHistory(), "История не пустая.");
        assertEquals(1, historyManager.getHistory().size(), "История не пустая.");
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size(), "Неправильное количество задач в истории");
        assertEquals(historyManager.getHistory().get(0), task, "задачи не совпадают");

        Task task1 = new Task("Test addNewTask1", "Test addNewTask description1", 1, NEW);
        historyManager.add(task1);
        assertEquals(historyManager.getHistory().get(1), task1, "задачи не совпадают");

        historyManager.add(task);
        assertEquals(historyManager.getHistory().get(0), task1, "порядок нарушен");
        assertEquals(historyManager.getHistory().get(1), task, "порядок нарушен");

        Task task2 = new Epic("Test addNewEpic2", "Test addNewEpic description2", 2, NEW);
        historyManager.add(task2);
        assertEquals(historyManager.getHistory().get(2), task2, "эпик не совпадает");

        Task task3 = new Subtask("Test subtask3", "Test subtask description2", 3, NEW, 2);
        historyManager.add(task3);
        assertEquals(historyManager.getHistory().get(3), task3, "эпик не совпадает");

        historyManager.remove(0);
        assertEquals(historyManager.getHistory().get(1), task2, "эпик не совпадает");
        historyManager.remove(2);
        assertEquals(historyManager.getHistory().get(1), task3, "задачи не совпадает");
    }
}
