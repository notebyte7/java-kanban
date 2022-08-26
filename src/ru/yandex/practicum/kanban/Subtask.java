package ru.yandex.practicum.kanban;

public class Subtask extends Task {
    int epicId;

    public Subtask(String name, String description, int id, String status, int epicId) {
        super(name, description, id, status);
        this.epicId = epicId;
    }
}

