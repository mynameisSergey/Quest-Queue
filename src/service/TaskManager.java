package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    List<Task> getHistory();

//     РАБОТА С ЗАДАЧАМИ

    ArrayList<Task> getArrayTask(); // получение списка всех задач

    void clearMapOfTask(); // удаление всех задач

    Task getTaskOdId(int id); // получение задачи по id

    void putTask(Task task); // создание задачи

    void updateTask(Task task); // обновление задачи

    void removeTask(int id); // удаление по идентификатору

//    РАБОТА С EPIC

    ArrayList<Epic> getArrayEpic(); // возвращает список Epic из мапы

    void clearMapOfEpic(); // удаление всех Эпиков

    Epic getEpicOfId(int id); // получение Epic по id

    void putEpic(Epic epic); // создание Epic

    void updateEpic(Epic epic); // обновление Epic и изменение статуса

    void removeEpicOfId(int id); // удаление Epic по идентификатору

    void getStatusEpic(int id); // Управление статусами эпиков

    // работа с Subtask

    ArrayList<Subtask> getArraySubtask(); // возвращает список Subtask из мапы

    // ArrayList<Integer> getArraySubtaskOfId(int id); // возвращает список Subtask по Id пика

    List<Subtask> getArraySubtaskOfId(int id);

    void clearMapOfSubtask(); // удаление всех Subtasks из мапы, из списка
    //эпика и изменение статуса эпика

    void clearMapOfSubtaskFromEpic(int id); // удаление всех Subtasks из Epic

    void removeSubtaskId(int id);

    Subtask getSubtaskOfId(int id); // получение Subtask по id

    ArrayList<Subtask> getAllSubtask(); // получение списка всех подзадач

    void putSubtask(Subtask subtask); // создание Subtask и добавление в список Эпика

    void updateSubtask(Subtask subtask); // обновление Subtask

    void removeSubtask(int id); // удаление Subtask по идентификатору и обновление статуса Эпика

    List<Task> getPrioritizedTasks();
}





