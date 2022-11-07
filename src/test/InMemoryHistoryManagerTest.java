package test;

import history.InMemoryHistoryManager;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static tasks.Status.*;

public class InMemoryHistoryManagerTest {

    private final static InMemoryHistoryManager historyManager = new InMemoryHistoryManager();


    @Test
    void HistoryTest() {
        List<Task> tempHistoryList = new ArrayList<>();
        Task task = new Task("Test addNewTask", "Test addNewTask description", 0, NEW);
        assertEquals(historyManager.getHistory().size(), 0, "История не пустая.");
        historyManager.add(task);
        tempHistoryList.add(task);
        assertEquals(historyManager.getHistory(), tempHistoryList, "история некорректна");

        Task task1 = new Task("Test addNewTask1", "Test addNewTask description1", 1, NEW);
        historyManager.add(task1);
        tempHistoryList.add(task1);
        assertEquals(historyManager.getHistory(), tempHistoryList, "история некорректна");

        historyManager.add(task);
        tempHistoryList.remove(task);
        tempHistoryList.add(task);
        assertEquals(historyManager.getHistory(), tempHistoryList, "порядок нарушен");

        Task task2 = new Epic("Test addNewEpic2", "Test addNewEpic description2", 2, NEW);
        historyManager.add(task2);
        tempHistoryList.add(task2);
        assertEquals(historyManager.getHistory(), tempHistoryList, "история некорректна");

        Task task3 = new Subtask("Test subtask3", "Test subtask description2", 3, NEW, 2);
        historyManager.add(task3);
        tempHistoryList.add(task3);
        assertEquals(historyManager.getHistory(), tempHistoryList, "история некорректна");

        historyManager.remove(2);
        tempHistoryList.remove(2);
        assertEquals(historyManager.getHistory(), tempHistoryList, "история некорректна");
    }
}
