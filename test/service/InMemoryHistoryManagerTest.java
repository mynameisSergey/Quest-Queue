package service;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static service.Managers.getDefaultHistory;

class InMemoryHistoryManagerTest {

    InMemoryHistoryManager inMemoryHistoryManager;

    Task task1;
    Subtask subtask1;
    Subtask subtask2;
    Epic epic1;

    @BeforeEach
    public void createTasksEpicAndSubtasks() {
        task1 = new Task(1, TasksType.TASK, "Тест", "Тестовая задача для всех", StatusTasks.NEW, Instant.now(), 3);
        epic1 = new Epic(2, TasksType.EPIC, "Тест", "Тестовый Эпик для всех", StatusTasks.NEW,Instant.now(), 3);
        subtask1 = new Subtask(3, TasksType.SUBTASK, "Тест", "Подзадача 1 для теста", StatusTasks.NEW,
                Instant.now(), 3, 2);
        subtask2 = new Subtask(4, TasksType.SUBTASK, "Тест", "Подзадача 2 для теста", StatusTasks.NEW,
                Instant.now(), 3, 2);

    }

    @BeforeEach
    public void getinMemoryHistoryManager() {
        inMemoryHistoryManager = getDefaultHistory();
    }

    @DisplayName("GIVEN a new Task, Epic, 2 Subtasks " +
            "WHEN a new Task, Epis, 2 Subtasks is created " +
            "THEN Task, Epis, 2 Subtasks put in list history")
    @Test
    void test1_addTaskEpic2SubtasksInToMap() {
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(epic1);
        inMemoryHistoryManager.add(subtask1);
        inMemoryHistoryManager.add(subtask2);
        List<Task> list = inMemoryHistoryManager.getHistory();
        assertEquals(Arrays.asList(task1, epic1, subtask1, subtask2), list);
    }

    @DisplayName("GIVEN empty historymap " +
            "WHEN empty historymap " +
            "THEN we get an empty list of task history")
    @Test
    void test2_addTaskInToMap() {
        List<Task> list = inMemoryHistoryManager.getHistory();
        assertTrue(list.isEmpty());
    }

    @DisplayName("GIVEN a 2 identical Tasks, 2 identical Epics, " +
            "2 identical Subtasks " +
            "WHEN a new identical Tasks add in list " +
            "THEN in historylist have 1 Task, 1 Epic, 1 Subtask")
    @Test
    void test3_add2IdenticalTasks2IdenticalEpics2IdenticalSubtasks() {
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(epic1);
        inMemoryHistoryManager.add(epic1);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(subtask1);
        inMemoryHistoryManager.add(epic1);
        inMemoryHistoryManager.add(subtask1);
        var list = inMemoryHistoryManager.getHistory();
        assertEquals(Arrays.asList(task1, epic1, subtask1), list);
    }

    @DisplayName("GIVEN a new Task, Epic, Subtasks " +
            "WHEN a new Tasks add in list and remove Tasks from firstslist " +
            "THEN we get stories without remote task")
    @Test
    void test4_schouldGetHistoryListWitchoutRemoveTask() {
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(epic1);
        inMemoryHistoryManager.add(subtask1);
        inMemoryHistoryManager.remove(task1.getId());
        List<Task> list = inMemoryHistoryManager.getHistory();
        assertEquals(Arrays.asList(epic1, subtask1), list);
    }

    @DisplayName("GIVEN a new Task, Epic, Subtasks " +
            "WHEN a new Tasks add in list and remove Tasks from middle " +
            "THEN we get stories without remote Epic")
    @Test
    void test5_chouldGetHistoryListWitchoutRemoveEpic() {
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(epic1);
        inMemoryHistoryManager.add(subtask1);
        inMemoryHistoryManager.remove(epic1.getId());
        List<Task> list = inMemoryHistoryManager.getHistory();
        assertEquals(Arrays.asList(task1, subtask1), list);
    }

    @DisplayName("GIVEN a new Task, Epic, Subtasks " +
            "WHEN a new Tasks, Epic, Subtasks created " +
            "THEN we get lists of tasks, epics, subtasks")
    @Test
    void test6_chouldGetHistoryListWitchoutRemoveEpic() {
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(epic1);
        inMemoryHistoryManager.add(subtask1);
        inMemoryHistoryManager.remove(subtask1.getId());
        List<Task> list = inMemoryHistoryManager.getHistory();
        assertEquals(Arrays.asList(task1, epic1), list);
    }
}