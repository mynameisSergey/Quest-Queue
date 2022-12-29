import manager.HistoryManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import static tasks.Status.NEW;
import static tasks.Status.DONE;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();
        System.out.println("создаем таски");
        Task taskFirst = new Task("таск1", "опиcание1", NEW);
        manager.addNewTask(taskFirst);
        Task taskSecond = new Task("таск2", "описание2", NEW);
        manager.addNewTask(taskSecond);

        System.out.println("Эпики с сабстасками");
        Epic epicFirst = new Epic("эпик1", "эпик первый с двумя сабами", NEW);
        manager.addNewEpic(epicFirst);
        Subtask subtaskFirst = new Subtask("Сабстак1", "Сабстак1 первого эпика", epicFirst.getId(), NEW);
        historyManager.add(subtaskFirst);

        manager.addNewSubtask(subtaskFirst);
        Subtask subtaskSecond = new Subtask("Сабстак2", "Сабстак2 первого эпика", epicFirst.getId(), NEW);
        manager.addNewSubtask(subtaskSecond);
        Subtask subtaskFhird = new Subtask("Сабстак3", "Сабстак3 первого эпика", epicFirst.getId(), NEW);
        manager.addNewSubtask(subtaskFhird);
        Epic epicSecond = new Epic("эпик2", "эпик второй с одним сабом", NEW);
        manager.addNewEpic(epicSecond);
        System.out.println(manager.getAllEpic()); // печать списка эпиков
        System.out.println(manager.getAllSubtask()); // печать списка подзадач
        System.out.println(manager.getAllTask()); //печать списка задач
        System.out.println("Обновление объекта и статуса эпика");
        Subtask subtaskThird = new Subtask("Саб1", "Саб1 первого эпика", 3, DONE); // создание подзадачи
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













