package ru.yandex.practicum.kanban.tasks;

import java.time.LocalDateTime;
import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId && type == subtask.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId, type);
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

