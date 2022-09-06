package ru.yandex.practicum.kanban.history;

import ru.yandex.practicum.kanban.tasks.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task);

    List<Task> getHistory();
}
