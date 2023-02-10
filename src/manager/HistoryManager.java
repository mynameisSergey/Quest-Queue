package manager;

import task.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task); //должен помечать задачи как просмотренные

    void remove(int id); // удаляет задачи из просмотра

    List<Task> getHistory(); //возвращать их список.

    class ManagerSaveException extends RuntimeException {

        public ManagerSaveException(String message) {
            super(message);
        }
    }
}
