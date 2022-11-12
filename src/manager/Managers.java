package manager;

import history.HistoryManager;
import history.InMemoryHistoryManager;
import server.KVServer;

import java.io.IOException;

public class Managers {

    public static TaskManager getDefault() throws IOException {
        return new HTTPTaskManager("http://localhost:" + KVServer.PORT + "/");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}