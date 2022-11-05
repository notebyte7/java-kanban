package ru.yandex.practicum.kanban.manager;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.tasks.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.yandex.practicum.kanban.tasks.Status.*;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class FileBackedTasksManagerTest extends TaskManagerTest<TaskManager> {

    private final static FileBackedTasksManager taskManager = new FileBackedTasksManager();

    public FileBackedTasksManagerTest() {
        super(taskManager);
    }

    @Test
    void save() throws IOException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW,
                LocalDateTime.of(2022, 11, 1, 12, 00), 30);
        final int taskId = taskManager.createTask(task);
        Epic epic = new Epic("Test epic", "Test Epic description", NEW);
        int epicId = taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Test subtask", "Test subtask description", NEW,
                LocalDateTime.of(2022, 11, 1, 13, 00), 30, epicId);
        final int subtaskId = taskManager.createSubtask(subtask);
        taskManager.save("manager.csv");

        String content = Files.readString(Path.of("manager.csv"));
        String content2 = "id,type,name,status,description,startTime,duration,epic\n" + task.toString() + "\n" + epic.toString() +
                "\n" + subtask.toString() + "\n\n";
        assertEquals(content, content2, "строки не совпадают");

    }


    @Test
    void loadFromFile() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", 1, NEW,
                LocalDateTime.of(2022, 11, 1, 12, 00), 30);
        final int taskId = taskManager.createTask(task);
        Epic epic = new Epic("Test epic", "Test Epic description", 2, NEW);
        int epicId = taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Test subtask", "Test subtask description", 3, NEW,
                LocalDateTime.of(2022, 11, 1, 13, 00), 30, epicId);
        final int subtaskId = taskManager.createSubtask(subtask);
        taskManager.save("manager.csv");

        FileBackedTasksManager testManager = FileBackedTasksManager.loadFromFile(new File("manager.csv"));
        task = testManager.getTaskList().get(0);
        Task savedTask = new Task("Test addNewTask", "Test addNewTask description", 1, NEW,
                LocalDateTime.of(2022, 11, 1, 12, 00), 30);
        assertEquals(task, savedTask, "таски не совпадают");
        subtask = testManager.getSubtaskList().get(0);
        Subtask savedsubtask = new Subtask("Test subtask", "Test subtask description", 3, NEW,
                LocalDateTime.of(2022, 11, 1, 13, 00), 30, epicId);
        assertEquals(subtask, savedsubtask, "сабтаски не совпадают");
        epic = testManager.getEpicList().get(0);
        Epic savedEpic = new Epic("Test epic", "Test Epic description", 2,
                NEW, LocalDateTime.of(2022, 11, 1, 13, 00), 30);
        assertEquals(epic, savedEpic, "'эпики не совпадают");
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
}