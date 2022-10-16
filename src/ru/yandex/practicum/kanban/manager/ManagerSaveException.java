package ru.yandex.practicum.kanban.manager;

import java.io.IOException;

public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException(IOException e) {
        e.getMessage();
    }

    public ManagerSaveException() {

    }
}
