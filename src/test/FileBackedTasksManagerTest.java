package test;

import manager.CrossingTaskException;
import manager.FileBackedTasksManager;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tasks.Status.NEW;

public class FileBackedTasksManagerTest extends TaskManagerTest<TaskManager> {

    public FileBackedTasksManagerTest() {
        super(new FileBackedTasksManager());
    }
    
    @Test
    void save() throws IOException, CrossingTaskException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW,
                LocalDateTime.of(2022, 11, 1, 12, 00), 30);
        taskManager.createTask(task);
        Epic epic = new Epic("Test epic", "Test tasks.Epic description", NEW);
        int epicId = taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Test subtask", "Test subtask description", NEW,
                LocalDateTime.of(2022, 11, 1, 13, 00), 30, epicId);
        taskManager.createSubtask(subtask);
        taskManager.save();

        FileBackedTasksManager testManager = taskManager.load();
        assertEquals(taskManager.getTaskList(), testManager.getTaskList(), "таски не совпадают");
        assertEquals(taskManager.getEpicList(), testManager.getEpicList(), "эпики не совпадают");
        assertEquals(taskManager.getSubtaskList(), testManager.getSubtaskList(), "сабтаски не совпадают");

    }


    @Test
    void load() throws CrossingTaskException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", 1, NEW,
                LocalDateTime.of(2022, 11, 1, 12, 00), 30);
        final int taskId = getTaskManager().createTask(task);
        Epic epic = new Epic("Test epic", "Test tasks.Epic description", 2, NEW);
        int epicId = getTaskManager().createEpic(epic);
        Subtask subtask = new Subtask("Test subtask", "Test subtask description", 3, NEW,
                LocalDateTime.of(2022, 11, 1, 13, 00), 30, epicId);
        final int subtaskId = getTaskManager().createSubtask(subtask);
        getTaskManager().save();

        FileBackedTasksManager testManager = getTaskManager().load();
        assertEquals(testManager.getTaskList().get(0), task, "таски не совпадают");
        assertEquals(testManager.getSubtaskList().get(0), subtask, "сабтаски не совпадают");
        assertEquals(testManager.getEpicList().get(0), getTaskManager().getEpic(epicId), "'эпики не совпадают");
        assertEquals(testManager.getHistory().size(), 0, "история не пуста");

        getTaskManager().getTask(taskId);
        getTaskManager().getEpic(epicId);
        getTaskManager().getSubtask(subtaskId);
        getTaskManager().save();

        testManager = getTaskManager().load();
        assertEquals(testManager.getHistory().get(0), task, "неправильная история");
        assertEquals(testManager.getHistory().get(1), epic, "неправильная история");
        assertEquals(testManager.getHistory().get(2), subtask, "неправильная история");

    }
}