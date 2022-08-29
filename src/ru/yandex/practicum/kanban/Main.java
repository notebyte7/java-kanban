package ru.yandex.practicum.kanban;

import ru.yandex.practicum.kanban.tasks.Status;
import ru.yandex.practicum.kanban.tasks.Task;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        //Проверка по условиям из задания
        TaskManager taskManager = new TaskManager();

        taskManager.createTask(new Task("Задача0", "Описание0", TaskManager.getUid(), Status.NEW));
        taskManager.createTask(new Task("Задача1", "Описание1", TaskManager.getUid(), Status.NEW));

        taskManager.createEpic(new Epic("Эпик0", "Описание-эпик0", TaskManager.getUid(),
                Status.NEW, new ArrayList()));
        taskManager.createSubtask(new Subtask("Суб0", "Описание-суб0", TaskManager.getUid(),
                Status.NEW, 2));
        taskManager.createSubtask(new Subtask("Суб1", "Описание-суб1", TaskManager.getUid(),
                Status.NEW, 2));

        taskManager.createEpic(new Epic("Эпик1", "Описание-эпик1", TaskManager.getUid(),
                Status.NEW, new ArrayList()));
        taskManager.createSubtask(new Subtask("Суб2", "Описание-суб2", TaskManager.getUid(),
                Status.NEW, 5));

        System.out.println(taskManager.tasksHashMap.values() + "\n" + taskManager.epicsHashMap.values() + "\n" +
                taskManager.subtasksHashMap.values());

        taskManager.updateTask(new Task("Задача1-обн", "Описание1-обн", 1,
                Status.DONE));
        taskManager.updateSubtask(new Subtask("Суб0-обн", "Описание-суб0-обн", 3,
                Status.DONE, 2));

        System.out.println("\n" + taskManager.tasksHashMap.values() + "\n" + taskManager.epicsHashMap.values() + "\n" +
                taskManager.subtasksHashMap.values());

        taskManager.removeSubtask(4);
        taskManager.removeEpic(5);

        System.out.println("\n" + taskManager.getTaskList() + "\n" + taskManager.getEpicList() + "\n" +
                taskManager.getSubtaskList());
    }
}
