package task;

import org.junit.jupiter.api.Test;
import task.TasksStatus.Status;

import java.time.Instant;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static task.TasksStatus.Status.*;
import static task.TasksType.TasksType.EPIC;
import static task.TasksType.TasksType.SUBTASK;


class EpicTest {
        Epic epic;

        @Test
        void emptyEpic() {
            epic = new Epic(12, EPIC, "эпик12", NEW,  "эпик первый с двумя сабами", Instant.now(), 0);
            assertEquals(Status.NEW, epic.getStatus());
        }

        @Test
        void newEpic() {
            epic = new Epic(12, EPIC, "эпик13", NEW,  "эпик первый с двумя сабами", Instant.now(), 0);
            HashMap<Integer, Subtask> subtasks = new HashMap<>();
            subtasks.put(2, new Subtask(2, SUBTASK, "Сабстак1", NEW,  "Сабстак1 первого эпика", Instant.now(), 0, epic.getId()));
            subtasks.put(3, new Subtask(3, SUBTASK, "Сабстак", NEW,  "Сабстак", Instant.now(), 0, epic.getId()));
            subtasks.put(4, new Subtask(4, SUBTASK, "Сабстак", NEW,  "Сабстак", Instant.now(), 0, epic.getId()));
            epic.addSubtask(2);
            epic.addSubtask(3); // 4 сабтаску сознательно не добавляем
            epic.calculateStatus(subtasks);
            assertEquals(Status.NEW, epic.getStatus());
        }

        @Test
        void doneEpic() {
            epic = new Epic(12, EPIC, "эпик13", NEW,  "эпик первый с двумя сабами", Instant.now(), 0);
            HashMap<Integer, Subtask> subtasks = new HashMap<>();
            subtasks.put(2, new Subtask(2, SUBTASK, "Сабстак1", NEW,  "Сабстак1 первого эпика", Instant.now(), 0, epic.getId()));
            subtasks.put(3, new Subtask(3, SUBTASK, "Сабстак", DONE,  "Сабстак", Instant.now(), 0, epic.getId()));
            subtasks.put(4, new Subtask(4, SUBTASK, "Сабстак", DONE,  "Сабстак", Instant.now(), 0, epic.getId()));
            epic.addSubtask(3);
            epic.addSubtask(4); // 2 сабтаску сознательно не добавляем
            epic.calculateStatus(subtasks);
            assertEquals(DONE, epic.getStatus());
        }

        @Test
        void newDoneEpic() {
            epic = new Epic(12, EPIC, "эпик13", NEW,  "эпик первый с двумя сабами", Instant.now(), 0);
            HashMap<Integer, Subtask> subtasks = new HashMap<>();
            subtasks.put(2, new Subtask(2, SUBTASK, "Сабстак1", NEW,  "Сабстак1 первого эпика", Instant.now(), 0, epic.getId()));
            subtasks.put(3, new Subtask(3, SUBTASK, "Сабстак", DONE,  "Сабстак", Instant.now(), 0, epic.getId()));
            subtasks.put(4, new Subtask(4, SUBTASK, "Сабстак", DONE,  "Сабстак", Instant.now(), 0, epic.getId()));
            epic.addSubtask(3);
            epic.addSubtask(2); // 4 сабтаску сознательно не добавляем
            epic.calculateStatus(subtasks);
            assertEquals(IN_PROGRESS, epic.getStatus());
        }

        @Test
        void inProgressEpic() {
            epic = new Epic(12, EPIC, "эпик13", NEW,  "эпик первый с двумя сабами", Instant.now(), 0);
            HashMap<Integer, Subtask> subtasks = new HashMap<>();
            subtasks.put(2, new Subtask(2, SUBTASK, "Сабстак1", IN_PROGRESS,  "Сабстак1 первого эпика", Instant.now(), 0, epic.getId()));
            subtasks.put(3, new Subtask(3, SUBTASK, "Сабстак", IN_PROGRESS,  "Сабстак", Instant.now(), 0, epic.getId()));
            subtasks.put(4, new Subtask(4, SUBTASK, "Сабстак", NEW,  "Сабстак", Instant.now(), 0, epic.getId()));
            epic.addSubtask(3);
            epic.addSubtask(2); // 4 сабтаску сознательно не добавляем
            epic.calculateStatus(subtasks);
            assertEquals(IN_PROGRESS, epic.getStatus());
        }
    }
