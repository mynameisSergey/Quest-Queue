package service;

import model.Node;

import java.util.ArrayList;
import java.util.List;

class CustomLinkedList<T> { // связный список
    private Node<T> head;
    private Node<T> tail;

    public CustomLinkedList() { // Конструктор
        this.head = null;
        this.tail = null;
    }

    void linkLast(Node<T> node) { // добавление узла
        if (node == null) {
            throw new IllegalArgumentException("Node cannot be null");
        }

        final Node<T> oldTail = tail;
        node.setPrev(oldTail);
        tail = node;
        if (oldTail == null) {
            head = node; // если список был пустым
        } else {
            oldTail.setNext(node); // соединяем старый хвост с новым узлом
        }
    }

    public List<T> getTasks() { // возвращает список задач из связного списка
        List<T> tasks = new ArrayList<>();
        Node<T> node = head;
        while (node != null) {
            tasks.add(node.getData());
            node = node.getNext();
        }
        return tasks;
    }

    void removeNode(Node<T> node) {
        if (node == null) {
            throw new IllegalArgumentException("Node cannot be null");
        }

        Node<T> prevNode = node.getPrev();
        Node<T> nextNode = node.getNext();

        if (prevNode == null && nextNode == null) { // пустой лист
            head = null;
            tail = null;
        } else if (prevNode == null) { // голова
            head = nextNode;
            if (nextNode != null) {
                nextNode.setPrev(null);
            }
        } else if (nextNode == null) { // хвост
            tail = prevNode;
            prevNode.setNext(null);
        } else { // узел в середине
            prevNode.setNext(nextNode);
            nextNode.setPrev(prevNode);
        }
    }

    public boolean isEmpty() {
        return head == null;
    }
} // удаляет узел из списка