package test;

import manager.FileBackedTasksManager;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;
import static tasks.Status.*;


import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManagerTest extends TaskManagerTest<TaskManager> {

    public FileBackedTasksManagerTest() {
        super(new FileBackedTasksManager());
    }

    @Test
    void save() throws IOException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW,
                LocalDateTime.of(2022, 11, 1, 12, 00), 30);
        List<Task> tempTaskList = new ArrayList<>();
        tempTaskList.add(task);
        final int taskId = getTaskManager().createTask(task);
        Epic epic = new Epic("Test epic", "Test tasks.Epic description", NEW);
        int epicId = getTaskManager().createEpic(epic);
        Subtask subtask = new Subtask("Test subtask", "Test subtask description", NEW,
                LocalDateTime.of(2022, 11, 1, 13, 00), 30, epicId);
        final int subtaskId = getTaskManager().createSubtask(subtask);
        getTaskManager().save("manager.csv");

        FileBackedTasksManager testManager = FileBackedTasksManager.loadFromFile(new File("manager.csv"));
        assertEquals(getTaskManager().getTaskList(), testManager.getTaskList(), "таски не совпадают");
        assertEquals(getTaskManager().getEpicList(), testManager.getEpicList(), "эпики не совпадают");
        assertEquals(getTaskManager().getSubtaskList(), testManager.getSubtaskList(), "сабтаски не совпадают");

    }


    @Test
    void loadFromFile() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", 1, NEW,
                LocalDateTime.of(2022, 11, 1, 12, 00), 30);
        final int taskId = getTaskManager().createTask(task);
        Epic epic = new Epic("Test epic", "Test tasks.Epic description", 2, NEW);
        int epicId = getTaskManager().createEpic(epic);
        Subtask subtask = new Subtask("Test subtask", "Test subtask description", 3, NEW,
                LocalDateTime.of(2022, 11, 1, 13, 00), 30, epicId);
        final int subtaskId = getTaskManager().createSubtask(subtask);
        getTaskManager().save("manager.csv");

        FileBackedTasksManager testManager = FileBackedTasksManager.loadFromFile(new File("manager.csv"));
        assertEquals(testManager.getTaskList().get(0), task, "таски не совпадают");
        assertEquals(testManager.getSubtaskList().get(0), subtask, "сабтаски не совпадают");
        assertEquals(testManager.getEpicList().get(0), getTaskManager().getEpic(epicId), "'эпики не совпадают");
        assertEquals(testManager.getHistory().size(), 0, "история не пуста");

        getTaskManager().getTask(taskId);
        getTaskManager().getEpic(epicId);
        getTaskManager().getSubtask(subtaskId);
        getTaskManager().save("manager.csv");

        testManager = FileBackedTasksManager.loadFromFile(new File("manager.csv"));
        assertEquals(testManager.getHistory().get(0), task, "неправильная история");
        assertEquals(testManager.getHistory().get(1), epic, "неправильная история");
        assertEquals(testManager.getHistory().get(2), subtask, "неправильная история");

    }
}