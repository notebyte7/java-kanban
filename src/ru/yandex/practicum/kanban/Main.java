package ru.yandex.practicum.kanban;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        taskManager.createTask(new Task("Задача0", "Описание0", TaskManager.uid, "NEW"));
        taskManager.createTask(new Task("Задача1", "Описание1", TaskManager.uid, "NEW"));

        taskManager.createEpic(new Epic("Эпик1", "Описание-эпик1", TaskManager.uid,
                "NEW", new ArrayList()));

        taskManager.createSubtask(new Subtask("Суб1", "Описание-суб1", TaskManager.uid,
                "NEW", 2));
        taskManager.createSubtask(new Subtask("Суб1", "Описание-суб1", TaskManager.uid,
                "NEW", 2));

        System.out.println(taskManager.tasksHashMap.get(0).id);
        System.out.println(taskManager.tasksHashMap.get(1).id);
        System.out.println(taskManager.epicsHashMap.get(2).subtaskIds);
        System.out.println(taskManager.subtasksHashMap.get(3).epicId);
        System.out.println(taskManager.subtasksHashMap.get(4).epicId);


    }

}
