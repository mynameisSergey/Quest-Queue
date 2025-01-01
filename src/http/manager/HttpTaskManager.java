package http.manager;

import adapters.InstantAdapter;
import com.google.gson.*;
import http.server.KVTaskClient;
import model.Epic;
import model.Subtask;
import model.Task;
import service.FileBackedTasksManager;

import java.time.Instant;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {
    private static final String TASK_KEY = "task";
    private static final String EPIC_KEY = "epic";
    private static final String SUBTASK_KEY = "subtask";
    private static final String HISTORY_KEY = "history";

    protected static final Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();

    KVTaskClient client;

    public HttpTaskManager(String pathToList) {
        super(pathToList);
        this.client = new KVTaskClient(pathToList);

        JsonElement jsonTasks = JsonParser.parseString(this.client.load(TASK_KEY));
        if (!jsonTasks.isJsonNull()) {
            JsonArray jsonArray = jsonTasks.getAsJsonArray();
            for (JsonElement jsonTask : jsonArray) {
                Task task = gson.fromJson(jsonTask, Task.class);
                this.putTask(task);
            }
        }

        JsonElement jsonEpics = JsonParser.parseString(this.client.load(EPIC_KEY));
        if (!jsonEpics.isJsonNull()) {
            JsonArray jsonArray = jsonEpics.getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                Epic epic = gson.fromJson(jsonElement, Epic.class);
                this.putEpic(epic);
            }
        }

        JsonElement jsonSubtasks = JsonParser.parseString(this.client.load(SUBTASK_KEY));

        if (!jsonSubtasks.isJsonNull()) {
            JsonArray jsonSubtasksArray = jsonSubtasks.getAsJsonArray();
            for (JsonElement jsonElement : jsonSubtasksArray) {
                Subtask subtask = gson.fromJson(jsonElement, Subtask.class);
                this.putSubtask(subtask);
            }
        }

        JsonElement jsonHistory = JsonParser.parseString(this.client.load(HISTORY_KEY));
        if (!jsonHistory.isJsonNull()) {
            JsonArray jsonHistoryArray = jsonHistory.getAsJsonArray();
            for (JsonElement jsonElement : jsonHistoryArray) {
                int id = jsonElement.getAsInt();
                if (this.taskMap.containsKey(id)) {
                    getTaskOdId(id);
                } else if (this.epicMap.containsKey(id)) {
                    getEpicOfId(id);
                } else if (this.subtaskMap.containsKey(id)) {
                    getSubtaskOfId(id);
                }
            }
        }
    }

    @Override
    public void save() {

        client.put(TASK_KEY, gson.toJson(taskMap.values()));
        client.put(EPIC_KEY, gson.toJson(epicMap.values()));
        client.put(SUBTASK_KEY, gson.toJson(subtaskMap.values()));
        client.put(HISTORY_KEY, gson.toJson(this.getHistory()
                .stream()
                .map(Task::getId)
                .collect(Collectors.toList())));
    }
}
