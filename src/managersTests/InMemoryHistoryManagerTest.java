package TestNew.manager;

import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Task;

import java.time.Instant;
import java.util.Collections;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static task.Status.NEW;
import static task.TasksType.TASK;

class InMemoryHistoryManagerTest {
    HistoryManager manager;
    private int id = 0;

    public int generateId() {
        return ++id;
    }

    protected Task addNewTask() {
        return new Task(3, TASK, "Таск1", NEW,  "описание2",  Instant.now(), 0);
    }

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryHistoryManager();
    }

    @Test
    public void shouldAddTasksToHistory() {
        Task task1 = addNewTask();
        int newTaskId1 = generateId();
        task1.setId(newTaskId1);
        Task task2 = addNewTask();
        int newTaskId2 = generateId();
        task2.setId(newTaskId2);
        Task task3 = addNewTask();
        int newTaskId3 = generateId();
        task3.setId(newTaskId3);
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);
        assertEquals(List.of(task1, task2, task3), manager.getHistory());
    }

    @Test
    public void shouldRemoveTask() {
        Task task1 = addNewTask();
        int newTaskId1 = generateId();
        task1.setId(newTaskId1);
        Task task2 = addNewTask();
        int newTaskId2 = generateId();
        task2.setId(newTaskId2);
        Task task3 = addNewTask();
        int newTaskId3 = generateId();
        task3.setId(newTaskId3);
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);
        manager.remove(task2.getId());
        assertEquals(List.of(task1, task3), manager.getHistory());
    }

    @Test
    public void shouldRemoveOnlyOneTask() {
        Task task = addNewTask();
        int newTaskId = generateId();
        task.setId(newTaskId);
        manager.add(task);
        manager.remove(task.getId());
        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }

    @Test
    public void shouldHistoryIsEmpty() {
        Task task1 = addNewTask();
        int newTaskId1 = generateId();
        task1.setId(newTaskId1);
        Task task2 = addNewTask();
        int newTaskId2 = generateId();
        task2.setId(newTaskId2);
        Task task3 = addNewTask();
        int newTaskId3 = generateId();
        task3.setId(newTaskId3);
        manager.remove(task1.getId());
        manager.remove(task2.getId());
        manager.remove(task3.getId());
        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }

    @Test
    public void shouldNotRemoveTaskWithBadId() {
        Task task = addNewTask();
        int newTaskId = generateId();
        task.setId(newTaskId);
        manager.add(task);
        assertEquals(List.of(task), manager.getHistory());
    }

    @Test
    void duplexTasksList() {
        manager.add(new Task(1, TASK, "Таск1", NEW,  "описание1",  Instant.now(), 0));

        manager.add(new Task(1, TASK, "Таск1", NEW,  "описание1",  Instant.now(), 0));

        assertEquals(1, manager.getHistory().size(), "Дублирование одинаковых таск в истории");
    }
}
