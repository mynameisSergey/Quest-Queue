package http.handlers;

import adapters.InstantAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.HttpException;
import http.Enum.EndpointTask;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

public class HandlerTask implements HttpHandler {

    protected static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    protected static final Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();
    protected final TaskManager taskManager;

    public HandlerTask(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        EndpointTask endpointTask = getEndpoint(httpExchange, httpExchange.getRequestMethod());
        try {
            switch (endpointTask) {
                case GET_TASKS:
                    handleGetTasks(httpExchange);
                    break;
                case GET_TASK_BY_ID:
                    handleGetById(httpExchange);
                    break;
                case POST_TASK:
                    handlePost(httpExchange);
                    break;
                case DELETE_TASK_BY_ID:
                    handleDeleteById(httpExchange);
                    break;
                case DELETE_ALL_TASKS:
                    handleDeleteAll(httpExchange);
                    break;
                case UNKNOWN:
                    writeResponse(httpExchange, "Неизвестный эндпоинт" + httpExchange.getRequestURI().getPath().split("/").length, 404);
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

    private void handleGetTasks(HttpExchange httpExchange) throws IOException { // возвращает список Тасок
        writeResponse(httpExchange, gson.toJson(taskManager.getArrayTask()), 200);
    }

    protected void handleGetById(HttpExchange httpExchange) throws HttpException, IOException { // возвращает задачу по id
        List<Task> listTasks = taskManager.getArrayTask();
        String idValue = httpExchange.getRequestURI().getQuery().split("=")[1];

        if (idValue.isEmpty()) {
            writeResponse(httpExchange, "Некорректный идентификатор задачи", 400);
            return;
        }
        int id = Integer.parseInt(idValue);
        if (listTasks.stream().noneMatch(task -> task.getId() == id)) {
            writeResponse(httpExchange, "Задача с инедтификатором " + id + " не найдена", 404);
            return;
        }
        try {
            writeResponse(httpExchange, gson.toJson(taskManager.getTaskOdId(id)), 200);

        } catch (IOException | RuntimeException e) {
            throw new HttpException("Не удалось сериализовать задачу", e);
        }
    }

    protected void handlePost(HttpExchange httpExchange) throws IOException {
        String bodyRequest;

        try (InputStream inputStream = httpExchange.getRequestBody()) {
            bodyRequest = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        } catch (IOException e) {
            writeResponse(httpExchange, "Ошибка при чтении тела запроса", 400); // Возвращаем 400 при ошибке чтения
            return;
        }
        try {
            Task task = gson.fromJson(bodyRequest, Task.class);
            if (taskManager.getArrayTask().contains(task)) {
                taskManager.updateTask(task);
                writeResponse(httpExchange, gson.toJson("Задача обновлена"), 200); // Код 204 для обновления
            } else {
                System.out.println("Создаем задачу.");
                taskManager.putTask(task);
                writeResponse(httpExchange, gson.toJson("Задача добавлена"), 200); // Код 201 для создания
            }
        } catch (JsonSyntaxException e) {
            writeResponse(httpExchange, "Ошибка синтаксиса JSON", 400); // Возвращаем 400 при ошибке парсинга
            return;
        }
    }


    protected void handleDeleteById(HttpExchange httpExchange) throws HttpException, IOException, HttpException { // удаляет задачу по id
        List<Task> listTask = taskManager.getArrayTask();
        String idValue = httpExchange.getRequestURI().getQuery().split("=")[1];

        if (idValue.isEmpty()) {
            writeResponse(httpExchange, "Некорректный идентификатор задачи", 400);
            return;
        }
        int id = Integer.parseInt(idValue);
        if (listTask.stream().noneMatch(task -> task.getId() == id)) {
            writeResponse(httpExchange, "Задача с инедтификатором " + id + " не найдена", 404);
            return;
        }
        try {
            taskManager.removeTask(id);
            writeResponse(httpExchange, gson.toJson(taskManager.getArrayTask()), 200);
        } catch (IOException | RuntimeException e) {
            throw new HttpException("Задача со следующим id не удалилась" + id, e);
        }
    }

    protected void handleDeleteAll(HttpExchange httpExchange) throws IOException { // удаление всех задач
        List<Task> listTask = taskManager.getArrayTask();

        if (listTask.isEmpty()) {
            writeResponse(httpExchange, gson.toJson("Нельзя очистить пустой список задач."), 400);
            return;
        }
        try {
            taskManager.clearMapOfTask();
            writeResponse(httpExchange, gson.toJson(taskManager.getArrayTask()), 200);
        } catch (Exception e) {
            // Логируем ошибку для отладки
            e.printStackTrace();
            writeResponse(httpExchange, gson.toJson("Произошла ошибка при удалении задач: " + e.getMessage()), 500);
        }
    }

    private EndpointTask getEndpoint(HttpExchange httpExchange, String requestMethod) {
        String query = httpExchange.getRequestURI().getQuery();

        if (query == null) { // Путь /tasks
            if (requestMethod.equals("GET")) {
                return EndpointTask.GET_TASKS;
            } else if (requestMethod.equals("POST")) {
                return EndpointTask.POST_TASK;
            } else if (requestMethod.equals("DELETE")) {
                return EndpointTask.DELETE_ALL_TASKS;
            }
        }

        if (query != null) { // Путь /tasks/{id}
            if (requestMethod.equals("GET")) {
                return EndpointTask.GET_TASK_BY_ID;
            } else if (requestMethod.equals("DELETE")) {
                return EndpointTask.DELETE_TASK_BY_ID;
            }
        }

        return EndpointTask.UNKNOWN;
    }

    protected void writeResponse(HttpExchange httpExchange, String response, int responseCode) throws IOException {
        if (response == null || response.isBlank() || response.equals("null")) {
            httpExchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = response.getBytes(DEFAULT_CHARSET);

            // Устанавливаем заголовок Content-Type
            httpExchange.getResponseHeaders().set("Content-Type", "application/json; charset=" + DEFAULT_CHARSET);

            // Отправляем заголовки с длиной ответа
            httpExchange.sendResponseHeaders(responseCode, bytes.length);

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }

}
