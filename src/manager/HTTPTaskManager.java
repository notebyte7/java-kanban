package manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import server.KVTaskClient;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HTTPTaskManager extends FileBackedTasksManager {
    private final String url;
    private final Gson gson;
    private KVTaskClient kvTaskClient;

    public HTTPTaskManager(String url) throws IOException {
        this.url = url;
        gson = new Gson();
    }

    @Override
    public void save() {
        kvTaskClient = new KVTaskClient(url);
        String jsonTasks = gson.toJson(getTaskList());
        kvTaskClient.put("tasks", jsonTasks);

        String jsonEpics = gson.toJson(getEpicList());
        kvTaskClient.put("epics", jsonEpics);

        String jsonSubtasks = gson.toJson(getSubtaskList());
        kvTaskClient.put("subtasks", jsonSubtasks);

        ArrayList<Integer> history = new ArrayList<>();
        for (Task task : getHistory()) {
            history.add(task.getId());
        }
        String jsonHistory = gson.toJson(history);
        kvTaskClient.put("history", jsonHistory);
    }

    @Override
    public HTTPTaskManager load() throws CrossingTaskException, IOException {
        kvTaskClient = new KVTaskClient(url);
        String jsonTasks = kvTaskClient.load("tasks");
        String jsonSubtasks = kvTaskClient.load("subtasks");
        String jsonEpics = kvTaskClient.load("epics");
        String jsonHistory = kvTaskClient.load("history");


        Type type = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> tasks = gson.fromJson(jsonTasks, type);

        type = new TypeToken<ArrayList<Subtask>>() {
        }.getType();
        List<Subtask> subtasks = gson.fromJson(jsonSubtasks, type);
        type = new TypeToken<ArrayList<Epic>>() {
        }.getType();
        List<Epic> epics = gson.fromJson(jsonEpics, type);

        ArrayList<Integer> history = gson.fromJson(jsonHistory, new TypeToken<ArrayList<Integer>>() {
        }.getType());

        if (tasks != null) {
            for (Task task : tasks) {
                createTask(task);
            }
        }
        if (epics != null) {
            for (Epic epic : epics) {
                epic.getSubtaskIds().clear();
                epic.getSubtaskList().clear();
                createEpic(epic);
            }
        }
        if (subtasks != null) {
            for (Subtask subtask : subtasks) {
                createSubtask(subtask);
            }
        }
        if (history != null) {
            for (Integer id : history) {
                if (getTaskList().stream().anyMatch(task -> task.getId() == id)) {
                    getTask(id);
                } else if (getEpicList().stream().anyMatch(epic -> epic.getId() == id)) {
                    getEpic(id);
                } else if (getSubtaskList().stream().anyMatch(subtask -> subtask.getId() == id)) {
                    getSubtask(id);
                }
            }
        }
        return null;
    }
}