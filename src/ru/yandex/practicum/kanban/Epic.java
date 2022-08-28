package ru.yandex.practicum.kanban;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    protected ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(String name, String description, int id, String status, ArrayList subtaskIds) {
        super(name, description, id, status);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // проверяем адреса объектов
        if (obj == null) return false; // проверяем ссылку на null
        if (this.getClass() != obj.getClass()) return false; // сравниваем классы
        Epic epic = (Epic) obj; // открываем доступ к полям другого объекта
        return Objects.equals(name, epic.name) && // проверяем все поля
                Objects.equals(description, epic.description) &&
                (id == epic.id) &&
                Objects.equals(status, epic.status) &&
                Objects.equals(subtaskIds, epic.subtaskIds); // примитивы сравниваем через ==
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (name != null) {
            hash += name.hashCode();
        }
        hash *= 31;
        if (description != null) {
            hash += description.hashCode();
        }
        hash *= 17;
        hash += id;
        if (status != null) {
            hash += status.hashCode();
        }
        hash *= 13;
        if (subtaskIds != null) {
            hash += subtaskIds.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        return "Epic{" +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", subtaskIds=" + subtaskIds + '\'' +
                '}';
    }
}