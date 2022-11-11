package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import history.HistoryManager;
import history.InMemoryHistoryManager;

import java.io.IOException;

public class Managers {

    public static TaskManager getDefault() throws IOException {
        return new HTTPTaskManager("http://localhost:8078/");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}