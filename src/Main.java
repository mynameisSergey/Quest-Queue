import manager.HistoryManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Instant;

import static tasks.Status.NEW;
import static tasks.Status.DONE;
import static tasks.TasksType.*;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();
        System.out.println("создаем таски");
        Task taskFirst = new Task(1, TASK, "Таск1", NEW,  "описание1",  Instant.now(), 0);
        manager.addNewTask(taskFirst);
        Task taskSecond = new Task(9, TASK, "Таск1", NEW,  "описание1",  Instant.now(), 0);
        manager.addNewTask(taskSecond);

        System.out.println("Эпики с сабстасками");
        Epic epicFirst = new Epic(3, EPIC, "эпик1", NEW,  "эпик первый с двумя сабами", Instant.now(), 0);
        manager.addNewEpic(epicFirst);
        Subtask subtaskFirst = new Subtask(4, SUBTASK, "Сабстак1", NEW,  "Сабстак1 первого эпика", Instant.now(), 0, epicFirst.getId());
        historyManager.add(subtaskFirst);

        manager.addNewSubtask(subtaskFirst);
        Subtask subtaskSecond = new Subtask(5, SUBTASK, "Сабстак1", NEW,  "Сабстак2 первого эпика", Instant.now(), 0, epicFirst.getId());
        manager.addNewSubtask(subtaskSecond);
        Subtask subtaskFhird = new Subtask(6, SUBTASK, "Сабстак1", NEW,  "Сабстак3 первого эпика", Instant.now(), 0, epicFirst.getId());
        manager.addNewSubtask(subtaskFhird);
        Epic epicSecond = new Epic(10, EPIC, "эпик1", NEW,  "эпик первый с двумя сабами", Instant.now(), 0);
        manager.addNewEpic(epicSecond);
        System.out.println(manager.getAllEpic()); // печать списка эпиков
        System.out.println(manager.getAllSubtask()); // печать списка подзадач
        System.out.println(manager.getAllTask()); //печать списка задач
        System.out.println("Обновление объекта и статуса эпика");
        Subtask subtaskThird = new Subtask(8, SUBTASK, "Сабстак1", NEW,  "Сабстак1 первого эпика", Instant.now(), 0, epicFirst.getId()); // создание подзадачи
        subtaskThird.setId(4);
        manager.updateSubtask(subtaskThird);
        System.out.println(manager.getSubtask(4));
        manager.getStatusEpic(3);
        System.out.println(manager.getEpic(3));
        System.out.println("ddddddddddd");
        manager.getEpic(3);
        System.out.println(manager.getHistory());
        // manager.getSubtask(4);
        System.out.println(manager.getHistory());
        manager.getTask(1);
        System.out.println(manager.getHistory());
        manager.getEpic(7);
        System.out.println(manager.getHistory());
        manager.getTask(2);
        manager.getEpic(3);
        System.out.println(manager.getHistory());
        manager.getEpic(7);
        manager.removeTaskId(1);

        System.out.println("ddddddddddd");

        System.out.println("ddddddddddd");
        System.out.println(manager.getHistory());
        manager.removeTaskId(1);
        System.out.println(manager.getHistory());
    }
}













