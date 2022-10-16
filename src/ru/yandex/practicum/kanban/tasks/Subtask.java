package ru.yandex.practicum.kanban.tasks;

public class Subtask extends Task {
    private final int epicId;
    private final TaskType type = TaskType.SUBTASK;

    public TaskType getType() {
        return type;
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

    @Override
    public String toString() {
        return getId() + "," + type + "," + getName() + "," + getStatus() +
                "," + getDescription() + "," + getEpicId();
    }
}

