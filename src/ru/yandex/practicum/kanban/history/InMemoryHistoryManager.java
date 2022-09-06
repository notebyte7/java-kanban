package ru.yandex.practicum.kanban.history;

import ru.yandex.practicum.kanban.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    //История просмотров
    private List<Task> history = new ArrayList<>();

    public void add(Task task) {
        while (history.size() >= 10) {
            history.remove(0);
        }
        history.add(task);
    }

    public List<Task> getHistory() {
        return history;
    }
}
