package manager.http;

import com.sun.net.httpserver.HttpServer;
import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import manager.http.handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final HttpServer httpServer;
    private static final int PORT = 8080;

    public HttpTaskServer() throws IOException, InterruptedException {
        HistoryManager historyManager = Managers.getDefaultHistory();
        TaskManager taskManager = Managers.getDefault(historyManager);
        this.httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/task/Task/", new TaskHandler(taskManager));
        httpServer.createContext("/task/Epic/", new EpicHandler(taskManager));
        httpServer.createContext("/task/Subtask/", new SubtaskHandler(taskManager));
        httpServer.createContext("/task/Subtask/Epic/", new SubtaskByEpicHandler(taskManager));
        httpServer.createContext("/task/history/", new HistoryHandler(taskManager));
        httpServer.createContext("/task/", new TasksHandler(taskManager));
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(1);
    }

}
