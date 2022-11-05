package ru.yandex.practicum.kanban.tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Epic extends Task {

    private final TaskType type = TaskType.EPIC;

    public TaskType getType() {
        return TaskType.EPIC;
    }

    private final List<Integer> subtaskIds = new ArrayList<>();

    private List<Subtask> subtaskList = new ArrayList<>();

    public void setSubtaskList(List<Subtask> subtaskList) {
        this.subtaskList = subtaskList;
    }

    public List<Subtask> getSubtaskList() {
        return subtaskList;
    }

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public Epic(String name, String description, int id, Status status) {
        super(name, description, id, status);
    }

    public Epic(String name, String description, int id, Status status, LocalDateTime startTime, int duration) {
        super(name, description, id, status, startTime, duration);

    }

    public final List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public LocalDateTime getStartTime() {
        Optional<LocalDateTime> startTime = subtaskList.stream()
                .filter(Objects::nonNull)
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo);
        if (startTime.isPresent()) {
            return startTime.get();
        } else {
            return null;
        }
    }

    public int getDuration() {
        if (!subtaskList.isEmpty()) {
            return subtaskList.stream()
                    .filter(Objects::nonNull)
                    .map(Subtask::getDuration)
                    .mapToInt(Integer::intValue)
                    .sum();
        } else {
            return 0;
        }
    }

    public LocalDateTime getEndTime() {
        Optional<LocalDateTime> endTime = subtaskList.stream()
                .filter(Objects::nonNull)
                .map(Subtask::getEndTime)
                .max(LocalDateTime::compareTo);
        if (endTime.isPresent()) {
            return endTime.get();
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        if (getStartTime() != null) {
            return getId() + "," + type + "," + getName() + "," + getStatus() +
                    "," + getDescription() + "," + getStartTime() + "," + getDuration() + ",";
        } else {
            return getId() + "," + type + "," + getName() + "," + getStatus() +
                    "," + getDescription() + "," + "," + ",";
        }
    }
}