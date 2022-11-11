package test;

import manager.CrossingTaskException;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static tasks.Status.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    public T getTaskManager() {
        return taskManager;
    }

    public TaskManagerTest(T taskManager) {
        this.taskManager = taskManager;
    }

    @Test
    void createTask() throws CrossingTaskException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);
        final int taskId = taskManager.createTask(task);

        Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTaskList();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void createTaskWithTime() throws CrossingTaskException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW,
                LocalDateTime.of(2022, 11, 1, 12, 00), 30);
        int taskId = taskManager.createTask(task);

        Epic epic = new Epic("Test epic", "Test tasks.Epic description", NEW);
        int epicId = taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Test subtask", "Test subtask description", NEW,
                LocalDateTime.of(2022, 11, 1, 13, 00), 30, epicId);
        int subtaskId = taskManager.createSubtask(subtask);

        assertEquals(taskManager.getTask(taskId).getEndTime(), LocalDateTime.of(2022, 11, 1, 12, 30),
                "неправильный расчет endTime");
        assertEquals(taskManager.getSubtask(subtaskId).getEndTime(), LocalDateTime.of(2022, 11, 1, 13, 30),
                "неправильный расчет endTime");
        assertEquals(taskManager.getEpic(epicId).getStartTime(), LocalDateTime.of(2022, 11, 1, 13, 00),
                "неправильный расчет startTime");
        assertEquals(taskManager.getEpic(epicId).getDuration(), 30, "неправильный расчет Duration");
        assertEquals(taskManager.getEpic(epicId).getEndTime(), LocalDateTime.of(2022, 11, 1, 13, 30),
                "неправильный расчет endTime");

        subtask = new Subtask("Test subtask", "Test subtask description", NEW,
                LocalDateTime.of(2022, 11, 2, 12, 00), 60, epicId);
        taskManager.createSubtask(subtask);

        assertEquals(taskManager.getEpic(epicId).getStartTime(), LocalDateTime.of(2022, 11, 1,
                        13, 00),
                "неправильный расчет startTime");
        assertEquals(taskManager.getEpic(epicId).getDuration(), 90, "неправильный расчет Duration");
        assertEquals(taskManager.getEpic(epicId).getEndTime(), LocalDateTime.of(2022, 11, 2,
                        13, 00),
                "неправильный расчет endTime");

        subtask = new Subtask("Test subtask", "Test subtask description", NEW,
                LocalDateTime.of(2022, 11, 17, 12, 00), 300, epicId);
        subtaskId = taskManager.createSubtask(subtask);
        assertEquals(taskManager.getEpic(epicId).getStartTime(), LocalDateTime.of(2022, 11, 1,
                        13, 00),
                "неправильный расчет startTime");
        assertEquals(taskManager.getEpic(epicId).getDuration(), 390, "неправильный расчет Duration");
        assertEquals(taskManager.getEpic(epicId).getEndTime(), LocalDateTime.of(2022, 11, 17,
                        17, 00),
                "неправильный расчет endTime");

        taskManager.removeSubtask(subtaskId);
        assertEquals(taskManager.getEpic(epicId).getStartTime(), LocalDateTime.of(2022, 11, 1,
                        13, 00),
                "неправильный расчет startTime");
        assertEquals(taskManager.getEpic(epicId).getDuration(), 90, "неправильный расчет Duration");
        assertEquals(taskManager.getEpic(epicId).getEndTime(), LocalDateTime.of(2022, 11, 2,
                        13, 00),
                "неправильный расчет endTime");
    }

    @Test
    void checkCrossingTask() throws CrossingTaskException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW,
                LocalDateTime.of(2022, 11, 1, 12, 00), 30);
        taskManager.createTask(task);

        Epic epic = new Epic("Test epic", "Test tasks.Epic description", NEW);
        int epicId = taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Test subtask", "Test subtask description", NEW,
                LocalDateTime.of(2022, 11, 1, 13, 00), 30, epicId);
        taskManager.createSubtask(subtask);

        task = new Task("Test addNewTask", "Test addNewTask description", NEW,
                LocalDateTime.of(2022, 11, 1, 12, 00), 15);
        Task finalTask = task;
        CrossingTaskException thrown = Assertions
                .assertThrows(CrossingTaskException.class, () -> {
                    taskManager.createTask(finalTask);
                }, "Пересекающиеся задачи");
        Assertions.assertEquals(
                "Задачи пересекаются!", thrown.getMessage());

        subtask = new Subtask("Test subtask", "Test subtask description", NEW,
                LocalDateTime.of(2022, 11, 1, 13, 20), 10, epicId);

        Subtask finalSubtask = subtask;
        thrown = Assertions
                .assertThrows(CrossingTaskException.class, () -> {
                    taskManager.createSubtask(finalSubtask);
                }, "Пересекающиеся задачи");
        Assertions.assertEquals(
                "Задачи пересекаются!", thrown.getMessage());

        task = new Task("Test addNewTask", "Test addNewTask description", NEW,
                LocalDateTime.of(2022, 11, 1, 12, 00), 600);
        Task finalTask1 = task;
        thrown = Assertions
                .assertThrows(CrossingTaskException.class, () -> {
                    taskManager.createTask(finalTask1);
                }, "Пересекающиеся задачи");
        Assertions.assertEquals(
                "Задачи пересекаются!", thrown.getMessage());

        task = new Task("Test addNewTask", "Test addNewTask description", NEW,
                LocalDateTime.of(2022, 11, 1, 13, 30), 15);
    }

    @Test
    void getTaskList() throws CrossingTaskException {
        final List<Task> tasks = taskManager.getTaskList();

        assertNotNull(tasks, "Список не найден");
        assertEquals(tasks, new ArrayList<>(), "Вначале список не пустой");

        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);
        taskManager.createTask(task);

        final List<Task> tasks1 = taskManager.getTaskList();

        assertNotNull(tasks1, "Задачи на возвращаются.");
        assertEquals(1, tasks1.size(), "Неверное количество задач.");
        assertEquals(task, tasks1.get(0), "Задачи не совпадают.");
    }

    @Test
    void removeAllTasks() throws CrossingTaskException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);
        taskManager.createTask(task);

        final List<Task> tasks = taskManager.getTaskList();

        assertEquals(1, tasks.size(), "Неверное количество задач.");

        taskManager.removeAllTasks();
        final List<Task> tasks0 = taskManager.getTaskList();
        assertEquals(tasks0, new ArrayList<>(), "Список не очистился");
    }

    @Test
    void getTask() throws CrossingTaskException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);
        final int taskId = taskManager.createTask(task);

        Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        assertNull(taskManager.getTask(7777), "несуществующая задача");
    }

    @Test
    void updateTask() throws CrossingTaskException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);
        final int taskId = taskManager.createTask(task);
        Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        task = new Task("Test addNewTask - change", "Test addNewTask description - change",
                taskId, IN_PROGRESS);
        taskManager.updateTask(task);
        Task savedTask1 = taskManager.getTask(taskId);

        assertNotNull(savedTask1, "Задача не найдена.");
        assertEquals(task, savedTask1, "Задачи не обновлена.");

        final List<Task> tasks = taskManager.getTaskList();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void removeTask() throws CrossingTaskException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);
        final int taskId = taskManager.createTask(task);
        Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        taskManager.removeTask(taskId);
        assertNull(taskManager.getTask(taskId), "несуществующая задача");
    }

    // Для Эпиков
    @Test
    void createEpic() {
        Epic epic = new Epic("Test tasks.Epic", "Test tasks.Epic description", NEW);
        final int epicId = taskManager.createEpic(epic);

        Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Эпик не найдена.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        final List<Epic> epics = taskManager.getEpicList();

        assertNotNull(epics, "Задачи на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    void getEpicList() {
        final List<Epic> epics = taskManager.getEpicList();

        assertNotNull(epics, "Список не найден");
        assertEquals(epics, new ArrayList<>(), "Вначале список не пустой");

        Epic epic = new Epic("Test createEpic", "Test createEpic description", NEW);
        taskManager.createEpic(epic);

        final List<Epic> epic1 = taskManager.getEpicList();

        assertNotNull(epic, "Задачи на возвращаются.");
        assertEquals(1, epic1.size(), "Неверное количество задач.");
        assertEquals(epic, epic1.get(0), "Задачи не совпадают.");
    }

    @Test
    void removeAllEpics() {
        Epic epic = new Epic("Test tasks.Epic", "Test tasks.Epic description", NEW);
        taskManager.createEpic(epic);

        final List<Epic> epics = taskManager.getEpicList();

        assertEquals(1, epics.size(), "Неверное количество задач.");

        taskManager.removeAllEpics();
        final List<Epic> epics0 = taskManager.getEpicList();
        assertEquals(epics0, new ArrayList<>(), "Список не очистился");
    }

    @Test
    void getEpic() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", NEW);
        final int epicId = taskManager.createEpic(epic);

        Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        assertNull(taskManager.getEpic(7777), "несуществующая задача");
    }

    @Test
    void updateEpic() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", NEW);
        final int epicId = taskManager.createEpic(epic);
        Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        epic = new Epic("Test addNewEpic - change", "Test addNewEpic description - change",
                epicId, IN_PROGRESS);
        taskManager.updateEpic(epic);
        Task savedEpic1 = taskManager.getEpic(epicId);

        assertNotNull(savedEpic1, "Эпик не найдена.");
        assertEquals(epic, savedEpic1, "Эпик не обновлен.");

        final List<Epic> epics = taskManager.getEpicList();

        assertNotNull(epics, "Задачи на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    void removeEpic() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", NEW);
        final int epicId = taskManager.createEpic(epic);
        Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        taskManager.removeEpic(epicId);
        assertNull(taskManager.getEpic(epicId), "несуществующая задача");
    }

    @Test
    void getEpicSubtaskList() throws CrossingTaskException {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", NEW);
        final int epicId = taskManager.createEpic(epic);
        assertEquals(taskManager.getEpicSubtaskList(epicId), new ArrayList<>(), "список subtaskIds не пустой");
        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description",
                NEW, epicId);
        taskManager.createSubtask(subtask);
        List<Subtask> tempEpicSubtaskList = new ArrayList<>();
        tempEpicSubtaskList.add(subtask);
        assertEquals(taskManager.getEpicSubtaskList(epicId), tempEpicSubtaskList,
                "список subtaskIds не соответствует");

        assertNull(taskManager.getEpicSubtaskList(7777), "несуществующая задача");
    }

    //Сабтаски
    @Test
    void createSubtask() throws CrossingTaskException {
        Epic epic = new Epic("Test tasks.Epic", "Test tasks.Epic description", NEW);
        final int epicId = taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Test tasks.Subtask", "Test tasks.Subtask description", NEW, epicId);
        final int subtaskId = taskManager.createSubtask(subtask);

        final Subtask savedSubtask = taskManager.getSubtask(subtaskId);

        assertNotNull(savedSubtask, "Сабтаск не найден.");
        assertEquals(subtask, savedSubtask, "Сабтаски не совпадают.");

        final List<Subtask> subtasks = taskManager.getSubtaskList();

        assertNotNull(subtasks, "Сабтаски не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask, subtasks.get(0), "Сабтаски не совпадают.");
    }

    @Test
    void getSubtaskList() throws CrossingTaskException {

        final List<Subtask> subtasks = taskManager.getSubtaskList();

        assertNotNull(subtasks, "Список не найден");
        assertEquals(subtasks, new ArrayList<>(), "Вначале список не пустой");

        Epic epic = new Epic("Test tasks.Epic", "Test tasks.Epic description", NEW);
        final int epicId = taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Test tasks.Subtask", "Test tasks.Subtask description", NEW, epicId);
        taskManager.createSubtask(subtask);

        final List<Epic> subtask1 = taskManager.getEpicList();

        assertNotNull(epic, "Задачи на возвращаются.");
        assertEquals(1, subtask1.size(), "Неверное количество задач.");
        assertEquals(epic, subtask1.get(0), "Задачи не совпадают.");
    }

    @Test
    void removeAllSubtasks() throws CrossingTaskException {
        Epic epic = new Epic("Test tasks.Epic", "Test tasks.Epic description", NEW);
        final int epicId = taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Test tasks.Subtask", "Test tasks.Subtask description", NEW, epicId);
        taskManager.createSubtask(subtask);

        final List<Subtask> subtasks = taskManager.getSubtaskList();

        assertEquals(1, subtasks.size(), "Неверное количество задач.");

        taskManager.removeAllEpics();
        final List<Subtask> subtask0 = taskManager.getSubtaskList();
        assertEquals(subtask0, new ArrayList<>(), "Список не очистился");
    }

    @Test
    void getSubtask() throws CrossingTaskException {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", NEW);
        final int epicId = taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Test tasks.Subtask", "Test tasks.Subtask description", NEW, epicId);
        final int subtaskId = taskManager.createSubtask(subtask);

        final Subtask savedSubtask = taskManager.getSubtask(subtaskId);

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");

        assertNull(taskManager.getSubtask(7777), "несуществующая задача");
    }

    @Test
    void updateSubtask() throws CrossingTaskException {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", NEW);
        final int epicId = taskManager.createEpic(epic);
        Epic savedEpic = taskManager.getEpic(epicId);

        Subtask subtask = new Subtask("Test tasks.Subtask", "Test tasks.Subtask description", NEW, epicId);
        final int subtaskId = taskManager.createSubtask(subtask);
        final Subtask savedSubtask = taskManager.getSubtask(subtaskId);

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");

        subtask = new Subtask("Test tasks.Subtask - change", "Test tasks.Subtask description - change",
                subtaskId, IN_PROGRESS, epicId);
        taskManager.updateSubtask(subtask);
        final Subtask savedSubtask1 = taskManager.getSubtask(subtaskId);

        assertNotNull(savedSubtask1, "Сабтаск не найдена.");
        assertEquals(subtask, savedSubtask1, "Сабтаск не обновлен.");

        final List<Subtask> subtasks = taskManager.getSubtaskList();

        assertNotNull(subtasks, "Задачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask, subtasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void removeSubtask() throws CrossingTaskException {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", NEW);
        final int epicId = taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Test tasks.Subtask", "Test tasks.Subtask description", NEW, epicId);
        final int subtaskId = taskManager.createSubtask(subtask);
        final Subtask savedSubtask = taskManager.getSubtask(subtaskId);

        assertNotNull(savedSubtask, "Сабтаск не найден.");
        assertEquals(subtask, savedSubtask, "Сабтаски не совпадают.");

        taskManager.removeSubtask(subtaskId);
        assertNull(taskManager.getSubtask(subtaskId), "несуществующая задача");
    }
}
