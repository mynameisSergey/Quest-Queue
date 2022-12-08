package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    final private int historyLength = 10; // длина списка истории просмотров
    protected List<Task> historyTasks = new ArrayList<>(historyLength);

    public void add(Task task) { //добавление просмотренной задачи в список истории просмотров
        if (historyTasks.size() == historyLength) {
            historyTasks.remove(0);
        }
            historyTasks.add(task);
        }

    @Override
    public List<Task> getHistory() {
        return historyTasks;
    } // получение истоиии просмотров
}
