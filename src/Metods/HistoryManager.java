package Metods;

import java.util.List;

public interface HistoryManager {
    void add(int id); //должен помечать задачи как просмотренные

    List<Integer> getHistory(); //возвращать их список.

}
