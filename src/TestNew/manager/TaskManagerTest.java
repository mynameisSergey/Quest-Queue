package TestNew.manager;

import manager.TaskManager;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static tasks.Status.*;
import static tasks.TasksType.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;
    Epic epic;
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
        Assertions.assertNotNull(task.getStatus());
        Assertions.assertEquals(NEW, task.getStatus());
        Assertions.assertEquals(List.of(task), tasks);
    }

    @Test
    public void shouldaddNewEpic() {
        Epic epic = addNewEpic();
        manager.addNewEpic(epic);
        List<Epic> epics = manager.getAllEpic();
        Assertions.assertNotNull(epic.getStatus());
        Assertions.assertEquals(NEW, epic.getStatus());
        Assertions.assertEquals(Collections.EMPTY_LIST, epic.getSubTaskId());
        Assertions.assertEquals(List.of(epic), epics);
    }

    @Test
    public void shouldaddNewSubtask() {
        Epic epic = addNewEpic();
        manager.addNewEpic(epic);
        Subtask subtask = addNewSubtask(epic);
        manager.addNewSubtask(subtask);
        List<Subtask> subtasks = manager.getAllSubtask();
        Assertions.assertNotNull(subtask.getStatus());
        Assertions.assertEquals(epic.getId(), subtask.getEpicId());
        Assertions.assertEquals(NEW, subtask.getStatus());
        Assertions.assertEquals(List.of(subtask), subtasks);
        Assertions.assertEquals(List.of(subtask.getId()), epic.getSubTaskId());
    }

    @Test
    void shouldReturnNullWhenaddNewTaskNull() {
        Task task = manager.addNewTask(null);
        Assertions.assertNull(task);
    }

    @Test
    void shouldReturnNullWhenaddNewEpicNull() {
        Epic epic = manager.addNewEpic(null);
        Assertions.assertNull(epic);
    }

    @Test
    void shouldReturnNullWhenaddNewSubtaskNull() {
        Subtask subtask = manager.addNewSubtask(null);
        Assertions.assertNull(subtask);
    }

    @Test
    public void shouldUpdateTaskStatusToInProgress() {
        Task task = addNewTask();
        manager.addNewTask(task);
        task.setStatus(IN_PROGRESS);
        manager.updateTask(task);
        Assertions.assertEquals(IN_PROGRESS, manager.getTask(task.getId()).getStatus());
    }

    @Test
    public void shouldUpdateEpicStatusToInProgress() {
        Epic epic = addNewEpic();
        manager.addNewEpic(epic);
        epic.setStatus(IN_PROGRESS);
        Assertions.assertEquals(IN_PROGRESS, manager.getEpic(epic.getId()).getStatus());
    }

    @Test
    public void shouldUpdateSubtaskStatusToInProgress() { //Подзадачи со статусом IN_PROGRESS.
        Epic epic = addNewEpic();
        manager.addNewEpic(epic);
        Subtask subtask = addNewSubtask(epic);
        manager.addNewSubtask(subtask);
        subtask.setStatus(IN_PROGRESS);
        manager.updateSubtask(subtask);
        Assertions.assertEquals(IN_PROGRESS, manager.getSubtask(subtask.getId()).getStatus());
        Assertions.assertEquals(IN_PROGRESS, manager.getEpic(epic.getId()).getStatus());
    }
    @Test
    public void shouldUpdateSubtaskStatusToInNewDone() { //Подзадачи со статусом IN_PROGRESS.
        Epic epic = addNewEpic();
        manager.addNewEpic(epic);
        Subtask subtask = addNewSubtask(epic);
        manager.addNewSubtask(subtask);
        subtask.setStatus(IN_PROGRESS);
        manager.updateSubtask(subtask);
        Assertions.assertEquals(IN_PROGRESS, manager.getSubtask(subtask.getId()).getStatus());
        Assertions.assertEquals(IN_PROGRESS, manager.getEpic(epic.getId()).getStatus());
    }
    @Test
    public void shouldUpdateTaskStatusToInDone() {
        Task task = addNewTask();
        manager.addNewTask(task);
        task.setStatus(DONE);
        manager.updateTask(task);
        Assertions.assertEquals(DONE, manager.getTask(task.getId()).getStatus());
    }

    @Test
    public void shouldUpdateEpicStatusToInDone() {
        Epic epic = addNewEpic();
        manager.addNewEpic(epic);
        epic.setStatus(DONE);
        Assertions.assertEquals(DONE, manager.getEpic(epic.getId()).getStatus());
    }


    @Test
    public void shouldUpdateSubtaskStatusToInDone() { // подзадачи со статусом DONE.
        Epic epic = addNewEpic();
        manager.addNewEpic(epic);
        Subtask subtask = addNewSubtask(epic);
        manager.addNewSubtask(subtask);
        subtask.setStatus(DONE);
        manager.updateSubtask(subtask);
        Assertions.assertEquals(DONE, manager.getSubtask(subtask.getId()).getStatus());
        Assertions.assertEquals(DONE, manager.getEpic(epic.getId()).getStatus());
    }

    @Test
    public void shouldNotUpdateTaskIfNull() {
        Task task = addNewTask();
        manager.addNewTask(task);
        manager.updateTask(null);
        Assertions.assertEquals(task, manager.getTask(task.getId()));
    }

    @Test
    public void shouldNotUpdateEpicIfNull() {
        Epic epic = addNewEpic();
        manager.addNewEpic(epic);
        manager.updateEpic(null);
        Assertions.assertEquals(epic, manager.getEpic(epic.getId()));
    }

    @Test
    public void shouldNotUpdateSubtaskIfNull() {
        Epic epic = addNewEpic();
        manager.addNewEpic(epic);
        Subtask subtask = addNewSubtask(epic);
        manager.addNewSubtask(subtask);
        manager.updateSubtask(null);
        Assertions.assertEquals(subtask, manager.getSubtask(subtask.getId()));
    }

    @Test
    public void shouldremoveAllTask() {
        Task task = addNewTask();
        manager.addNewTask(task);
        manager.removeAllTask();
        Assertions.assertEquals(Collections.EMPTY_LIST, manager.getAllTask());
    }

    @Test
    public void shouldremoveAllEpic() {
        Epic epic = addNewEpic();
        manager.addNewEpic(epic);
        manager.removeAllEpic();
        Assertions.assertEquals(Collections.EMPTY_LIST, manager.getAllEpic());
    }

    @Test
    public void shouldremoveAllSubtask() {
        Epic epic = addNewEpic();
        manager.addNewEpic(epic);
        Subtask subtask =addNewSubtask(epic);
        manager.addNewSubtask(subtask);
        manager.removeAllSubtask();
        Assertions.assertTrue(epic.getSubTaskId().isEmpty());
        Assertions.assertTrue(manager.getAllSubtask().isEmpty());
    }

    @Test
    public void shouldremoveAllSubtaskByEpic() { // Пустой список подзадач.
        Epic epic = addNewEpic();
        manager.addNewEpic(epic);
        Subtask subtask = addNewSubtask(epic);
        manager.addNewSubtask(subtask);
        manager.removeAllSubtaskByEpic(epic);
        Assertions.assertTrue(epic.getSubTaskId().isEmpty());
        Assertions.assertTrue(manager.getAllSubtask().isEmpty());
    }

    @Test
    public void shouldremoveTaskId() {
        Task task = addNewTask();
        manager.addNewTask(task);
        manager.removeTaskId(task.getId());
        Assertions.assertEquals(Collections.EMPTY_LIST, manager.getAllTask());
    }

    @Test
    public void shouldremoveEpicId() { ////// lsflsdl;fkjslk;f;s';
        Epic epic = addNewEpic();
        manager.addNewEpic(epic);
        manager.removeEpicId(epic.getId());
        Assertions.assertEquals(Collections.EMPTY_LIST, manager.getAllEpic());
    }

    @Test
    public void shouldNotDeleteTaskIfBadId() {
        Task task = addNewTask();
        manager.addNewTask(task);
        manager.removeTaskId(999);
        Assertions.assertEquals(List.of(task), manager.getAllTask());
    }

    @Test
    public void shouldNotDeleteEpicIfBadId() {
        Epic epic = addNewEpic();
        manager.addNewEpic(epic);
        manager.removeEpicId(999);
        Assertions.assertEquals(List.of(epic), manager.getAllEpic());
    }

    @Test
    public void shouldNotDeleteSubtaskIfBadId() {
        Epic epic = addNewEpic();
        manager.addNewEpic(epic);
        Subtask subtask = addNewSubtask(epic);
        manager.addNewSubtask(subtask);
        manager.removeSubtaskId(999);
        Assertions.assertEquals(List.of(subtask), manager.getAllSubtask());
        Assertions.assertEquals(List.of(subtask.getId()), manager.getEpic(epic.getId()).getSubTaskId());
    }

    @Test
    public void shouldDoNothingIfTaskHashMapIsEmpty(){
        manager.removeAllTask();
        manager.removeTaskId(999);
        Assertions.assertEquals(0, manager.getAllTask().size());
    }

    @Test
    public void shouldDoNothingIfEpicHashMapIsEmpty(){
        manager.removeAllEpic();
        manager.removeEpicId(999);
        Assertions.assertTrue(manager.getAllEpic().isEmpty());
    }

    @Test
    public void shouldDoNothingIfSubtaskHashMapIsEmpty(){
        manager.removeAllEpic();
        manager.removeSubtaskId(999);
        Assertions.assertEquals(0, manager.getAllSubtask().size());
    }

    @Test
    void shouldReturnEmptyListWhenGetSubtaskByEpicIdIsEmpty() {
        Epic epic = addNewEpic();
        manager.addNewEpic(epic);
        List<Subtask> subtasks = manager.getIdSubtask(epic.getId());
        Assertions.assertTrue(subtasks.isEmpty());
    }

    @Test
    public void shouldReturnEmptyListTasksIfNoTasks() {
        Assertions.assertTrue(manager.getAllTask().isEmpty());
    }

    @Test
    public void shouldReturnEmptyListEpicsIfNoEpics() {
        Assertions.assertTrue(manager.getAllEpic().isEmpty());
    }

    @Test
    public void shouldReturnEmptyListSubtasksIfNoSubtasks() {
        Assertions.assertTrue(manager.getAllSubtask().isEmpty());
    }

    @Test
    public void shouldReturnNullIfTaskDoesNotExist() {
        Assertions.assertNull(manager.getTask(999));
    }

    @Test
    public void shouldReturnNullIfEpicDoesNotExist() {
        Assertions.assertNull(manager.getEpic(999));
    }

    @Test
    public void shouldReturnNullIfSubtaskDoesNotExist() {
        Assertions.assertNull(manager.getSubtask(999));
    }

    @Test
    public void shouldReturnEmptyHistory() {
        Assertions.assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }

    @Test
    public void shouldReturnEmptyHistoryIfTasksNotExist() {
        manager.getTask(999);
        manager.getSubtask(999);
        manager.getEpic(999);
        Assertions.assertTrue(manager.getHistory().isEmpty());
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
        Assertions.assertEquals(2, list.size());
        Assertions.assertTrue(list.contains(subtask));
        Assertions.assertTrue(list.contains(epic));
    }
}