package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import http.Enum.EndpointHistoryPriority;
import http.manager.HttpTaskManager;

import java.io.IOException;

public class HandlerPriority extends HandlerTask{
    public HandlerPriority(HttpTaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        EndpointHistoryPriority endpointTask = getEndpoint(httpExchange.getRequestURI().getPath(), httpExchange.getRequestMethod());
        switch (endpointTask) {
            case GET_PRIORITY:
                handleGetPriority(httpExchange);
                break;
            case UNKNOWN:
                break;
        }
    }


    private void handleGetPriority(HttpExchange httpExchange) throws IOException { //приорити Таски
       writeResponse(httpExchange, gson.toJson(taskManager.getPrioritizedTasks()), 200);
    }

    private EndpointHistoryPriority getEndpoint(String requestPath, String requestMethod) { //создание эндпоинта приорити
        String[] pathParts = requestPath.split("/");

        if (pathParts.length == 3 && pathParts[1].equals("tasks"))
            if(requestMethod.equals("GET"))
            return EndpointHistoryPriority.GET_PRIORITY;

        return EndpointHistoryPriority.UNKNOWN;
    }


}
