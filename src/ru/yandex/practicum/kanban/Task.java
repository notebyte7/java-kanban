package ru.yandex.practicum.kanban;
import java.util.HashMap;

public class Task {
    String name;
    String description;
    int id;
    String status;


    public Task(String name, String description, int id, String status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
    }


}

