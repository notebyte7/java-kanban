package ru.yandex.practicum.kanban;

import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected String status;


    public Task(String name, String description, int id, String status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    @Override
    public boolean equals (Object obj){
        if (this == obj) return true; // проверяем адреса объектов
        if (obj == null) return false; // проверяем ссылку на null
        if (this.getClass() != obj.getClass()) return false; // сравниваем классы
        Task task = (Task) obj; // открываем доступ к полям другого объекта
        return Objects.equals(name, task.name) && // проверяем все поля
                Objects.equals(description, task.description) &&
                (id == task.id) &&
                Objects.equals(status, task.status); // примитивы сравниваем через ==
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
        return hash;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}

