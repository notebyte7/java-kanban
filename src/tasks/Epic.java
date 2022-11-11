package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Epic extends Task {

    private final TaskType type = TaskType.EPIC;

    @Override
    public TaskType getType() {
        return type;
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

    @Override
    public LocalDateTime getStartTime() {
        if (subtaskList != null) {
            Optional<LocalDateTime> startTime = subtaskList.stream()
                    .filter(Objects::nonNull)
                    .map(Subtask::getStartTime)
                    .filter(Objects::nonNull)
                    .min(LocalDateTime::compareTo);
            if (startTime.isPresent()) {
                setStartTime(startTime.get());
                return startTime.get();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public int getDuration() {
        if (!subtaskList.isEmpty()) {
            int duration = subtaskList.stream()
                    .filter(Objects::nonNull)
                    .map(Subtask::getDuration)
                    .mapToInt(Integer::intValue)
                    .sum();
            setDuration(duration);
            return duration;
        } else {
            return 0;
        }
    }

    @Override
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Epic epic = (Epic) o;
        return Objects.equals(subtaskIds, epic.subtaskIds) &&
                Objects.equals(subtaskList, epic.subtaskList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIds, subtaskList);
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