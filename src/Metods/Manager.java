package Metods;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.util.HashMap;
import java.util.ArrayList;


public class Manager {

    public int id = 0;

    // РАБОТА С ЗАДАЧАМИ
    public HashMap<Integer, Task> allTasks = new HashMap<>(); // мапа с задачами

    public Task getTask(int id) { // получение задачи по айди

        return allTasks.get(id);
    }

    public ArrayList<Task> getAllTask() { // получение списка всех задач
        return new ArrayList<>(allTasks.values());
    }

    public void addNewTask(Task task) { // добавление задачи в мапу
        task.setId(++id);

        allTasks.put(id, task);
    }

    public void removeAllTask() { // удаление всех задач из мапы
        allTasks.clear();
    }

    public void removeTaskId(int id) { // удаление задачи из мапы по айди
        allTasks.remove(id);
    }


    public void updateTask(Task task) { // обновление задачи
        if (allTasks.containsKey(task.getId())) {
            allTasks.put(task.getId(), task);
        }
    }


    // РАБОТА С ЭПИКАМИ
    public HashMap<Integer, Epic> allEpics = new HashMap<>(); // мапа с эпиками


    public Epic getEpic(int id) { // получение эпика по айди
        return allEpics.get(id);
    }

    public ArrayList<Epic> getAllEpic() { // получение списка всех эпиков
        return new ArrayList<>(allEpics.values());
    }

    public void addNewEpic(Epic epic) { // дробавляем новый эпик
        epic.setId(++id);
        allEpics.put(id, epic);
    }

    public void removeAllEpic() { // удаление всех эпиков из мапы
        allSubtasks.clear();
        allEpics.clear();
    }

    public void removeEpicId(int id) { // удаление эпика из мапы по айди
        ArrayList<Integer> number = new ArrayList<>();
        Epic epic = allEpics.remove(id);
        for (Subtask idSub : allSubtasks.values()) {
            number.add(idSub.getId());
        }
        for (Integer integer : number) {
            if (epic.getSubTaskId().contains(integer)) {
                allSubtasks.remove(integer);
            }
            allEpics.remove(id);
        }
    }




    public void updateEpic(Epic epic) { // обновление эпика
        if (allEpics.containsKey(epic.getId())) {
            allEpics.put(epic.getId(), epic);
        }
    }


    public ArrayList<Subtask> getIdSubtask(int id) { // Получение списка всех подзадач определённого эпика.
        ArrayList<Subtask> subtasksEpic = new ArrayList<>();
        for (Integer idSub : allEpics.get(id).getSubTaskId()) {
            subtasksEpic.add(allSubtasks.get(idSub));
        }
        return subtasksEpic;
    }

    public void getStatusEpic(int id) { // Управление статусами эпиков
        int statusSubNew = 0;
        int statusSubDone = 0;

        for (Integer idSub : allEpics.get(id).getSubTaskId()) {
            if (allSubtasks.get(idSub).getStatus().equals("NEW")) {
                statusSubNew++;
            } else if (allSubtasks.get(idSub).getStatus().equals("DONE")) {
                statusSubDone++;
            }
            if ((allEpics.get(id).getSubTaskId() == null) || (statusSubNew == allEpics.get(id).getSubTaskId().size())) {
                allEpics.get(id).setStatus("NEW");
            } else if (statusSubDone == allEpics.get(id).getSubTaskId().size()) {
                allEpics.get(id).setStatus("DONE");
            } else {
                allEpics.get(id).setStatus("IN_PROGRESS");
            }
        }
    }


    // РАБОТА С ПОДЗАДАЧАМИ
    HashMap<Integer, Subtask> allSubtasks = new HashMap<>(); // мапа с подзадачами

    public void addNewSubtask(Subtask subtask) { // добавляем новые подзадачи
        if (allEpics.containsKey(subtask.getEpicId())) {
            subtask.setId(++id);
            allSubtasks.put(subtask.getId(), subtask);
            allEpics.get(subtask.getEpicId()).getSubTaskId().add(subtask.getId());
        }
    }

    public Subtask getSubtask(int id) { // получение подзадачи по айди
        return allSubtasks.get(id);
    }

    public ArrayList<Subtask> getAllSubtask() { // получение списка всех подзадач
        return new ArrayList<>(allSubtasks.values());
    }

    public void removeAllSubtask() { // удаление всех подзадач из мапы
        allSubtasks.clear();
        for (Epic id : allEpics.values()) {
            id.getSubTaskId().clear();
            getStatusEpic(id.getId());
        }
    }


    public void removeSubtaskId(int id) { // удаление подзадачи из мапы по айди
        Subtask subtask = allSubtasks.remove(id);
        allSubtasks.remove(id);
        allEpics.get(subtask.getEpicId()).getSubTaskId().remove(id);
        getStatusEpic(subtask.getEpicId());
    }

    public void updateSubtask(Subtask subtask) { // обновление подзадачи
        if (allSubtasks.containsKey(subtask.getId()) && (allEpics.containsKey(subtask.getEpicId()))) {
            allSubtasks.put(subtask.getId(), subtask);
            getStatusEpic(allSubtasks.get(subtask.getId()).getEpicId());
        }
    }
}


























