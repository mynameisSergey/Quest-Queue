package http.handlers;


import com.sun.net.httpserver.HttpExchange;
import http.Enum.EndpointHistoryPriority;
import service.TaskManager;

import java.io.IOException;

public class HandlerHistory extends HandlerTask {
    public HandlerHistory(TaskManager taskManager) {
        super(taskManager);
    }


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        EndpointHistoryPriority endpointTask = getEndpoint(httpExchange.getRequestURI().getPath(), httpExchange.getRequestMethod());
        switch (endpointTask) {
            case GET_HISTORY:
                handleGetHistory(httpExchange);
                break;
            case UNKNOWN:
                break;
        }
    }

    private void handleGetHistory(HttpExchange httpExchange) throws IOException { // История просмотров
        writeResponse(httpExchange, gson.toJson(taskManager.getHistory()), 200);
    }


    private EndpointHistoryPriority getEndpoint(String requestPath, String requestMethod) { //создание эндпоинта истории
        String[] pathParts = requestPath.split("/");

        if (pathParts.length == 3 && pathParts[2].equals("history"))
            return EndpointHistoryPriority.GET_HISTORY;

        return EndpointHistoryPriority.UNKNOWN;
    }

}
