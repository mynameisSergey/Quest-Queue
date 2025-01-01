package http.handlers;

import exception.*;
import com.sun.net.httpserver.HttpExchange;
import http.Enum.EndpointEpic;
import model.Epic;
import service.TaskManager;

import java.io.IOException;
import java.util.List;

public class HandlerEpic extends HandlerTask {

    public HandlerEpic(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        EndpointEpic endpointTask = getEndpoint(httpExchange, httpExchange.getRequestMethod());
        try {
            switch (endpointTask) {

                case GET_EPICS:
                    handleGetEpics(httpExchange);
                    break;
                case GET_EPIC_BY_ID:
                    handleGetById(httpExchange);
                    break;
                case POST_EPIC:
                    handlePost(httpExchange);
                    break;
                case DELETE_EPIC_BY_ID:
                    handleDeleteById(httpExchange);
                    break;
                case DELETE_ALL_EPICS:
                    handleDeleteAll(httpExchange);
                    break;
                case UNKNOWN:
                    writeResponse(httpExchange, "Неизвестный эндпоинт"+ httpExchange.getRequestURI().getPath().split("/").length, 404);

                    break;
            }
        } catch (HttpException e) {
            writeResponse(httpExchange, e.getMessage(), 500);
        } catch (Exception e) {
            writeResponse(httpExchange, "Внутренняя ошибка сервера", 500);
        } finally {
            httpExchange.close();
        }

    }

    private void handleGetEpics(HttpExchange httpExchange) throws IOException { // возвращает список Эпиков
        writeResponse(httpExchange, gson.toJson(taskManager.getArrayEpic()), 200);
    }

    @Override
    protected void handleGetById(HttpExchange httpExchange) throws HttpException, IOException { // возвращает Эпик по id
        List<Epic> listEpics = taskManager.getArrayEpic();
        String idValue = httpExchange.getRequestURI().getQuery().split("=")[1];


        if (idValue.isEmpty()) {
            writeResponse(httpExchange, "Некорректный идентификатор эпика", 400);
            return;
        }
        int id = Integer.parseInt(idValue);
        if (listEpics.stream().noneMatch(epic -> epic.getId() == id)) {
            writeResponse(httpExchange, "Эпик с инедтификатором " + id + " не найден", 404);
            return;
        }
        try {
            writeResponse(httpExchange, gson.toJson(taskManager.getEpicOfId(id)), 200);

        } catch (IOException | RuntimeException e) {
            throw new HttpException("Не удалось сериализовать Эпик", e);
        }

    }

    @Override
    protected void handlePost(HttpExchange httpExchange) throws IOException { // добавление или обновление эпика
        List<Epic> listEpics = taskManager.getArrayEpic();
        String bodyRequest = new String(httpExchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        try {
            Epic epic = gson.fromJson(bodyRequest, Epic.class);
            if (epic == null)
                writeResponse(httpExchange, "Эпик не может быть пустой", 400);

            if (!listEpics.contains(epic)) {
                taskManager.putEpic(epic);
                writeResponse(httpExchange, gson.toJson("Эпик добавлен"), 200);
            } else {
                taskManager.updateEpic(epic);
                writeResponse(httpExchange, gson.toJson("Эпик обновлен"), 200);
            }
        } catch (IOException | RuntimeException e) {
            writeResponse(httpExchange, "Ошибка обработки запроса", 500); // Возвращаем 500 в случае ошибки
        }
    }

    @Override
    protected void handleDeleteById(HttpExchange httpExchange) throws HttpException, IOException { // удаляет Эпик по id
        List<Epic> listEpics = taskManager.getArrayEpic();
        String idValue = httpExchange.getRequestURI().getQuery().split("=")[1];

        if (idValue.isEmpty()) {
            writeResponse(httpExchange, "Некорректный идентификатор Эпика", 400);
            return;
        }

        int id = Integer.parseInt(idValue);

        if (listEpics.stream().noneMatch(epic -> epic.getId() == id)) {
            writeResponse(httpExchange, "Эпи с инедтификатором " + id + " не найден", 404);
            return;
        }
        try {
            taskManager.removeEpicOfId(id);
            writeResponse(httpExchange, gson.toJson(taskManager.getArrayEpic()), 200);
        } catch (IOException | RuntimeException e) {
            throw new HttpException("Эпик со следующим id не удалился" + id, e);
        }

    }

    @Override
    protected void handleDeleteAll(HttpExchange httpExchange) { //удаление всех Эпиков
        List<Epic> listEpics = taskManager.getArrayEpic();
        try {
            if (listEpics.isEmpty()) {
                writeResponse(httpExchange, gson.toJson("Нельзя очистить пустой список Эпиков."), 400);
                return;
            }
                taskManager.clearMapOfEpic();
                writeResponse(httpExchange, gson.toJson(taskManager.getArrayEpic()), 200);

        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Проблемы с удалением эпиков");
        }
    }
    private EndpointEpic getEndpoint(HttpExchange httpExchange, String requestMethod) { //создание эндпоинта Эпиков
        String query = httpExchange.getRequestURI().getQuery();

        if (query == null) { // Путь /epics/
            if (requestMethod.equals("GET")) {
                return EndpointEpic.GET_EPICS;
            } else if (requestMethod.equals("POST")) {
                return EndpointEpic.POST_EPIC;
            } else if (requestMethod.equals("DELETE")) {
                return EndpointEpic.DELETE_ALL_EPICS;
            }
        }
        if (query != null) { // Путь /epics/{id}
            if (requestMethod.equals("GET")) {
                return EndpointEpic.GET_EPIC_BY_ID;
            } else if (requestMethod.equals("DELETE")) {
                return EndpointEpic.DELETE_EPIC_BY_ID;
            }
        }
        return EndpointEpic.UNKNOWN;
    }
}
