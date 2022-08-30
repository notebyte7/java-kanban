package ru.yandex.practicum.kanban.tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIds;

    public Epic(String name, String description, Status status, ArrayList subtaskIds) {
        super(name, description, status);
        this.subtaskIds = subtaskIds;
    }

    public Epic(String name, String description, int id, Status status, ArrayList subtaskIds) {
        super(name, description, id, status);
        this.subtaskIds = subtaskIds;
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    @Override
    public String toString() {
        return "Epic{" +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                ", subtaskIds=" + subtaskIds + '\'' +
                '}';
    }
}