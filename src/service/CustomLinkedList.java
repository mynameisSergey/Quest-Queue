package service;

import model.Node;
import model.Task;

import java.util.ArrayList;
import java.util.List;

class CustomLinkedList<T> { // связный список
    private Node<T> head;
    private Node<T> tail;

    void linkLast(Node<T> node) { // добавление узла
        final Node<T> oldTail = tail;
        node.prev = oldTail;
        tail = node;
        if (oldTail == null) head = node;
        else oldTail.next = node;
    }

    public List<Task> getTasks() { // возвращает список задач из связного списка
        List<Task> tasks = new ArrayList<>();
        Node<T> node = head;
        while (node != null) {
            tasks.add((Task) node.data);
            node = node.next;
        }
        return tasks;
    }

    void removeNode(Node<T> node) { // удаляет узел из списка просмотренных задач
        Node<T> prevNode = node.prev;
        Node<T> nextNode = node.next;
        if (prevNode == null && nextNode == null) { // пустой лист
            head = null;
            tail = null;
        }
        if (prevNode == null && nextNode != null) { // голова
            head = nextNode;
            nextNode.prev = null;
        }
        if (prevNode != null && nextNode == null) { // хвост
            tail = prevNode;
            prevNode.next = null;
        }
        if (prevNode != null && nextNode != null) {
            prevNode.next = nextNode;
            nextNode.prev = prevNode;
        }
    }
}
