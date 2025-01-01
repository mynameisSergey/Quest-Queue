package http;

import http.manager.HttpTaskManager;
import http.server.KVServer;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TasksType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManagerTest;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static model.StatusTasks.NEW;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    KVServer server;

    @BeforeEach
    void creatManager() {  //данные будут восстанавливаться с KVServer сервера.
        try {
            server = new KVServer();
            server.start();
            manager = Managers.getDefault();
            System.out.println("Все успешно!");
        } catch (IOException e) {
            System.out.println("Ошибка при создании менеджера");
        }
    }

    @AfterEach
    void stop() {
        server.stopServer();
    }

    @DisplayName("GIVEN a new Tasks " +
            "WHEN a new Tasks is created " +
            "THEN number of saved tasks is 2")
    @Test
    void test1_createTasks() {
        Task task1 = new Task(1, TasksType.TASK, "Тест", "Тестовый Task для всех", NEW,
                Instant.now(), 0);
        Task task2 = new Task(9, TasksType.TASK, "Тест2", "Тестовый2 Task для всех", NEW,
                Instant.now(), 0);
        manager.putTask(task1);
        manager.putTask(task2);
        manager.getTaskOdId(task1.getId());
        manager.getTaskOdId(task2.getId());
        List<Task> list = manager.getHistory();
        assertEquals(manager.getArrayTask(), list);
    }

    @DisplayName("GIVEN a new Epics " +
            "WHEN a new Epics is created " +
            "THEN number of saved epics is 2")
    @Test
    void test2_createEpics() {
        Epic epic1 = new Epic(1, TasksType.EPIC, "Тестовый Эпик для всех", "описаниеТеста", NEW,
                Instant.now(), 0);
        Epic epic2 = new Epic(2, TasksType.EPIC, "Тестовый Эпик для всех", "описаниеТеста", NEW,
                Instant.now(), 0);
        manager.putEpic(epic1);
        manager.putEpic(epic2);
        manager.getEpicOfId(epic1.getId());
        manager.getEpicOfId(epic2.getId());
        List<Task> list = manager.getHistory();
        assertEquals(manager.getArrayEpic(), list);
    }

    @DisplayName("GIVEN a new Subtasks " +
            "WHEN a new Subtasks is created " +
            "THEN number of saved subtasks is 2")
    @Test
    void test3_createSubtasks() {
        Epic epic1 = new Epic(5, TasksType.EPIC, "Тестовый Эпик для всех", "описаниеТеста", NEW,
                Instant.now(), 0);
        Subtask subtask3 = new Subtask(7, TasksType.SUBTASK, "Подзадача 1 для теста", "тест", NEW,
                Instant.now(), 0, 5);
        Subtask subtask4 = new Subtask(8, TasksType.SUBTASK, "Подзадача 2 для теста", "тест", NEW,
                Instant.now(), 0, 5);
        manager.putEpic(epic1);
        manager.putSubtask(subtask3);
        manager.putSubtask(subtask4);
        manager.getSubtaskOfId(subtask3.getId());
        manager.getSubtaskOfId(subtask4.getId());
        List<Task> list = manager.getHistory();
        assertEquals(manager.getArraySubtask(), list);
    }

}