import Metods.HistoryManager;
import Metods.InMemoryTaskManager;
import Metods.Managers;
import Metods.TaskManager;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();
        System.out.println("создаем таски");
        Task taskFirst = new Task("таск1", "опиcание1", "NEW");
        manager.addNewTask(taskFirst);
        manager.removeAllTask();
        Task taskSecond = new Task("таск2", "описание2", "NEW");
        manager.addNewTask(taskSecond);

        System.out.println(manager.getTask(2));

        System.out.println("Эпики с сабстасками");
        Epic epicFirst = new Epic("эпик1", "эпик первый с двумя сабами", "NEW");
        manager.addNewEpic(epicFirst);
        Subtask subtaskFirst = new Subtask("Сабстак1", "Сабстак1 первого эпика", epicFirst.getId(), "NEW");
        manager.addNewSubtask(subtaskFirst);
        Subtask subtaskSecond = new Subtask("Сабстак2", "Сабстак2 первого эпика", epicFirst.getId(), "NEW");
        manager.addNewSubtask(subtaskSecond);

        Epic epicSecond = new Epic("эпик2", "эпик второй с одним сабом", "NEW");
        manager.addNewEpic(epicSecond);
        Subtask subtaskThree = new Subtask("Сабстак3", "Сабстак3 второго эпика", epicSecond.getId(), "NEW");
        manager.addNewSubtask(subtaskThree);

        System.out.println(manager.getAllEpic()); // печать списка эпиков
        System.out.println(manager.getAllSubtask()); // печать списка подзадач
        System.out.println(manager.getAllTask()); //печать списка задач
        System.out.println(manager.getEpic(3)); // печать первого эпика по айди
        System.out.println(manager.getEpic(6)); // печать второго эпика по айди
        System.out.println(subtaskFirst);

        System.out.println("Обновление объекта и статуса эпика");
        Subtask subtaskThird = new Subtask("Саб1", "Саб1 первого эпика", 3, "DONE"); // создание подзадачи
        subtaskThird.setId(4);
        manager.updateSubtask(subtaskThird);
        System.out.println(manager.getSubtask(4));
        manager.getStatusEpic(3);
        System.out.println(manager.getEpic(3));

        manager.removeTaskId(2);
        manager.removeEpicId(3);

        manager.getEpic(6);
        manager.getSubtask(4);
        manager.getTask(2);
        manager.getHistory();
    }
}













