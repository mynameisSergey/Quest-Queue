package http.server;

import com.sun.net.httpserver.HttpServer;
import http.handlers.*;
import http.manager.HttpTaskManager;
import service.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    public final HttpServer httpServer;
    private static final Integer PORT = 8080;

    public HttpTaskServer() throws IOException {
        HttpTaskManager taskManager = Managers.getDefault();
        this.httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/task/", new HandlerTask(taskManager));
        httpServer.createContext("/tasks/subtask/", new HandlerSubtask(taskManager));
        httpServer.createContext("/tasks/epic/", new HandlerEpic(taskManager));
        httpServer.createContext("/tasks/subtask/epic/", new HandlerSubtaskEpic(taskManager));
        httpServer.createContext("/tasks/history/", new HandlerHistory(taskManager));
        httpServer.createContext("/tasks/", new HandlerPriority(taskManager));
        System.out.println("Сервер запущен:" + PORT);

    }

    public void startServer() {
        httpServer.start();
    }

    public void stopServer() {
        httpServer.stop(1);
    }

}

