package http.handlers;

import exception.HttpException;
import com.sun.net.httpserver.HttpExchange;
import http.Enum.EndpointSubtask;
import model.Subtask;
import service.TaskManager;

import java.io.IOException;
import java.util.List;

public class HandlerSubtask extends HandlerTask {
    public HandlerSubtask(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        EndpointSubtask endpointTask = getEndpoint(httpExchange, httpExchange.getRequestMethod());
        try {
            switch (endpointTask) {

                case GET_SUBTASKS:
                    handleGetSubtasks(httpExchange);
                    break;
                case GET_SUBTASK_BY_ID:
                    handleGetById(httpExchange);
                    break;
                case POST_SUBTASK:
                    handlePost(httpExchange);
                    break;
                case DELETE_SUBTASK_BY_ID:
                    handleDeleteById(httpExchange);
                    break;
                case DELETE_ALL_SUBTASKS:
                    handleDeleteAll(httpExchange);
                    break;
                case UNKNOWN:
                    writeResponse(httpExchange, "Неизвестный эндпоинт", 404);
                    break;
            }
        } catch (HttpException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleGetSubtasks(HttpExchange httpExchange) throws IOException { // возвращает список Субтасок
        writeResponse(httpExchange, gson.toJson(taskManager.getArraySubtask()), 200);
    }

    @Override
    protected void handleGetById(HttpExchange httpExchange) throws HttpException, IOException { // возвращает субтаску по id
        List<Subtask> listSubtask = taskManager.getArraySubtask();
        String idValue = httpExchange.getRequestURI().getQuery().split("=")[1];

        if (idValue.trim().isEmpty()) {
            writeResponse(httpExchange, "Некорректный идентификатор субтаски", 400);
            return;
        }

        int id = Integer.parseInt(idValue);

        if (listSubtask.stream().noneMatch(subtask -> subtask.getId() == id)) {
            writeResponse(httpExchange, "Субтаска с инедтификатором " + id + " не найдена", 404);
            return;
        }
        try {
            writeResponse(httpExchange, gson.toJson(taskManager.getSubtaskOfId(id)), 200);

        } catch (IOException | RuntimeException e) {
            throw new HttpException("Не удалось сериализовать субтаску", e);
        }
    }

    @Override
    protected void handlePost(HttpExchange httpExchange) throws IOException { // добавление или обновление субтаски
        List<Subtask> listSubtask = taskManager.getArraySubtask();
        String bodyRequest = new String(httpExchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        try {
            Subtask subtask = gson.fromJson(bodyRequest, Subtask.class);
            if (subtask == null) {
                writeResponse(httpExchange, "Субтаска не может быть пустой", 400);
                return;
            }
            if (!listSubtask.contains(subtask)) {
                taskManager.putSubtask(subtask);
                writeResponse(httpExchange, gson.toJson("Субтаска добавлена"), 200);
            } else {
                taskManager.updateSubtask(subtask);
                writeResponse(httpExchange, gson.toJson("Субтаска обновлена"), 200);
            }
        } catch (IOException | RuntimeException e) {
            writeResponse(httpExchange, "Ошибка обработки запроса", 500); // Возвращаем 500 в случае ошибки
        }
    }

    @Override
    protected void handleDeleteById(HttpExchange httpExchange) throws IOException, HttpException { // удаляет субтаску по id
        List<Subtask> listSubtask = taskManager.getArraySubtask();
        String idValue = httpExchange.getRequestURI().getQuery().split("=")[1];

        if (idValue.trim().isEmpty()) {
            writeResponse(httpExchange, "Некорректный идентификатор субтаски", 400);
            return;
        }
        int id = Integer.parseInt(idValue);
        if (listSubtask.stream().noneMatch(subtask -> subtask.getId() == id)) {
            writeResponse(httpExchange, "Субтаска с инедтификатором " + id + " не найдена", 404);
            return;
        }
        try {
            taskManager.removeSubtaskId(id);
            writeResponse(httpExchange, gson.toJson(taskManager.getArraySubtask()), 200);
        } catch (IOException | RuntimeException e) {
            throw new HttpException("Субтаска со следующим id не удалилась" + id, e);
        }
    }

    @Override
    protected void handleDeleteAll(HttpExchange httpExchange) { //удаление всех субтасок
        try {
            List<Subtask> listSubtask = taskManager.getArraySubtask();

            if (listSubtask.isEmpty()) {
                writeResponse(httpExchange, gson.toJson("Нельзя очистить пустой список субтасок."), 400);
                return;
            }
            taskManager.clearMapOfSubtask();
            writeResponse(httpExchange, gson.toJson(taskManager.getArraySubtask()), 200);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Субтаска падает");
        }
    }

    private EndpointSubtask getEndpoint(HttpExchange httpExchange, String requestMethod) { //создание эндпоинта субтасок
        String query = httpExchange.getRequestURI().getQuery();

        if (query == null) { // Путь /subtask/
            if (requestMethod.equals("GET")) {
                return EndpointSubtask.GET_SUBTASKS;
            } else if (requestMethod.equals("POST")) {
                return EndpointSubtask.POST_SUBTASK;
            } else if (requestMethod.equals("DELETE")) {
                return EndpointSubtask.DELETE_ALL_SUBTASKS;
            }
        }

        if (query != null) { // Путь /subtask/{id}
            if (requestMethod.equals("GET")) {
                return EndpointSubtask.GET_SUBTASK_BY_ID;
            } else if (requestMethod.equals("DELETE")) {
                return EndpointSubtask.DELETE_SUBTASK_BY_ID;
            }
        }
        return EndpointSubtask.UNKNOWN;
    }
}
