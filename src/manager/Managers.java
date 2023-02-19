package manager;

import manager.http.KVServer;

import java.io.File;
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
    public static TaskManager getFileBacked(String file) {
        return FileBackedTasksManager.loadFromFile(new File(file));
    }
}
