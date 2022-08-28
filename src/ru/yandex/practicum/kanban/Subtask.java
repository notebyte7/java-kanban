package ru.yandex.practicum.kanban;

import java.util.Objects;

class Subtask extends Task {
    protected int epicId;

    public Subtask(String name, String description, int id, String status, int epicId) {
        super(name, description, id, status);
        this.epicId = epicId;
    }

    @Override
    public boolean equals (Object obj){
        if (this == obj) return true; // проверяем адреса объектов
        if (obj == null) return false; // проверяем ссылку на null
        if (this.getClass() != obj.getClass()) return false; // сравниваем классы
        Subtask subtask = (Subtask) obj; // открываем доступ к полям другого объекта
        return Objects.equals(name, subtask.name) && // проверяем все поля
                Objects.equals(description, subtask.description) &&
                (id == subtask.id) &&
                Objects.equals(status, subtask.status) &&
                (epicId == subtask.epicId); // примитивы сравниваем через ==
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (name != null) {
            hash +=  name.hashCode();
        }
        hash *=  31;
        if (description != null) {
            hash += description.hashCode();
        }
        hash *=  13;
        hash += id;
        if (status != null) {
            hash +=  status.hashCode();
        }
        hash *=  17;
        hash += epicId;
        return hash;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", epicId=" + epicId +
                '}';
    }
}

