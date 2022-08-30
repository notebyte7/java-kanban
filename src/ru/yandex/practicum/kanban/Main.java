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

        taskManager.createTask(new Task("Задача0", "Описание0", Status.NEW));
        taskManager.createTask(new Task("Задача1", "Описание1", Status.NEW));

        taskManager.createEpic(new Epic("Эпик0", "Описание-эпик0", Status.NEW, new ArrayList()));
        taskManager.createSubtask(new Subtask("Суб0", "Описание-суб0", Status.NEW, 2));
        taskManager.createSubtask(new Subtask("Суб1", "Описание-суб1", Status.NEW, 2));

        taskManager.createEpic(new Epic("Эпик1", "Описание-эпик1", Status.NEW, new ArrayList()));
        taskManager.createSubtask(new Subtask("Суб2", "Описание-суб2", Status.NEW, 5));

        System.out.println(taskManager.getTasksHashMap().values() + "\n" + taskManager.getEpicsHashMap().values() +
                "\n" + taskManager.getSubtasksHashMap().values());

        taskManager.updateTask(new Task("Задача1-обн", "Описание1-обн", 0,
                Status.DONE));
        taskManager.updateSubtask(new Subtask("Суб0-обн", "Описание-суб0-обн", 3,
                Status.DONE, 2));

        System.out.println("\n" + taskManager.getTasksHashMap().values() + "\n" +
                taskManager.getEpicsHashMap().values() + "\n" + taskManager.getSubtasksHashMap().values());

        taskManager.removeSubtask(4);
        taskManager.removeEpic(5);

        System.out.println("\n" + taskManager.getTaskList() + "\n" + taskManager.getEpicList() + "\n" +
                taskManager.getSubtaskList());
    }
}
