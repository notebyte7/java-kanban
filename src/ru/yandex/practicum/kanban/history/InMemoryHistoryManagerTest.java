package ru.yandex.practicum.kanban.history;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.kanban.tasks.Status.*;

public class InMemoryHistoryManagerTest implements HistoryManager {

    private final static InMemoryHistoryManager historyManager = new InMemoryHistoryManager();


    @Test
    void HistoryTest() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", 0, NEW);
        historyManager.add(task);
        assertNotNull(historyManager.getHistory(), "История не пустая.");
        assertEquals(1, historyManager.getHistory().size(), "История не пустая.");
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size(), "Неправильное количество задач в истории");
        Task task1 = new Task("Test addNewTask1", "Test addNewTask description1", 1, NEW);
        historyManager.add(task1);
        Task task2 = new Epic("Test addNewEpic2", "Test addNewEpic description2", 2, NEW);
        historyManager.add(task2);
        Task task3 = new Task("Test addNewTask3", "Test addNewTask description3", 3, NEW);
        historyManager.add(task3);
        assertEquals(4, historyManager.getHistory().size(), "Неправильное количество задач в истории");


        historyManager.remove(0);
        assertEquals(3, historyManager.getHistory().size(), "Неправильное количество задач в истории");
        historyManager.remove(2);
        assertEquals(2, historyManager.getHistory().size(), "Неправильное количество задач в истории");
        historyManager.remove(3);
        assertEquals(1, historyManager.getHistory().size(), "Неправильное количество задач в истории");
    }


    @Override
    public List<Task> getHistory() {
        return null;
    }

    @Override
    public void add(Task task) {

    }

    @Override
    public void remove(int id) {

    }
}
