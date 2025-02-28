package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    static TaskManager inMemoryTaskManager;
    public static Epic epic1;
    public static Subtask subtask1;
    public static Subtask subtask2;

    @BeforeEach
    public void createNewEpicAndNewSubtasks() {
        inMemoryTaskManager = Managers.getDefaultInMemory();

        epic1 = new Epic(TasksType.EPIC, "Тестовый Эпик для всех", "описаниеТеста", Instant.now(), 0);
        inMemoryTaskManager.putEpic(epic1);

        subtask1 = new Subtask(TasksType.SUBTASK, "Подзадача 1 для теста", "тест", Instant.now(), 0, epic1.getId());
        subtask2 = new Subtask(TasksType.SUBTASK, "Подзадача 2 для т", "тест", Instant.now(), 0,  epic1.getId());

        inMemoryTaskManager.putSubtask(subtask1);
        inMemoryTaskManager.putSubtask(subtask2);
    }

    @DisplayName("GIVEN a new Epic " +
            "WHEN a new Epic is created " +
            "THEN remove Subtask from Epic" +
            "status Epic if subtasksArrayList is empty")
    @Test
    void test1_statusEpicIfsubtaskArrayListIsEmpty() {
        inMemoryTaskManager.getStatusEpic(epic1.getId());
        inMemoryTaskManager.removeSubtask(subtask1.getId());
        inMemoryTaskManager.removeSubtask(subtask2.getId());

        StatusTasks status = inMemoryTaskManager.getEpicOfId(epic1.getId()).getStatusTasks();
        assertEquals(StatusTasks.NEW, status);
    }

    @DisplayName("GIVEN a new Epic " +
            "WHEN a new Epic is created " +
            "THEN order creation method returns " +
            "status Epic if all subtasks is NEW")
    @Test
    void test2_statusEpicIfAllSubtasksIsNew() {
        inMemoryTaskManager.getStatusEpic(epic1.getId());
        StatusTasks status = inMemoryTaskManager.getEpicOfId(epic1.getId()).getStatusTasks();
        assertEquals(StatusTasks.NEW, status);
    }

    @DisplayName("GIVEN a new Epic " +
            "WHEN a new Epic is created " +
            "THEN order creation method returns " +
            "status Epic if All subtasks is DONE")
    @Test
    void test3_statusEpicIfAllSubtasksIsDone() {
        subtask1.setStatus(StatusTasks.DONE);
        subtask2.setStatus(StatusTasks.DONE);
        inMemoryTaskManager.updateSubtask(subtask1);
        inMemoryTaskManager.updateSubtask(subtask2);
        StatusTasks status = inMemoryTaskManager.getEpicOfId(epic1.getId()).getStatusTasks();
        assertEquals(StatusTasks.DONE, status);
    }

    @DisplayName("GIVEN a new Epic " +
            "WHEN a new Epic is created " +
            "THEN order creation method returns " +
            "status Epic if 1 subtasks is DONE")
    @Test
    void test4_statusEpicIfOneSubtaskIsDone() {
        subtask1.setStatus(StatusTasks.DONE);
        inMemoryTaskManager.updateSubtask(subtask1);
        StatusTasks status = inMemoryTaskManager.getEpicOfId(epic1.getId()).getStatusTasks();
        assertEquals(StatusTasks.IN_PROGRESS, status);
    }

    @DisplayName("GIVEN a new Epic " +
            "WHEN a new Epic is created " +
            "THEN order creation method returns " +
            "status Epic if 2 subtasks IN_PROGRESS")
    @Test
    void test5_statusEpicIfTwoSubtasksInProgress() {
        subtask1.setStatus(StatusTasks.IN_PROGRESS);
        subtask2.setStatus(StatusTasks.IN_PROGRESS);
        inMemoryTaskManager.updateSubtask(subtask1);
        inMemoryTaskManager.updateSubtask(subtask2);
        StatusTasks status = inMemoryTaskManager.getEpicOfId(epic1.getId()).getStatusTasks();
        assertEquals(StatusTasks.IN_PROGRESS, status);
    }

}