package manager;

import history.HistoryManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private int uid;

    private final HashMap<Integer, Task> tasksHashMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicsHashMap = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasksHashMap = new HashMap<>();

    Comparator<Task> comparator = new Comparator<Task>() {
        @Override
        public int compare(Task o1, Task o2) {
            if ((o1.getStartTime() != null) && (o2.getStartTime() != null)) {
                if (o1.getStartTime().equals(o2.getStartTime())) {
                    return 1;
                } else {
                    return o1.getStartTime().compareTo(o2.getStartTime());
                }

            } else if (o1.getStartTime() != null) {
                return -1;
            } else {
                return 1;
            }
        }
    };

    private Set<Task> prioritizedTasks = new TreeSet<>(comparator);

    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    private HistoryManager historyManager = Managers.getDefaultHistory();

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public boolean isCrossing(Task newTask) {
        if (newTask.getStartTime() != null) {
            for (Task prioritizedTask : getPrioritizedTasks()) {
                if (!(newTask.getEndTime().isBefore(prioritizedTask.getStartTime()) ||
                        (newTask.getEndTime().isEqual(prioritizedTask.getStartTime())) ||
                        (newTask.getStartTime().isEqual(prioritizedTask.getEndTime())) ||
                        (newTask.getStartTime().isAfter(prioritizedTask.getEndTime())))) {
                    return true;
                }
            }
        }
        return false;
    }

    // Для тасков
    @Override
    public int createTask(Task task) {  //создание Таска
        if (!isCrossing(task)) {
            final int id;
            if (task.getId() != 0) {
                id = task.getId();
                uid = id;
            } else {
                id = ++uid;
                task.setId(id);
            }
            tasksHashMap.put(id, task);
            prioritizedTasks.add(task);
            return id;
        } else {
            return 0;
        }
    }

    @Override
    public ArrayList<Task> getTaskList() { //список всех задач
        return new ArrayList<>(tasksHashMap.values());
    }

    @Override
    public void removeAllTasks() { //удаление всех задач
        for (Integer id : tasksHashMap.keySet()) {
            removeTask(id);
        }
    }

    @Override
    public Task getTask(int id) { //получение по идентификатору
        Task task = tasksHashMap.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public void updateTask(Task task) { //обновление задачи
        if (!isCrossing(task)) {
            Task updateTask = tasksHashMap.get(task.getId());
            tasksHashMap.put(task.getId(), task);
            prioritizedTasks.remove(updateTask);
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void removeTask(int id) { //удаление по идентификатору
        Task deleteTask = tasksHashMap.get(id);
        tasksHashMap.remove(id);
        historyManager.remove(id);
        prioritizedTasks.remove(deleteTask);
    }

    // Для Эпиков
    @Override
    public int createEpic(Epic epic) { //создание Эпика
        final int id;
        if (epic.getId() != 0) {
            id = epic.getId();
            uid = id;
        } else {
            id = ++uid;
            epic.setId(id);
        }
        epicsHashMap.put(id, epic);
        return id;
    }

    @Override
    public ArrayList<Epic> getEpicList() { //список всех эпиков
        return new ArrayList<>(epicsHashMap.values());
    }

    @Override
    public void removeAllEpics() { //удаление всех эпиков
        for (Integer id : epicsHashMap.keySet()) {
            removeEpic(id);
        }
        //удаление всех эпиков ведет к удалению всех сабтасков
    }

    @Override
    public Epic getEpic(int id) { //получение по идентификатору
        Epic epic = epicsHashMap.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public void updateEpic(Epic epic) { //обновление эпика
        epic.setStatus(getEpicStatus(epic.getId())); //проверка и обновление статуса эпика при его обновлении
        epicsHashMap.put(epic.getId(), epic);
    }

    @Override
    public void removeEpic(int id) { //удаление по идентификатору
        ArrayList<Integer> subtasksId = new ArrayList<>(epicsHashMap.get(id).getSubtaskIds());
        epicsHashMap.remove(id);
        for (Integer subtaskId : subtasksId) {
            subtasksHashMap.remove(subtaskId);
            historyManager.remove(subtaskId);
            //удаление всех сабтасков удаленного эпика
        }
        historyManager.remove(id);
    }

    @Override
    public ArrayList<Subtask> getEpicSubtaskList(int id) {
        //получение списка всех подзадач определенного эпика
        ArrayList<Subtask> thisEpicSubtaskList = new ArrayList<>();
        for (Integer subtaskId : epicsHashMap.get(id).getSubtaskIds()) {
            thisEpicSubtaskList.add(subtasksHashMap.get(subtaskId));
        }
        return thisEpicSubtaskList;
    }

    public Status getEpicStatus(int id) { //проверка и возврат статуса Эпика
        int n = 1; //проверка на условие, аналог опреатора И
        int m = 1;
        Status status;
        Epic epic = epicsHashMap.get(id);
        if (epic.getSubtaskIds() == null) {
            status = Status.NEW;
        } else {
            for (Integer subtaskId : epic.getSubtaskIds()) {
                if (subtasksHashMap.get(subtaskId).getStatus().equals(Status.NEW)) {
                    n *= 1;
                } else {
                    n *= 0;
                }
                if (subtasksHashMap.get(subtaskId).getStatus().equals(Status.DONE)) {
                    m *= 1;
                } else {
                    m *= 0;
                }
            }
            if (n == 1) {
                status = Status.NEW;
            } else if (m == 1) {
                status = Status.DONE;
            } else {
                status = Status.IN_PROGRESS;
            }
        }
        return status;
    }

    // Для Сабтасков
    @Override
    public int createSubtask(Subtask subtask) { //создание Сабтаска
        if (!isCrossing(subtask)) {
            final int id;
            if (subtask.getId() != 0) {
                id = subtask.getId();
                uid = id;
            } else {
                id = ++uid;
                subtask.setId(id);
            }
            subtasksHashMap.put(id, subtask);
            prioritizedTasks.add(subtask);
            Epic epic = epicsHashMap.get(subtask.getEpicId());

            try {
                epic.getSubtaskIds().add(id);
                epic.setSubtaskList(getEpicSubtaskList(epic.getId()));
                epic.setStatus(getEpicStatus(epic.getId()));
            } catch (NullPointerException e) {
                System.out.println("Внимание: epicID у сабтаска(id = " + id + ") не соответствует ID эпика");
            }
            return id;
        } else {
            return 0;
        }

    }

    @Override
    public ArrayList<Subtask> getSubtaskList() { //список всех задач
        return new ArrayList<>(subtasksHashMap.values());
    }

    @Override
    public void removeAllSubtasks() {
        for (Integer id : subtasksHashMap.keySet()) {
            removeSubtask(id);
        }
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasksHashMap.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (!isCrossing(subtask)) {
            Subtask updateSubtask = subtasksHashMap.get(subtask.getId());
            subtasksHashMap.put(subtask.getId(), subtask);
            prioritizedTasks.remove(updateSubtask);
            prioritizedTasks.add(subtask);

            Epic epic = epicsHashMap.get(subtask.getEpicId());
            epic.setSubtaskList(getEpicSubtaskList(epic.getId()));
            epic.setStatus(getEpicStatus(epic.getId()));
            //проверка и изменение статуса при обновлении сабтаска
        }
    }

    @Override
    public void removeSubtask(int id) {
        Subtask deleteSubtask = subtasksHashMap.get(id);
        int epicId = subtasksHashMap.get(id).getEpicId();
        subtasksHashMap.remove(id);
        prioritizedTasks.remove(deleteSubtask);

        Epic epic = epicsHashMap.get(epicId);
        epic.getSubtaskIds().remove(Integer.valueOf(id));
        epic.setStatus(getEpicStatus(epic.getId()));
        epic.setSubtaskList(getEpicSubtaskList(epic.getId()));
        //проверка и изменение статуса при удалении сабтаска
        historyManager.remove(id);
    }

    @Override
    public void save(String path) {
    }
}

