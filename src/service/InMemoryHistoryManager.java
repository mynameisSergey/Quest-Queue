package service;

import model.Node;
import model.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    CustomLinkedList<Task> list = new CustomLinkedList<>(); // список истории просмотров
    Map<Integer, Node<Task>> containerLink = new HashMap<>(); // история просмотров

    @Override
    public void add(Task task) { //добавление просмотренной задачи в список истории просмотров
        Node<Task> node = new Node<>(null, task, null);
        if (containerLink.containsKey(task.getId())) {
            list.removeNode(containerLink.get(task.getId()));
        }
        list.linkLast(node);
        containerLink.put(task.getId(), node);
    }

    @Override
    public void remove(int id) { // удаление задачи из мапы и ее удаление из истории просмотров
        if (!containerLink.isEmpty()) {
            list.removeNode(containerLink.get(id));
            containerLink.remove(id);
        }
    }

    @Override
    public List<Task> getHistory()
    { // возвращает просмотренные задачи
        return list.getTasks();
    }
}









































