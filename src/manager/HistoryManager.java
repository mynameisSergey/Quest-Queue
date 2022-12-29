package manager;

import tasks.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task); //должен помечать задачи как просмотренные

    void remove(int id); // удаляет задачи из просмотра

    List<Task> getHistory(); //возвращать их список.

}
