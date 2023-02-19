package manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static task.TasksStatus.Status.NEW;
import static task.TasksType.TasksType.EPIC;
import static task.TasksType.TasksType.TASK;


class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private Path path = Path.of("resourses/list.csv");
    File file = new File(String.valueOf(path));

    @BeforeEach
    public void beforeEach() {
        manager = new FileBackedTasksManager("resourses/list.csv");
    }

    @AfterEach
    public void afterEach() {
        try {
            Files.delete(path);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void shouldCorrectlySaveAndLoad() {
        Task task = new Task(1, TASK, "Таск1", NEW,  "описание1",  Instant.now(), 0);
        manager.addNewTask(task);
        Epic epic = new Epic(3, EPIC, "эпик1", NEW,  "эпик первый с двумя сабами", Instant.now(), 0);
        manager.addNewEpic(epic);

        FileBackedTasksManager fileManager = new FileBackedTasksManager(file.getPath());
        fileManager.loadFromFile(new File("resourses/list.csv"));
        Assertions.assertEquals(List.of(task), manager.getAllTask());
        Assertions.assertEquals(List.of(epic), manager.getAllEpic());
    }

    @Test
    public void shouldSaveAndLoadEmptyTasksEpicsSubtasks() {
        FileBackedTasksManager fileManager = new FileBackedTasksManager(file.getPath());
        fileManager.save();
        fileManager.loadFromFile(new File("resourses/list.csv"));
        Assertions.assertEquals(Collections.EMPTY_LIST, manager.getAllTask());
        Assertions.assertEquals(Collections.EMPTY_LIST, manager.getAllEpic());
        Assertions.assertEquals(Collections.EMPTY_LIST, manager.getAllSubtask());
    }

    @Test
    public void shouldSaveAndLoadEmptyHistory() {
        FileBackedTasksManager fileManager = new FileBackedTasksManager(file.getPath());
        fileManager.save();
        fileManager.loadFromFile(new File("resourses/list.csv"));
        Assertions.assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }
}