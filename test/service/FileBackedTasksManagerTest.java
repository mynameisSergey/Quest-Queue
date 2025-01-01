package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TasksType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static model.StatusTasks.NEW;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    private static final Path pathToList = Path.of("history.csv");
    File file;

    @BeforeEach
    public void beforeEach() {
        manager = new FileBackedTasksManager("history.csv");
        file = new File(String.valueOf(pathToList));
    }


    @AfterEach
    public void afterEach() {
        try {
            Files.delete(pathToList);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @DisplayName("GIVEN empty list tasks " +
            "WHEN save and baked empty list " +
            "THEN get empty lists ")
    @Test
    void test1_chouldGetEmptyListsTasksEpicsSubtasks() throws IOException {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("history.csv");
        fileBackedTasksManager.save();
        fileBackedTasksManager.loadFromFile(new File("history.csv"));
        assertEquals(Collections.EMPTY_LIST, fileBackedTasksManager.getArrayTask());
        assertEquals(Collections.EMPTY_LIST, fileBackedTasksManager.getArrayEpic());
        assertEquals(Collections.EMPTY_LIST, fileBackedTasksManager.getArraySubtask());
    }

    @DisplayName("GIVEN new Epic" +
            "WHEN new Epic add list writen in file and read from file  " +
            "THEN List.of(epic) ")
    @Test
    void test2_schouldGetEmptyListsEpicsWithoutSubtasksList() throws IOException {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("history.csv");
        Epic epic = new Epic(1, TasksType.EPIC, "Тест", "Тестовый Эпик для всех", NEW, Instant.now(), 0);
        fileBackedTasksManager.putEpic(epic);
        fileBackedTasksManager.save();
        fileBackedTasksManager.loadFromFile(new File("history.csv"));
        List<Epic> list = fileBackedTasksManager.getArrayEpic();
        assertEquals(List.of(epic), list);
    }

    @DisplayName("GIVEN empty list tasks " +
            "WHEN save and baked empty list " +
            "THEN get empty Historylists ")
    @Test
    void test3_chouldGetEmptyHistoryList() throws IOException {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("history.csv");
        fileBackedTasksManager.save();
        fileBackedTasksManager.loadFromFile(new File("history.csv"));
        assertEquals(Collections.EMPTY_LIST, fileBackedTasksManager.getHistory());

    }

    @DisplayName("GIVEN a new Task, Epic, Subtask " +
            "WHEN new Task, Epic, Subtask created " +
            "THEN List.of(tasks) ")
    @Test
    void test4_correctSaveTaskEpicSubtask() throws IOException {

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("history.csv");
        Task task = new Task(1, TasksType.TASK, "Тест", "Тестовый Task для всех", NEW, Instant.now(), 0);
        Epic epic = new Epic(2, TasksType.EPIC, "Тест", "Тестовый Эпик для всех", NEW, Instant.now(), 0);
        Subtask subtask = new Subtask(3, TasksType.SUBTASK, "dsfsdf", "dsadas", NEW, Instant.now(), 0, 2);


        fileBackedTasksManager.putTask(task);
        fileBackedTasksManager.putEpic(epic);
        fileBackedTasksManager.putSubtask(subtask);
        fileBackedTasksManager.save();
        fileBackedTasksManager.loadFromFile(new File("history.csv"));
        List<Task> list1 = fileBackedTasksManager.getArrayTask();
        assertEquals(List.of(task), list1);
        List<Epic> list2 = fileBackedTasksManager.getArrayEpic();
        assertEquals(List.of(epic), list2);
        List<Subtask> list3 = fileBackedTasksManager.getArraySubtask();
        assertEquals(List.of(subtask), list3);

    }


}