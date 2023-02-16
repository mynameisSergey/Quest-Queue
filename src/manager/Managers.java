package manager;
import http.HTTPTaskManager;
import http.KVServer;

import java.io.IOException;

public class Managers {

    public static TaskManager getInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }
    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
    public static HTTPTaskManager getDefault(HistoryManager historyManager) throws IOException, InterruptedException {
        return new HTTPTaskManager(historyManager, "http://localhost:" + KVServer.PORT);
    }

}
