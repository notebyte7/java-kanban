import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.manager.FileBackedTasksManager;
import ru.yandex.practicum.kanban.manager.TaskManager;
import ru.yandex.practicum.kanban.tasks.*;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.kanban.tasks.Status.*;


import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class FileBackedTasksManagerTest extends TaskManagerTest<TaskManager> {

    public FileBackedTasksManagerTest() {
        super(new FileBackedTasksManager());
    }

    @Test
    void save() throws IOException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW,
                LocalDateTime.of(2022, 11, 1, 12, 00), 30);
        final int taskId = getTaskManager().createTask(task);
        Epic epic = new Epic("Test epic", "Test Epic description", NEW);
        int epicId = getTaskManager().createEpic(epic);
        Subtask subtask = new Subtask("Test subtask", "Test subtask description", NEW,
                LocalDateTime.of(2022, 11, 1, 13, 00), 30, epicId);
        final int subtaskId = getTaskManager().createSubtask(subtask);
        getTaskManager().save("manager.csv");

        FileBackedTasksManager testManager = FileBackedTasksManager.loadFromFile(new File("manager.csv"));
        assertEquals(testManager.getTaskList().get(0), task, "строки не совпадают");
        assertEquals(testManager.getEpicList().get(0), epic, "строки не совпадают");
        assertEquals(testManager.getSubtaskList().get(0), subtask, "строки не совпадают");

    }


    @Test
    void loadFromFile() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", 1, NEW,
                LocalDateTime.of(2022, 11, 1, 12, 00), 30);
        final int taskId = getTaskManager().createTask(task);
        Epic epic = new Epic("Test epic", "Test Epic description", 2, NEW);
        int epicId = getTaskManager().createEpic(epic);
        Subtask subtask = new Subtask("Test subtask", "Test subtask description", 3, NEW,
                LocalDateTime.of(2022, 11, 1, 13, 00), 30, epicId);
        final int subtaskId = getTaskManager().createSubtask(subtask);
        getTaskManager().save("manager.csv");

        FileBackedTasksManager testManager = FileBackedTasksManager.loadFromFile(new File("manager.csv"));
        assertEquals(testManager.getTaskList().get(0), task, "таски не совпадают");
        assertEquals(testManager.getSubtaskList().get(0), subtask, "сабтаски не совпадают");
        assertEquals(testManager.getEpicList().get(0), getTaskManager().getEpic(epicId), "'эпики не совпадают");

        assertEquals(testManager.getHistory(), new ArrayList(), "история не пуста");
        testManager.getTask(taskId);
        assertEquals(testManager.getHistory().get(0), task, "неправильная история");
        testManager.getEpic(epicId);
        assertEquals(testManager.getHistory().get(1), epic, "неправильная история");
        testManager.getSubtask(subtaskId);
        assertEquals(testManager.getHistory().get(2), subtask, "неправильная история");

    }
}