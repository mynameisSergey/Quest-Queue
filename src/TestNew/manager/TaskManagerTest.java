package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import manager.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.*;
import static tasks.Status.*;
import static tasks.TasksType.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;
    protected Task addNewTask() {
        return new Task(1, TASK, "Таск1", NEW,  "описание1",  Instant.now(), 0);
    }
    protected Epic addNewEpic() {

        return new Epic(3, EPIC, "эпик1", NEW,  "эпик первый с двумя сабами", Instant.now(), 0);
    }
    protected Subtask addNewSubtask(Epic epic) {
        return new Subtask(4, SUBTASK, "Сабстак1", NEW,  "Сабстак1 первого эпика", Instant.now(), 0, epic.getId());
    }

    @Test
    public void shouldaddNewTask() {
        Task task = addNewTask();
        manager.addNewTask(task);
        List<Task> tasks = manager.getAllTask();
        assertNotNull(task.getStatus());
        assertEquals(NEW, task.getStatus());
        assertEquals(List.of(task), tasks);
    }

    @Test
    public void shouldaddNewEpic() {
        Epic epic = addNewEpic();
        manager.addNewEpic(epic);
        List<Epic> epics = manager.getAllEpic();
        assertNotNull(epic.getStatus());
        assertEquals(NEW, epic.getStatus());
        assertEquals(Collections.EMPTY_LIST, epic.getSubTaskId());
        assertEquals(List.of(epic), epics);
    }

    @Test
    public void shouldaddNewSubtask() {
        Epic epic = addNewEpic();
        manager.addNewEpic(epic);
        Subtask subtask = addNewSubtask(epic);
        manager.addNewSubtask(subtask);
        List<Subtask> subtasks = manager.getAllSubtask();
        assertNotNull(subtask.getStatus());
        assertEquals(epic.getId(), subtask.getEpicId());
        assertEquals(NEW, subtask.getStatus());
        assertEquals(List.of(subtask), subtasks);
        assertEquals(List.of(subtask.getId()), epic.getSubTaskId());
    }

    @Test
    void shouldReturnNullWhenaddNewTaskNull() {
        Task task = manager.addNewTask(null);
        assertNull(task);
    }

    @Test
    void shouldReturnNullWhenaddNewEpicNull() {
        Epic epic = manager.addNewEpic(null);
        assertNull(epic);
    }

    @Test
    void shouldReturnNullWhenaddNewSubtaskNull() {
        Subtask subtask = manager.addNewSubtask(null);
        assertNull(subtask);
    }

    @Test
    public void shouldUpdateTaskStatusToInProgress() {
        Task task = addNewTask();
        manager.addNewTask(task);
        task.setStatus(IN_PROGRESS);
        manager.updateTask(task);
        assertEquals(IN_PROGRESS, manager.getTask(task.getId()).getStatus());
    }

    @Test
    public void shouldUpdateEpicStatusToInProgress() {
        Epic epic = addNewEpic();
        manager.addNewEpic(epic);
        epic.setStatus(IN_PROGRESS);
        assertEquals(IN_PROGRESS, manager.getEpic(epic.getId()).getStatus());
    }

    @Test
    public void shouldUpdateSubtaskStatusToInProgress() {
        Epic epic = addNewEpic();
        manager.addNewEpic(epic);
        Subtask subtask = addNewSubtask(epic);
        manager.addNewSubtask(subtask);
        subtask.setStatus(IN_PROGRESS);
        manager.updateSubtask(subtask);
        assertEquals(IN_PROGRESS, manager.getSubtask(subtask.getId()).getStatus());
        assertEquals(IN_PROGRESS, manager.getEpic(epic.getId()).getStatus());
    }

    @Test
    public void shouldUpdateTaskStatusToInDone() {
        Task task = addNewTask();
        manager.addNewTask(task);
        task.setStatus(DONE);
        manager.updateTask(task);
        assertEquals(DONE, manager.getTask(task.getId()).getStatus());
    }

    @Test
    public void shouldUpdateEpicStatusToInDone() {
        Epic epic = addNewEpic();
        manager.addNewEpic(epic);
        epic.setStatus(DONE);
        assertEquals(DONE, manager.getEpic(epic.getId()).getStatus());
    }

    @Test
    public void shouldUpdateSubtaskStatusToInDone() {
        Epic epic = addNewEpic();
        manager.addNewEpic(epic);
        Subtask subtask = addNewSubtask(epic);
        manager.addNewSubtask(subtask);
        subtask.setStatus(DONE);
        manager.updateSubtask(subtask);
        assertEquals(DONE, manager.getSubtask(subtask.getId()).getStatus());
        assertEquals(DONE, manager.getEpic(epic.getId()).getStatus());
    }

    @Test
    public void shouldNotUpdateTaskIfNull() {
        Task task = addNewTask();
        manager.addNewTask(task);
        manager.updateTask(null);
        assertEquals(task, manager.getTask(task.getId()));
    }

    @Test
    public void shouldNotUpdateEpicIfNull() {
        Epic epic = addNewEpic();
        manager.addNewEpic(epic);
        manager.updateEpic(null);
        assertEquals(epic, manager.getEpic(epic.getId()));
    }

    @Test
    public void shouldNotUpdateSubtaskIfNull() {
        Epic epic = addNewEpic();
        manager.addNewEpic(epic);
        Subtask subtask = addNewSubtask(epic);
        manager.addNewSubtask(subtask);
        manager.updateSubtask(null);
        assertEquals(subtask, manager.getSubtask(subtask.getId()));
    }

    @Test
    public void shouldremoveAllTask() {
        Task task = addNewTask();
        manager.addNewTask(task);
        manager.removeAllTask();
        assertEquals(Collections.EMPTY_LIST, manager.getAllTask());
    }

    @Test
    public void shouldremoveAllEpic() {
        Epic epic = addNewEpic();
        manager.addNewEpic(epic);
        manager.removeAllEpic();
        assertEquals(Collections.EMPTY_LIST, manager.getAllEpic());
    }

    @Test
    public void shouldremoveAllSubtask() {
        Epic epic = addNewEpic();
        manager.addNewEpic(epic);
        Subtask subtask =addNewSubtask(epic);
        manager.addNewSubtask(subtask);
        manager.removeAllSubtask();
        assertTrue(epic.getSubTaskId().isEmpty());
        assertTrue(manager.getAllSubtask().isEmpty());
    }

    @Test
    public void shouldremoveAllSubtaskByEpic() {
        Epic epic = addNewEpic();
        manager.addNewEpic(epic);
        Subtask subtask = addNewSubtask(epic);
        manager.addNewSubtask(subtask);
        manager.removeAllSubtaskByEpic(epic);
        assertTrue(epic.getSubTaskId().isEmpty());
        assertTrue(manager.getAllSubtask().isEmpty());
    }

    @Test
    public void shouldremoveTaskId() {
        Task task = addNewTask();
        manager.addNewTask(task);
        manager.removeTaskId(task.getId());
        assertEquals(Collections.EMPTY_LIST, manager.getAllTask());
    }

    @Test
    public void shouldremoveEpicId() {
        Epic epic = addNewEpic();
        manager.addNewEpic(epic);
        manager.removeEpicId(epic.getId());
        assertEquals(Collections.EMPTY_LIST, manager.getAllEpic());
    }

    @Test
    public void shouldNotDeleteTaskIfBadId() {
        Task task = addNewTask();
        manager.addNewTask(task);
        manager.removeTaskId(999);
        assertEquals(List.of(task), manager.getAllTask());
    }

    @Test
    public void shouldNotDeleteEpicIfBadId() {
        Epic epic = addNewEpic();
        manager.addNewEpic(epic);
        manager.removeEpicId(999);
        assertEquals(List.of(epic), manager.getAllEpic());
    }

    @Test
    public void shouldNotDeleteSubtaskIfBadId() {
        Epic epic = addNewEpic();
        manager.addNewEpic(epic);
        Subtask subtask = addNewSubtask(epic);
        manager.addNewSubtask(subtask);
        manager.removeSubtaskId(999);
        assertEquals(List.of(subtask), manager.getAllSubtask());
        assertEquals(List.of(subtask.getId()), manager.getEpic(epic.getId()).getSubTaskId());
    }

    @Test
    public void shouldDoNothingIfTaskHashMapIsEmpty(){
        manager.removeAllTask();
        manager.removeTaskId(999);
        assertEquals(0, manager.getAllTask().size());
    }

    @Test
    public void shouldDoNothingIfEpicHashMapIsEmpty(){
        manager.removeAllEpic();
        manager.removeEpicId(999);
        assertTrue(manager.getAllEpic().isEmpty());
    }

    @Test
    public void shouldDoNothingIfSubtaskHashMapIsEmpty(){
        manager.removeAllEpic();
        manager.removeSubtaskId(999);
        assertEquals(0, manager.getAllSubtask().size());
    }

    @Test
    void shouldReturnEmptyListWhenGetSubtaskByEpicIdIsEmpty() {
        Epic epic = addNewEpic();
        manager.addNewEpic(epic);
        List<Subtask> subtasks = manager.getIdSubtask(epic.getId());
        assertTrue(subtasks.isEmpty());
    }

    @Test
    public void shouldReturnEmptyListTasksIfNoTasks() {
        assertTrue(manager.getAllTask().isEmpty());
    }

    @Test
    public void shouldReturnEmptyListEpicsIfNoEpics() {
        assertTrue(manager.getAllEpic().isEmpty());
    }

    @Test
    public void shouldReturnEmptyListSubtasksIfNoSubtasks() {
        assertTrue(manager.getAllSubtask().isEmpty());
    }

    @Test
    public void shouldReturnNullIfTaskDoesNotExist() {
        assertNull(manager.getTask(999));
    }

    @Test
    public void shouldReturnNullIfEpicDoesNotExist() {
        assertNull(manager.getEpic(999));
    }

    @Test
    public void shouldReturnNullIfSubtaskDoesNotExist() {
        assertNull(manager.getSubtask(999));
    }

    @Test
    public void shouldReturnEmptyHistory() {
        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }

    @Test
    public void shouldReturnEmptyHistoryIfTasksNotExist() {
        manager.getTask(999);
        manager.getSubtask(999);
        manager.getEpic(999);
        assertTrue(manager.getHistory().isEmpty());
    }

    @Test
    public void shouldReturnHistoryWithTasks() {
        Epic epic = addNewEpic();
        manager.addNewEpic(epic);
        Subtask subtask = addNewSubtask(epic);
        manager.addNewSubtask(subtask);
        manager.getEpic(epic.getId());
        manager.getSubtask(subtask.getId());
        List<Task> list = manager.getHistory();
        assertEquals(2, list.size());
        assertTrue(list.contains(subtask));
        assertTrue(list.contains(epic));
    }
}