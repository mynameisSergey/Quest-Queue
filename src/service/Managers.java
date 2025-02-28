package service;
import http.manager.HttpTaskManager;
import http.server.KVServer;

import java.io.File;
import java.io.IOException;

public class Managers {

    public static InMemoryTaskManager getDefaultInMemory() {
        return new InMemoryTaskManager();
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getFileBacked(String file) throws IOException {
        return FileBackedTasksManager.loadFromFile(new File(file));
    }

    public static HttpTaskManager getDefault(){
        return new HttpTaskManager("http://localhost:" + KVServer.PORT);
    }

}

