package Metods;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    final private int historyLength = 10; // длина списка истории просмотров
    protected List<Integer> historyTasks = new ArrayList<>(historyLength);

    public void add(int id) { //добавление просмотренной задачи в список истории просмотров
        if (historyTasks.size() == historyLength) {
            historyTasks.remove(0);
        }
            historyTasks.add(id);
        }

    @Override
    public List<Integer> getHistory() {
        return historyTasks;
    } // получение списка id истоиии просмотров
}
