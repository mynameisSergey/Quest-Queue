package http.handlers;

import exception.HttpException;
import com.sun.net.httpserver.HttpExchange;
import model.Epic;
import service.TaskManager;

import java.io.IOException;
import java.util.List;

public class HandlerSubtaskEpic extends HandlerTask {
    public HandlerSubtaskEpic(TaskManager taskManager) {
        super(taskManager);
    }


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String[] pathParts = httpExchange.getRequestURI().getPath().split("/");
        if (pathParts.length == 4 && pathParts[3].equals("epic")) {
            try {
                handleGetById(httpExchange);
            } catch (HttpException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }

        }
    }

    protected void handleGetById(HttpExchange httpExchange) throws HttpException, IOException { // возвращает задачу по id
        String idValue = httpExchange.getRequestURI().getQuery().split("=")[1];
        if (idValue.trim().isEmpty()) {
            writeResponse(httpExchange, "Некорректный идентификатор Эпика", 400);
            return;
        }
        List<Epic> listEpics = taskManager.getArrayEpic();
        int id = Integer.parseInt(idValue);

        if (listEpics.stream().noneMatch(epic -> epic.getId() == id)) {
            writeResponse(httpExchange, "Эпик с инедтификатором " + id + " не найден", 404);
            return;
        }
        try {
            writeResponse(httpExchange, gson.toJson(taskManager.getArraySubtaskOfId(id)), 200);

        } catch (IOException | RuntimeException e) {
            throw new HttpException("Не удалось сериализовать список Субтасок", e);
        }
    }

}

