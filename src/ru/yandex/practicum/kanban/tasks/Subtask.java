package ru.yandex.practicum.kanban.tasks;

import java.time.LocalDateTime;

public class Subtask extends Task {
    private final int epicId;
    private final TaskType type = TaskType.SUBTASK;

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    public int getEpicId() {
        return epicId;
    }

    public Subtask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, int id, Status status, int epicId) {
        super(name, description, id, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Status status, LocalDateTime startTime, int duration,
                   int epicId) {
        super(name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, int id, Status status, LocalDateTime startTime, int duration,
                   int epicId) {
        super(name, description, id, status, startTime, duration);
        this.epicId = epicId;
    }


    @Override
    public String toString() {
        if (getStartTime() != null) {
            return getId() + "," + type + "," + getName() + "," + getStatus() +
                    "," + getDescription() + "," + getStartTime() + "," + getDuration() + "," + getEpicId();
        } else {
            return getId() + "," + type + "," + getName() + "," + getStatus() +
                    "," + getDescription() + "," + "," + "," + getEpicId();
        }
    }
}

