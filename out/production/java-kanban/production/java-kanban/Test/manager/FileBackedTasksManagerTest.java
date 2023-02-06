package manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tasks.Epic;
import tasks.Task;
import tasks.Status;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static tasks.Status.NEW;
import static tasks.TasksType.EPIC;
import static tasks.TasksType.TASK;

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
        assertEquals(List.of(task), fileManager.getAllTask());
        assertEquals(List.of(epic), fileManager.getAllEpic());
    }

    @Test
    public void shouldSaveAndLoadEmptyTasksEpicsSubtasks() {
        FileBackedTasksManager fileManager = new FileBackedTasksManager(file.getPath());
        fileManager.save();
        fileManager.loadFromFile(new File("resourses/list.csv"));
        assertEquals(Collections.EMPTY_LIST, manager.getAllTask());
        assertEquals(Collections.EMPTY_LIST, manager.getAllEpic());
        assertEquals(Collections.EMPTY_LIST, manager.getAllSubtask());
    }

    @Test
    public void shouldSaveAndLoadEmptyHistory() {
        FileBackedTasksManager fileManager = new FileBackedTasksManager(file.getPath());
        fileManager.save();
        fileManager.loadFromFile(new File("resourses/list.csv"));
        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }
}