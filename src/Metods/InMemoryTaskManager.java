package Metods;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;


public class InMemoryTaskManager extends InMemoryHistoryManager implements TaskManager {
    HistoryManager historyManager = new InMemoryHistoryManager();
    public ArrayList<Integer> historyTasks = new ArrayList<>();
    public int id = 0;

    // РАБОТА С ЗАДАЧАМИ
    public HashMap<Integer, Task> allTasks = new HashMap<>(); // мапа с задачами

    @Override
    public List<Integer> getHistory() {
        return historyTasks;
    }

    public ArrayList<Task> getTaskViewHistory() {    //Получение списка истории просмотренных задач
        ArrayList<Task> taskViewHistory = new ArrayList<>();
        for (Integer taskId : historyManager.getHistory()) {
            if (allTasks.containsKey(id)) {
                taskViewHistory.add(allTasks.get(id));
            }
            if (allEpics.containsKey(taskId)) {
                taskViewHistory.add(allEpics.get(id));
            }
            if (allSubtasks.containsKey(taskId)) {
                taskViewHistory.add(allSubtasks.get(id));
            }
        }
        return taskViewHistory;
    }


    @Override
    public Task getTask(int id) { // получение задачи по айди
        add(id);
        return allTasks.get(id);
    }

    @Override
    public ArrayList<Task> getAllTask() { // получение списка всех задач
        return new ArrayList<>(allTasks.values());
    }

    @Override
    public void addNewTask(Task task) { // добавление задачи в мапу
        task.setId(++id);
        allTasks.put(id, task);
    }

    @Override
    public void removeAllTask() { // удаление всех задач из мапы
        allTasks.clear();
    }

    @Override
    public void removeTaskId(int id) { // удаление задачи из мапы по айди
        allTasks.remove(id);
    }

    @Override
    public void updateTask(Task task) { // обновление задачи
        if (allTasks.containsKey(task.getId())) {
            allTasks.put(task.getId(), task);
        }
    }


    // РАБОТА С ЭПИКАМИ
    public HashMap<Integer, Epic> allEpics = new HashMap<>(); // мапа с эпиками

    @Override
    public Epic getEpic(int id) { // получение эпика по айди
        add(id);
        return allEpics.get(id);
    }


    @Override
    public ArrayList<Epic> getAllEpic() { // получение списка всех эпиков
        return new ArrayList<>(allEpics.values());
    }

    @Override
    public void addNewEpic(Epic epic) { // дробавляем новый эпик
        epic.setId(++id);
        allEpics.put(id, epic);
    }

    @Override
    public void removeAllEpic() { // удаление всех эпиков из мапы
        allSubtasks.clear();
        allEpics.clear();
    }

    @Override
    public void removeEpicId(int id) { // удаление эпика из мапы по айди
        Epic epic = allEpics.remove(id);
        for (Integer subtaskId : epic.getSubTaskId()) {
            allSubtasks.remove(subtaskId);
        }
    }

    @Override
    public void updateEpic(Epic epic) { // обновление эпика
        if (allEpics.containsKey(epic.getId())) {
            allEpics.put(epic.getId(), epic);
        }
    }

    @Override
    public ArrayList<Subtask> getIdSubtask(int id) { // Получение списка всех подзадач определённого эпика.
        ArrayList<Subtask> subtasksEpic = new ArrayList<>();
        for (Integer idSub : allEpics.get(id).getSubTaskId()) {
            subtasksEpic.add(allSubtasks.get(idSub));
        }
        return subtasksEpic;
    }

    @Override
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

    @Override
    public void addNewSubtask(Subtask subtask) { // добавляем новые подзадачи
        if (allEpics.containsKey(subtask.getEpicId())) {
            subtask.setId(++id);
            allSubtasks.put(subtask.getId(), subtask);
            allEpics.get(subtask.getEpicId()).getSubTaskId().add(subtask.getId());
        }
    }

    @Override
    public Subtask getSubtask(int id) {// получение подзадачи по айди
        add(id);
        return allSubtasks.get(id);
    }

    @Override
    public ArrayList<Subtask> getAllSubtask() { // получение списка всех подзадач
        return new ArrayList<>(allSubtasks.values());
    }

    @Override
    public void removeAllSubtask() { // удаление всех подзадач из мапы
        allSubtasks.clear();
        for (Epic id : allEpics.values()) {
            id.getSubTaskId().clear();
            getStatusEpic(id.getId());
        }
    }

    @Override
    public void removeSubtaskId(int id) { // удаление подзадачи из мапы по айди
        Subtask subtask = allSubtasks.remove(id);
        allSubtasks.remove(id);
        allEpics.get(subtask.getEpicId()).removeSubtask(id);
        getStatusEpic(subtask.getEpicId());
    }

    @Override
    public void updateSubtask(Subtask subtask) { // обновление подзадачи
        if (allSubtasks.containsKey(subtask.getId()) && (allEpics.containsKey(subtask.getEpicId()))) {
            allSubtasks.put(subtask.getId(), subtask);
            getStatusEpic(allSubtasks.get(subtask.getId()).getEpicId());
        }
    }
}

























