package ru.yandex.practicum.kanban;

import ru.yandex.practicum.kanban.manager.Managers;
import ru.yandex.practicum.kanban.manager.TaskManager;
import ru.yandex.practicum.kanban.tasks.Status;
import ru.yandex.practicum.kanban.tasks.Task;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;

public class Main {
    public static void main(String[] args) {
        //Проверка по условиям из задания
        TaskManager taskManager = Managers.getDefault();

        taskManager.createTask(new Task("Задача0", "Описание0", Status.NEW));
        taskManager.createTask(new Task("Задача1", "Описание1", Status.NEW));

        taskManager.createEpic(new Epic("Эпик0", "Описание-эпик0", Status.NEW));
        taskManager.createSubtask(new Subtask("Суб0", "Описание-суб0", Status.NEW, 2));
        taskManager.createSubtask(new Subtask("Суб1", "Описание-суб1", Status.NEW, 2));

        taskManager.createEpic(new Epic("Эпик1", "Описание-эпик1", Status.NEW));
        taskManager.createSubtask(new Subtask("Суб2", "Описание-суб2", Status.NEW, 5));

        System.out.println(taskManager.getTaskList() + "\n" + taskManager.getEpicList() +
                "\n" + taskManager.getSubtaskList());

        taskManager.updateTask(new Task("Задача1-обн", "Описание1-обн", 0,
                Status.DONE));
        taskManager.updateSubtask(new Subtask("Суб0-обн", "Описание-суб0-обн", 3,
                Status.DONE, 2));

        System.out.println("\n" + taskManager.getTaskList() + "\n" + taskManager.getEpicList() +
                "\n" + taskManager.getSubtaskList());

        taskManager.removeSubtask(4);
        taskManager.removeEpic(5);

        System.out.println("\n" + taskManager.getTaskList() + "\n" + taskManager.getEpicList() +
                "\n" + taskManager.getSubtaskList());

        taskManager.getTask(0);
        taskManager.getTask(1);
        taskManager.getEpic(2);
        taskManager.getTask(1);
        taskManager.getSubtask(3);
        taskManager.getTask(0);
        taskManager.getTask(1);
        taskManager.getEpic(2);
        taskManager.getTask(1);
        taskManager.getSubtask(3);
        System.out.println("\n" + "getHistory" + "\n" + taskManager.getHistory());
        taskManager.getEpic(2);
        System.out.println("\n" + "getHistory" + "\n" + taskManager.getHistory());
        taskManager.getTask(1);
        System.out.println("\n" + "getHistory" + "\n" + taskManager.getHistory());
        taskManager.getEpic(2);
        System.out.println("\n" + "getHistory" + "\n" + taskManager.getHistory());
        taskManager.getSubtask(3);
        System.out.println("\n" + "getHistory" + "\n" + taskManager.getHistory());
    }
}
