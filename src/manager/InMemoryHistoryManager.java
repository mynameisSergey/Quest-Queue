package manager;

import tasks.Task;
import tasks.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    CustomLinkedList<Task> list = new CustomLinkedList<>();
    HashMap<Integer, Node> containerLink = new HashMap<>();
    List<Task> tasks = new ArrayList<>();

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
    public void remove(int id) { // удаление задачи из мапы, а потом ее удаление из истории просмотров
        if (!containerLink.isEmpty()) {
            list.removeNode(containerLink.get(id));
            containerLink.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return list.getTasks();
    } // получение истории просмотров
}

class CustomLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;

    void linkLast(Node node) {
        final Node<T> oldTail = tail;
        node.prev = oldTail;
        tail = node;
        if (oldTail == null) {
            head = node;
        } else {
            oldTail.next = node;
        }
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node node = head;
        while (node != null) {
            tasks.add((Task) node.data);
            node = node.next;
        }
        return tasks;
    }

    void removeNode(Node node) {
        Node prevNode = node.prev;
        Node nextNode = node.next;
        if (prevNode == null && nextNode == null) {
            head = null;
            tail = null;
            node = null;
        }
        if (prevNode == null && nextNode != null) {
            head = nextNode;
            nextNode.prev = null;
        }
        if (prevNode != null && nextNode == null) {
            tail = prevNode;
            prevNode.next = null;
        }
        if (prevNode != null && nextNode != null) {
            prevNode.next = nextNode;
            nextNode.prev = prevNode;
        }


    }

}
