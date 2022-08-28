package ru.yandex.practicum.kanban;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        //Проверка по условиям из задания
        TaskManager taskManager = new TaskManager();

        taskManager.createTask(new Task("Задача0", "Описание0", TaskManager.uid, "NEW"));
        taskManager.createTask(new Task("Задача1", "Описание1", TaskManager.uid, "NEW"));

        taskManager.createEpic(new Epic("Эпик0", "Описание-эпик0", TaskManager.uid,
                "NEW", new ArrayList()));
        taskManager.createSubtask(new Subtask("Суб0", "Описание-суб0", TaskManager.uid,
                "NEW", 2));
        taskManager.createSubtask(new Subtask("Суб1", "Описание-суб1", TaskManager.uid,
                "NEW", 2));

        taskManager.createEpic(new Epic("Эпик1", "Описание-эпик1", TaskManager.uid,
                "NEW", new ArrayList()));
        taskManager.createSubtask(new Subtask("Суб2", "Описание-суб2", TaskManager.uid,
                "NEW", 5));

        System.out.println(taskManager.tasksHashMap.values() + "\n" + taskManager.epicsHashMap.values() + "\n" +
                taskManager.subtasksHashMap.values());

        taskManager.updateTask(1, new Task("Задача1-обн", "Описание1-обн", 1,
                "DONE"));
        taskManager.updateSubtask(3, new Subtask("Суб0-обн", "Описание-суб0-обн", 3,
                "DONE", 2));

        System.out.println("\n" + taskManager.tasksHashMap.values() + "\n" + taskManager.epicsHashMap.values() + "\n" +
                taskManager.subtasksHashMap.values());

        taskManager.removeSubtask(4);
        taskManager.removeEpic(5);

        System.out.println("\n" + taskManager.tasksHashMap.values() + "\n" + taskManager.epicsHashMap.values() + "\n" +
                taskManager.subtasksHashMap.values());
    }
}
