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
        taskManager.createSubtask(new Subtask("Суб2", "Описание-суб2", Status.NEW, 2));

        taskManager.createEpic(new Epic("Эпик1", "Описание-эпик1", Status.NEW));

        taskManager.getTask(0);
        System.out.println("\n" + "getHistory1" + "\n" + taskManager.getHistory());
        taskManager.getTask(1);
        System.out.println("\n" + "getHistory2" + "\n" + taskManager.getHistory());
        taskManager.getEpic(2);
        System.out.println("\n" + "getHistory3" + "\n" + taskManager.getHistory());
        taskManager.getSubtask(3);
        System.out.println("\n" + "getHistory4" + "\n" + taskManager.getHistory());
        taskManager.getTask(1);
        System.out.println("\n" + "getHistory5" + "\n" + taskManager.getHistory());
        taskManager.getSubtask(5);
        System.out.println("\n" + "getHistory6" + "\n" + taskManager.getHistory());
        taskManager.getSubtask(4);
        System.out.println("\n" + "getHistory7" + "\n" + taskManager.getHistory());
        taskManager.getTask(0);
        System.out.println("\n" + "getHistory8" + "\n" + taskManager.getHistory());
        taskManager.getTask(1);
        System.out.println("\n" + "getHistory9" + "\n" + taskManager.getHistory());
        taskManager.getSubtask(3);
        System.out.println("\n" + "getHistory10" + "\n" + taskManager.getHistory());
        taskManager.getEpic(6);
        System.out.println("\n" + "getHistory11" + "\n" + taskManager.getHistory());

        taskManager.removeTask(0);
        System.out.println("\n" + "getHistory12" + "\n" + taskManager.getHistory());

        taskManager.removeEpic(2);
        System.out.println("\n" + "getHistory13" + "\n" + taskManager.getHistory());


    }
}
