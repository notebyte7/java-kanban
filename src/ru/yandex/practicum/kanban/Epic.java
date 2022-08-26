package ru.yandex.practicum.kanban;
import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(String name, String description, int id, String status, ArrayList subtaskIds) {
        super(name, description, id, status);
    }
}