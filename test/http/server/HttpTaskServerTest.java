package http.server;

import adapters.InstantAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TasksType;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;

import static model.StatusTasks.NEW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpTaskServerTest {
    private static KVServer kvServer;
    private static HttpTaskServer taskServer;
    private static Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();
    private static final String TASK_BASE_URL = "http://localhost:8080/tasks/task/";
    private static final String EPIC_BASE_URL = "http://localhost:8080/tasks/epic/";
    private static final String SUBTASK_BASE_URL = "http://localhost:8080/tasks/subtask/";


    @BeforeAll
    static void startServer() {
        try {
            kvServer = new KVServer();
            kvServer.start();
            taskServer = new HttpTaskServer();
            taskServer.startServer();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @AfterAll
    static void stopServer() {
        kvServer.stopServer();
        taskServer.stopServer();
    }

    @BeforeEach
    void resetServer() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(TASK_BASE_URL);
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
            client.send(request, HttpResponse.BodyHandlers.ofString());

            url = URI.create(EPIC_BASE_URL);
            request = HttpRequest.newBuilder().uri(url).DELETE().build();
            client.send(request, HttpResponse.BodyHandlers.ofString());

            url = URI.create(SUBTASK_BASE_URL);
            request = HttpRequest.newBuilder().uri(url).DELETE().build();
            client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @DisplayName("GIVEN a new Task " +
            "WHEN a new Task is created " +
            "THEN number of saved task is 1")
    @Test
    void test_1_shouldGetTask() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(TASK_BASE_URL);
        Task task = new Task(61, TasksType.TASK, "Тест", "Тестовый Task для всех", NEW, Instant.now(), 0);

        // Создаем задачу
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();

        try {
            HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, postResponse.statusCode(), "Создание задачи не удалось."); // Проверяем статус создания

            // Получаем задачи
            HttpRequest getRequest = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Получение задач не удалось.");

            JsonArray arrayTasks = JsonParser.parseString(response.body()).getAsJsonArray();
            assertEquals(1, arrayTasks.size(), "Количество задач не совпадает.");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            fail("Тест завершился с ошибкой: " + e.getMessage()); // Завершаем тест с ошибкой
        }
    }

    @DisplayName("GIVEN a new Epic " +
            "WHEN a new Epic is created " +
            "THEN number of saved epic is 1")
    @Test
    void test_2_shouldGetEpic() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(TASK_BASE_URL);
        Epic epic = new Epic(62, TasksType.EPIC, "Тест", "Тестовый Task для всех", NEW, Instant.now(), 0);

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();

        try {
            HttpResponse<String> response = client.send(postRequest, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode(), "Создание задачи не удалось."); // Проверяем статус создания

            // Получаем задачи
            HttpRequest getRequest = HttpRequest.newBuilder().uri(url).GET().build();
            response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Получение задач не удалось.");
            JsonArray arrayEpics = JsonParser.parseString(response.body()).getAsJsonArray();
            assertEquals(1, arrayEpics.size(), "Количество задач не совпадает.");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            fail("Тест завершился с ошибкой: " + e.getMessage()); // Завершаем тест с ошибкой
        }
    }

    @DisplayName("GIVEN a new Subtask " +
            "WHEN a new Sybtask is created " +
            "THEN number of saved subtask is 1")
    @Test
    void test_3_shouldGetSubtask() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_BASE_URL);
        Epic epic1 = new Epic(62, TasksType.EPIC, "Тест", "Тестовый Epic для всех", NEW, Instant.now(), 0);

        // Создаем Epic
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic1)))
                .build();

        try {
            HttpResponse<String> response = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
            url = URI.create(SUBTASK_BASE_URL);
            Subtask subtask1 = new Subtask(63, TasksType.SUBTASK, "Тест", "Тестовый Subtask для всех", NEW, Instant.now(), 0, 62);

            // Создаем Subtask
            postRequest = HttpRequest.newBuilder()
                    .uri(url)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask1)))
                    .build();

            response = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Создание или обновление Subtask не удалось."); // Проверяем статус создания

            // Получаем Subtask
            HttpRequest getRequest = HttpRequest.newBuilder().uri(url).GET().build();
            response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Получение Subtask не удалось.");

            JsonArray arraySubtasks = JsonParser.parseString(response.body()).getAsJsonArray();
            assertEquals(1, arraySubtasks.size(), "Количество задач не совпадает.");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            fail("Тест завершился с ошибкой: " + e.getMessage()); // Завершаем тест с ошибкой
        }
    }


    @DisplayName("GIVEN a new Task " +
            "WHEN a new Task is created " +
            "THEN number of saved task is 1")
    @Test
    void test_4_shouldGetTaskById() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(TASK_BASE_URL);
        Task task = new Task(61, TasksType.TASK, "Тест", "Тестовый Task для всех", NEW, Instant.now(), 0);

        // Создаем задачу
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();

        try {
            HttpResponse<String> response = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Создание или обновление задачи не удалось."); // Проверяем статус создания

            // Получаем задачи
            HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create(url + "?id=61")).GET().build();
            response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Получение задач не удалось.");
            Task task2 = gson.fromJson(response.body(), Task.class);
            assertEquals(task, task2, "Задачи не совпадают.");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            fail("Тест завершился с ошибкой: " + e.getMessage()); // Завершаем тест с ошибкой
        }
    }

    @DisplayName("GIVEN a new Epic " +
            "WHEN a new Epic is created " +
            "THEN number of saved Epic is 1")
    @Test
    void test_5_shouldGetEpicById() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_BASE_URL);
        Epic epic1 = new Epic(62, TasksType.EPIC, "Тест", "Тестовый Epic для всех", NEW, Instant.now(), 0);

        // Создаем Epic
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic1)))
                .build();

        try {
            HttpResponse<String> response = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Создание или обновление Epic не удалось."); // Проверяем статус создания

            // Получаем Epic
            HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create(url + "?id=62")).GET().build();
            response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Получение Epic не удалось.");
            Epic epic2 = gson.fromJson(response.body(), Epic.class);
            assertEquals(epic1, epic2, "Epics не совпадают.");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            fail("Тест завершился с ошибкой: " + e.getMessage()); // Завершаем тест с ошибкой
        }
    }

    @DisplayName("GIVEN a new Epic, Subtask " +
            "WHEN a new Epic, Subtask is created " +
            "THEN number of saved Subtask is 1")
    @Test
    void test_6_shouldGetSubtaskById() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_BASE_URL);
        Epic epic1 = new Epic(62, TasksType.EPIC, "Тест", "Тестовый Epic для всех", NEW, Instant.now(), 0);

        // Создаем Epic
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic1)))
                .build();

        try {
            HttpResponse<String> response = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
            url = URI.create(SUBTASK_BASE_URL);
            Subtask subtask1 = new Subtask(63, TasksType.SUBTASK, "Тест", "Тестовый Subtask для всех", NEW, Instant.now(), 0, 62);

            // Создаем Epic
            postRequest = HttpRequest.newBuilder()
                    .uri(url)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask1)))
                    .build();

            response = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Создание или обновление Subtask не удалось."); // Проверяем статус создания

            // Получаем Subtask
            HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create(url + "?id=63")).GET().build();
            response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Получение Subtask не удалось.");

            Subtask subtask2 = gson.fromJson(response.body(), Subtask.class);
            assertEquals(subtask1, subtask2, "Subtasks не совпадают.");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            fail("Тест завершился с ошибкой: " + e.getMessage()); // Завершаем тест с ошибкой
        }
    }

    @DisplayName("GIVEN a new Task " +
            "WHEN a new Task is created and delete " +
            "THEN listTasks.sise() == 0")
    @Test
    void test_7_shoulDeleteTaskById() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(TASK_BASE_URL);
        Task task = new Task(61, TasksType.TASK, "Тест", "Тестовый Task для всех", NEW, Instant.now(), 0);

        // Создаем задачу
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();

        try {
            HttpResponse<String> response = client.send(postRequest, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode(), "Создание задачи не удалось."); // Проверяем статус создания

            // Получаем задачи
            HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create(TASK_BASE_URL + "?id=61")).DELETE().build();
            response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Получение задач не удалось.");

            JsonArray arrayTasks = JsonParser.parseString(response.body()).getAsJsonArray();
            assertEquals(0, arrayTasks.size(), "Количество задач не совпадает.");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            fail("Тест завершился с ошибкой: " + e.getMessage()); // Завершаем тест с ошибкой
        }
    }

    @DisplayName("GIVEN a new Epic " +
            "WHEN a new Epic is created and delete " +
            "THEN listEpics.sise() == 0")
    @Test
    void test_8_shoulDeleteEpicById() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_BASE_URL);
        Epic epic1 = new Epic(61, TasksType.EPIC, "Тест", "Тестовый Epic для всех", NEW, Instant.now(), 0);

        // Создаем задачу
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic1)))
                .build();

        try {
            HttpResponse<String> response = client.send(postRequest, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode(), "Создание Epic не удалось."); // Проверяем статус создания

            // Получаем Epic
            HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create(EPIC_BASE_URL + "?id=61")).DELETE().build();
            response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println("Get response status: " + response.statusCode());
            System.out.println("Get response body: " + response.body());
            assertEquals(200, response.statusCode(), "Получение задач не удалось.");

            JsonArray arrayTasks = JsonParser.parseString(response.body()).getAsJsonArray();
            assertEquals(0, arrayTasks.size(), "Количество задач не совпадает.");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            fail("Тест завершился с ошибкой: " + e.getMessage()); // Завершаем тест с ошибкой
        }
    }

    @DisplayName("GIVEN a new Epic, Subtask " +
            "WHEN a new Epic, Subtask is created and delete Subtask " +
            "THEN listSubtasks.sise() == 0")
    @Test
    void test_9_shoulDeleteSubtaskById() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_BASE_URL);
        Epic epic1 = new Epic(62, TasksType.EPIC, "Тест", "Тестовый Epic для всех", NEW, Instant.now(), 0);

        // Создаем Epic
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic1)))
                .build();

        try {
            HttpResponse<String> response = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
            url = URI.create(SUBTASK_BASE_URL);
            Subtask subtask1 = new Subtask(63, TasksType.SUBTASK, "Тест", "Тестовый Subtask для всех", NEW, Instant.now(), 0, epic1.getId());

            // Создаем Epic
            postRequest = HttpRequest.newBuilder()
                    .uri(url)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask1)))
                    .build();

            response = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Создание или обновление Subtask не удалось."); // Проверяем статус создания

            // Получаем Subtask
            HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create(url + "?id=63")).DELETE().build();
            response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Удаление Subtask не удалось.");

            JsonArray jsonArray = JsonParser.parseString(response.body()).getAsJsonArray();
            assertEquals(0, jsonArray.size(), "Subtasks не удалены.");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            fail("Тест завершился с ошибкой: " + e.getMessage()); // Завершаем тест с ошибкой
        }
    }

    @DisplayName("GIVEN a new Task " +
            "WHEN a new Task is created and deleteAll " +
            "THEN listTasks.sise() == 0")
    @Test
    void test_10_shoulDeleteAllTasks() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(TASK_BASE_URL);
        Task task = new Task(61, TasksType.TASK, "Тест", "Тестовый Task для всех", NEW, Instant.now(), 0);

        // Создаем задачу
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();

        try {
            HttpResponse<String> response = client.send(postRequest, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode(), "Создание задачи не удалось."); // Проверяем статус создания

            // Получаем задачи
            HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create(TASK_BASE_URL)).DELETE().build();
            response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Получение задач не удалось.");

            JsonArray arrayTasks = JsonParser.parseString(response.body()).getAsJsonArray();
            assertEquals(0, arrayTasks.size(), "Количество задач не совпадает.");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            fail("Тест завершился с ошибкой: " + e.getMessage()); // Завершаем тест с ошибкой
        }
    }

    @DisplayName("GIVEN a new Epic " +
            "WHEN a new Epic is created and deleteAll " +
            "THEN listEpics.sise() == 0")
    @Test
    void test_11_shoulDeleteAllEpics() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_BASE_URL);
        Epic epic1 = new Epic(61, TasksType.EPIC, "Тест", "Тестовый Epic для всех", NEW, Instant.now(), 0);

        // Создаем задачу
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic1)))
                .build();

        try {
            HttpResponse<String> response = client.send(postRequest, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode(), "Создание Epic не удалось."); // Проверяем статус создания

            // Получаем Epic
            HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create(EPIC_BASE_URL)).DELETE().build();
            response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println("Get response status: " + response.statusCode());
            System.out.println("Get response body: " + response.body());
            assertEquals(200, response.statusCode(), "Получение задач не удалось.");

            JsonArray arrayTasks = JsonParser.parseString(response.body()).getAsJsonArray();
            assertEquals(0, arrayTasks.size(), "Количество задач не совпадает.");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            fail("Тест завершился с ошибкой: " + e.getMessage()); // Завершаем тест с ошибкой
        }
    }

    @DisplayName("GIVEN a new Epic, Subtask " +
            "WHEN a new Epic, Subtask is created and delete All Subtasks " +
            "THEN listSubtasks.sise() == 0")
    @Test
    void test_12_shoulDeleteSubtaskById() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_BASE_URL);
        Epic epic1 = new Epic(62, TasksType.EPIC, "Тест", "Тестовый Epic для всех", NEW, Instant.now(), 0);

        // Создаем Epic
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic1)))
                .build();

        try {
            HttpResponse<String> response = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
            url = URI.create(SUBTASK_BASE_URL);
            Subtask subtask1 = new Subtask(63, TasksType.SUBTASK, "Тест", "Тестовый Subtask для всех", NEW, Instant.now(), 0, epic1.getId());

            // Создаем Epic
            postRequest = HttpRequest.newBuilder()
                    .uri(url)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask1)))
                    .build();

            response = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Создание или обновление Subtask не удалось."); // Проверяем статус создания

            // Получаем Subtask
            HttpRequest getRequest = HttpRequest.newBuilder().uri(url).DELETE().build();
            response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Удаление Subtask не удалось.");

            JsonArray jsonArray = JsonParser.parseString(response.body()).getAsJsonArray();
            assertEquals(0, jsonArray.size(), "Subtasks не удалены.");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            fail("Тест завершился с ошибкой: " + e.getMessage()); // Завершаем тест с ошибкой
        }
    }

    @DisplayName("GIVEN a new Epic, Subtask " +
            "WHEN a new Epic, Subtask is created " +
            "THEN number of saved Subtask is 1")
    @Test
    void test_13_shouldGetSubtaskById() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_BASE_URL);
        Epic epic1 = new Epic(62, TasksType.EPIC, "Тест", "Тестовый Epic для всех", NEW, Instant.now(), 0);

        // Создаем Epic
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic1)))
                .build();

        try {
            HttpResponse<String> response = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
            url = URI.create(SUBTASK_BASE_URL);
            Subtask subtask1 = new Subtask(63, TasksType.SUBTASK, "Тест", "Тестовый Subtask для всех", NEW, Instant.now(), 0, epic1.getId());

            // Создаем Epic
            postRequest = HttpRequest.newBuilder()
                    .uri(url)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask1)))
                    .build();

            response = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Создание или обновление Subtask не удалось."); // Проверяем статус создания

            // Получаем Subtask
            HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create(url + "epic/?id=62")).GET().build();
            response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Получение Subtask не удалось.");

            JsonArray jsonArray = JsonParser.parseString(response.body()).getAsJsonArray();
            assertEquals(1, jsonArray.size(), "Список пустой.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            fail("Тест завершился с ошибкой: " + e.getMessage()); // Завершаем тест с ошибкой
        }
    }

    @DisplayName("GIVEN a new history " +
            "WHEN a new history " +
            "THEN history is Empty")
    @Test
    void test_14_shouldGetHistory() {

        HttpClient client = HttpClient.newHttpClient();
        // Получаем историю
        try {
            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/tasks/history/"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Получение истории не удалось.");

            JsonArray arrayTasks = JsonParser.parseString(response.body()).getAsJsonArray();
            assertEquals(0, arrayTasks.size(), "Список истории не пуст.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            fail("Ошибка при отправке GET-запроса: " + e.getMessage());
        }
    }


   /* @Test
    void test_15_shouldGetPtiority() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks/");
            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(url)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Получение истории не удалось.");

            JsonArray arrayTasks = JsonParser.parseString(response.body()).getAsJsonArray();
            assertEquals(0, arrayTasks.size(), "Список приорити не пуст.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            fail("Ошибка при отправке GET-запроса: " + e.getMessage());
        }
    }*/


}