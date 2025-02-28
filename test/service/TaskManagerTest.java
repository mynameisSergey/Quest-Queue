package service;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static model.StatusTasks.NEW;
import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T manager;
    Task task1;
    Subtask subtask1;
    Subtask subtask2;
    Epic epic1;

    @BeforeEach
    public void createTasksEpicAndSubtasks() {
        task1 = new Task(1, TasksType.TASK, "Тестовая задача для всех", "описаниеТеста", NEW, Instant.now(), 0);
        epic1 = new Epic(2, TasksType.EPIC, "Тестовый Эпик для всех", "описаниеТеста", NEW, Instant.now(), 0);
        subtask1 = new Subtask(3, TasksType.SUBTASK, "Подзадача 1", "Описание 1", StatusTasks.NEW, Instant.now(), 0, 2);
        subtask2 = new Subtask(4, TasksType.SUBTASK, "Подзадача 2", "Описание 2", StatusTasks.NEW, Instant.now(), 0, 2);

    }

    @DisplayName("GIVEN a new Task " +
            "WHEN a new Task is created " +
            "THEN Task in not NULL " + " status NEW " +
            " return Task")
    @Test
    public void test1_TaskIsNotNullAndStatusNew() {
        manager.putTask(task1);
        assertNotNull(task1);
        assertEquals(NEW, task1.getStatus());
        assertEquals(1, task1.getId());
        assertEquals(task1, manager.getTaskOdId(task1.getId()));
    }

    @DisplayName("GIVEN a new  Epic " +
            "WHEN a new Epic is created " +
            "THEN Epic in not NULL " + " status NEW " +
            " return Epic")
    @Test
    public void test2_EpicIsNotNullAndStatusNew() {
        manager.putEpic(epic1);
        manager.putSubtask(subtask1);
        assertNotNull(epic1);
        assertEquals(NEW, epic1.getStatus());
        assertEquals(2, epic1.getId());
        assertEquals(epic1, manager.getEpicOfId(epic1.getId()));
        epic1.setStatusTasks(StatusTasks.DONE);
        manager.updateEpic(epic1);
    }

    @DisplayName("GIVEN a new  Subtask " +
            "WHEN a new Subtask is created " +
            "THEN Subtask is not NULL " + " status NEW " +
            "return Subtask ")
    @Test
    public void test3_SubtaskIsNotNullAndStatusNew() {
        manager.putEpic(epic1);
        manager.putSubtask(subtask1);
        assertNotNull(subtask1);
        assertEquals(NEW, subtask1.getStatus());
        assertEquals(3, subtask1.getId());
        assertFalse(manager.getAllSubtask().isEmpty());
        assertEquals(subtask1, manager.getSubtaskOfId(subtask1.getId()));
        manager.clearMapOfSubtaskFromEpic(2);
        assertTrue(epic1.getSubtaskArrayList().isEmpty());
    }

    @DisplayName("GIVEN a new Task " +
            "WHEN a new Task is created " +
            "THEN ArrayList<Task> in not Empty " + "clear ArrayList isEmpty")
    @Test
    public void test4_ArrayListTasksIsNotNull() {
        manager.putTask(task1);
        assertFalse(manager.getArrayTask().isEmpty());
        manager.clearMapOfTask();
        assertTrue(manager.getArrayTask().isEmpty());
    }

    @DisplayName("GIVEN a new Epic " +
            "WHEN a new Epic is created " +
            "THEN ArrayList<Epic> is not Empty " + "clear ArrayList isEmpty")
    @Test
    public void test5_ArrayListEpicIsNotEmpty() {
        manager.putEpic(epic1);
        assertFalse(manager.getArrayEpic().isEmpty());
        manager.clearMapOfEpic();
        assertTrue(manager.getArrayEpic().isEmpty());
    }

    @DisplayName("GIVEN a new Subtask " +
            "WHEN a new Subtask is created " +
            "THEN ArrayList<Subtask> in not Empty " + "clear ArrayList isEmpty")
    @Test
    public void test6_ArrayListSubtaskIsNotEmpty() {
        manager.putEpic(epic1);
        manager.putSubtask(subtask1);
        assertFalse(manager.getArraySubtask().isEmpty());
        manager.clearMapOfSubtask();
        assertTrue(manager.getArraySubtask().isEmpty());
    }

    @DisplayName("GIVEN a new Task " +
            "WHEN a new Task is created " +
            "THEN ArrayList<Task> equals to tasklist call")
    @Test
    public void test7_ArrayListTasksEqualsTaskList() {
        manager.putTask(task1);
        List<Task> list = new ArrayList<>();
        list.add(task1);
        assertEquals(list, manager.getArrayTask());
    }

    @DisplayName("GIVEN a new Epic " +
            "WHEN a new Epic is created " +
            "THEN ArrayList<Epic> equals to epiclist call")
    @Test
    public void test8_ArrayListEpicEqualsEpicList() {
        manager.putEpic(epic1);
        List<Epic> list = new ArrayList<>();
        list.add(epic1);
        assertEquals(list, manager.getArrayEpic());
    }

    @DisplayName("GIVEN a new Subtask " +
            "WHEN a new Subtask is created " +
            "THEN ArrayList<Subtask> equals to subtasklist call")
    @Test
    public void test9_ArrayListSubtaskEqualsSubtasksList() {
        manager.putEpic(epic1);
        manager.putSubtask(subtask1);
        List<Subtask> list = new ArrayList<>();
        list.add(subtask1);
        assertEquals(list, manager.getArraySubtask());
    }

    @DisplayName("GIVEN a new Task " +
            "WHEN a new Task is created " +
            "THEN Status task is DONE")
    @Test
    public void test10_taskUpdateStatusDone() {
        manager.putTask(task1);
        task1.setStatus(StatusTasks.DONE);
        manager.updateTask(task1);
        StatusTasks statusTasks = manager.getTaskOdId(task1.getId()).getStatusTasks();
        assertEquals(StatusTasks.DONE, statusTasks);
    }

    @DisplayName("GIVEN a new Task " +
            "WHEN a new Task is created " +
            "THEN Status task is In_Progress")
    @Test
    public void test11_taskUpdateStatusIn_Progress() {
        manager.putTask(task1);
        task1.setStatus(StatusTasks.IN_PROGRESS);
        manager.updateTask(task1);
        StatusTasks statusTasks = manager.getTaskOdId(task1.getId()).getStatusTasks();
        assertEquals(StatusTasks.IN_PROGRESS, statusTasks);
    }

    @DisplayName("GIVEN a new Task " +
            "WHEN a new Task is created " +
            "THEN Task remove from list")
    @Test
    public void test12_taskRemoveFromList() {
        manager.putTask(task1);
        manager.removeTask(task1.getId());
        assertTrue(manager.getArrayTask().isEmpty());
    }

    @DisplayName("GIVEN a new Epic " +
            "WHEN a new Epic is created " +
            "THEN Epic remove from list")
    @Test
    public void test13_epicRemoveFromList() {
        manager.putEpic(epic1);
        manager.removeEpicOfId(epic1.getId());
        assertTrue(manager.getArrayEpic().isEmpty());
    }

    @DisplayName("GIVEN a new Subtask, Epic " +
            "WHEN a new Subtask, Epic is created " +
            "THEN Subtask remove from list")
    @Test
    public void test14_subtaskRemoveFromList() {
        manager.putEpic(epic1);
        manager.putSubtask(subtask1);
        manager.removeSubtask(subtask1.getId());
        assertTrue(manager.getArraySubtask().isEmpty());
    }

    @DisplayName("GIVEN a new Task, Epic, Subtask " +
            "WHEN a new Task, Epic, Subtask is created " +
            "THEN return history with 3 tasks ")
    @Test
    public void test15_returnHistoryTasks() {
        manager.putEpic(epic1);
        manager.putSubtask(subtask1);
        manager.putSubtask(subtask2);
        manager.putTask(task1);
        manager.getEpicOfId(epic1.getId());
        manager.getSubtaskOfId(subtask1.getId());
        manager.getSubtaskOfId(subtask2.getId());
        manager.getTaskOdId(task1.getId());
        List<Task> list1 = Arrays.asList(epic1, subtask1, subtask2, task1);
        List<Task> list2 = manager.getHistory();
        assertEquals(list1, list2);
    }

    @DisplayName("GIVEN a new  Epic, Subtask " +
            "WHEN a new Epic, Subtask is created " +
            "THEN Epic id equals subtask EpicId ")
    @Test
    public void test16_SubtaskGetEpicIdEqualsEpicId() {
        manager.putEpic(epic1);
        manager.putSubtask(subtask1);
        assertEquals(2, subtask1.getEpicId());
    }

    @DisplayName("GIVEN empty taskmap, epicmap, subtaskmap  " +
            "WHEN get Task, Epic, Subtask from empty maps " +
            "THEN compile exception ")
    @Test
    public void test17_TaskGetFromEmptyTaskMAp() {
        NoSuchElementException a = assertThrows(NoSuchElementException.class, () -> manager.getTaskOdId(1));
        assertEquals("Задача не найдена", a.getMessage());
        NoSuchElementException b = assertThrows(NoSuchElementException.class, () -> manager.getEpicOfId(1));
        assertEquals("Задача не найдена", b.getMessage());
        NoSuchElementException c = assertThrows(NoSuchElementException.class, () -> manager.getSubtaskOfId(1));
        assertEquals("Задача не найдена", c.getMessage());
    }

    @DisplayName("GIVEN Task equal Null  " +
            "WHEN put Task in the map " +
            "THEN compile exception ")
    @Test
    public void test18_TaskEqualNull() {
        IllegalArgumentException a = assertThrows(IllegalArgumentException.class, () -> manager.putTask(null));
        assertEquals("Задача не может быть NULL", a.getMessage());
    }

    @DisplayName("GIVEN Epic equal Null  " +
            "WHEN put Epic in the map " +
            "THEN compile exception ")
    @Test
    public void test19_EpicEqualNull() {

        IllegalArgumentException a = assertThrows(IllegalArgumentException.class, () -> manager.putEpic(null));
        assertEquals("Epic не может быть NULL", a.getMessage());
    }

    @DisplayName("GIVEN Substak equal Null  " +
            "WHEN put Substak in the map " +
            "THEN compile exception ")
    @Test
    public void test20_SubtaskEqualNull() {
        IllegalArgumentException a = assertThrows(IllegalArgumentException.class, () -> manager.putSubtask(null));
        assertEquals("Задача не может быть NULL", a.getMessage());
    }

}
