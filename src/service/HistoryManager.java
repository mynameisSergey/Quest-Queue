package service;

import model.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task); // помечает задачи просмотренными


    void remove(int id); //удаление задачи по идентификатору


    List<Task> getHistory(); // возвращает список просмотренных задач
}
