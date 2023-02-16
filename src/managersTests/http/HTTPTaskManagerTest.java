package managersTests.http;

import http.HTTPTaskManager;
import http.KVServer;
import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;

import managersTests.TaskManagerTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;


import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static task.Status.NEW;
import static task.TasksType.*;

class HTTPTaskManagerTest<T extends TaskManagerTest<HTTPTaskManager>> {
    private KVServer server;
    private TaskManager manager;

    @BeforeEach
    public void createManager() {
        try {
            server = new KVServer();
            server.start();
            HistoryManager historyManager = Managers.getDefaultHistory();
            manager = Managers.getDefault(historyManager);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при создании менеджера");
        }
    }

    @AfterEach
    public void stopServer() {
        server.stop();
    }

    @Test
    public void shouldLoadTasks() {
        Task task1 = new Task(1, TASK, "Таск1", NEW,  "описание1",  Instant.now(), 0);
        Task task2 = new Task(9, TASK, "Таск1", NEW,  "описание1",  Instant.now(), 0);
        manager.addNewTask(task1);
        manager.addNewTask(task2);
        manager.getTask(task1.getId());
        manager.getTask(task2.getId());
        List<Task> list = manager.getHistory();
        assertEquals(manager.getAllTask(), list);
    }

    @Test
    public void shouldLoadEpics() {
        Epic epic1 = new Epic(3, EPIC, "эпик1", NEW,  "эпик первый с двумя сабами", Instant.now(), 0);
        Epic epic2 = new Epic(4, EPIC, "эпик2", NEW,  "эпик второй с двумя сабами", Instant.now(), 0);
        manager.addNewEpic(epic1);
        manager.addNewEpic(epic2);
        manager.getEpic(epic1.getId());
        manager.getEpic(epic2.getId());
        List<Task> list = manager.getHistory();
        assertEquals(manager.getAllEpic(), list);
    }

    @Test
    public void shouldLoadSubtasks() {
        Epic epic1 = new Epic(5, EPIC, "эпик1", NEW,  "эпик первый с двумя сабами", Instant.now(), 5);
        Subtask subtask1 = new Subtask(10, SUBTASK, "Сабстак1", NEW,  "Сабстак1 первого эпика", Instant.now(), 0, epic1.getId());
        Subtask subtask2 = new Subtask(11, SUBTASK, "Сабстак2", NEW,  "Сабстак2 первого эпика", Instant.now(), 0, epic1.getId());
        manager.addNewSubtask(subtask1);
        manager.addNewSubtask(subtask2);
        manager.getSubtask(subtask1.getId());
        manager.getSubtask(subtask2.getId());
        List<Task> list = manager.getHistory();
        assertEquals(manager.getAllSubtask(), list);
    }

}

