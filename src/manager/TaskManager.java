package manager;

import java.util.List;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public interface TaskManager {

    List<Task> getHistory();

    Task getTask(int id); // получение задачи по айди

    List<Task> getAllTask();


    Task addNewTask(Task task);  // добавление задачи в мапу


    void removeAllTask(); // удаление всех задач из мапы


    void removeTaskId(int id);  // удаление задачи из мапы по айди


    void updateTask(Task task);  // обновление задачи


    // РАБОТА С ЭПИКАМИ

    Epic getEpic(int id); // получение эпика по айди


    List<Epic> getAllEpic(); // получение списка всех эпиков


    Epic addNewEpic(Epic epic); // дробавляем новый эпик


    void removeAllEpic(); // удаление всех эпиков из мапы


    default void removeEpicId(int id) // удаление эпика из мапы по айди
    {
    }

    void updateEpic(Epic epic); // обновление эпика


    void updateStatusEpic(Epic epic);

    List<Subtask> getIdSubtask(int id); // Получение списка всех подзадач определённого эпика.


    // РАБОТА С ПОДЗАДАЧАМИ

    Subtask addNewSubtask(Subtask subtask); // добавляем новые подзадачи


    Subtask getSubtask(int id); // получение подзадачи по айди


    List<Subtask> getAllSubtask(); // получение списка всех подзадач


    void removeAllSubtask(); // удаление всех подзадач из мапы

    default void removeSubtaskId(int id) // удаление подзадачи из мапы по айди
    {
    }


    void removeAllSubtaskByEpic(Epic epic);

    void updateSubtask(Subtask subtask); // обновление подзадачи


}
