package ru.yandex.practicum.kanban.history;

import ru.yandex.practicum.kanban.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList<Task> historyList = new CustomLinkedList<>();

    @Override
    public void add(Task task) {
        int id = task.getId();
        Node<Task> node;
        if (historyList.getTasksHistory().containsKey(id)) {
            node = historyList.getTasksHistory().get(id);
            historyList.removeNode(node);
            historyList.linkLast(task);
        } else {
            historyList.linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        if (historyList.getTasksHistory().containsKey(id)) {
            Node<Task> node;
            node = historyList.getTasksHistory().get(id);
            historyList.removeNode(node);
        }
    }

    public ArrayList<Task> getHistory() {
        return new ArrayList<>(historyList.getTasks());
    }

    public class CustomLinkedList<T> {
        private final Map<Integer, Node<Task>> tasksHistory = new HashMap<>();

        private Map<Integer, Node<Task>> getTasksHistory() {
            return tasksHistory;
        }

        private Node<Task> head;
        private Node<Task> tail;
        private int size = 0;

        private void linkLast(Task element) {
            final Node<Task> oldTail = tail;
            final Node<Task> newNode = new Node<>(oldTail, element, null);
            tail = newNode;
            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.next = newNode;
            }
            tasksHistory.put(element.getId(), newNode);
            size++;
        }

        public List<Task> getTasks() {
            ArrayList<Task> history = new ArrayList<>();
            Node<Task> currentNode = head;
            while (currentNode != null) {
                history.add(currentNode.data);
                currentNode = currentNode.next;
            }
            return history;
        }

        private void removeNode(Node<Task> node) {
            switch (size) {
                case 0:
                    break;
                case 1:
                    head = null;
                    tail = null;
                    break;
                default:
                    if (node.prev == null) {
                        head = node.next;
                        node.next.prev = null;
                    } else if (node.next == null) {
                        node.prev.next = null;
                        tail = node.prev;
                    } else {
                        node.prev.next = node.next;
                        node.next.prev = node.prev;
                        break;
                    }
            }
            tasksHistory.remove(node.data.getId());
        }
    }


}
