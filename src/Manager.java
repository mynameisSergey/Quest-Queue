import java.util.HashMap;
import java.util.ArrayList;


public class Manager {

    public int id = 0;

    // РАБОТА С ЗАДАЧАМИ
    public HashMap<Integer, Task> allTask = new HashMap<>(); // мапа с задачами

    public Task getTask(int id) { // получение задачи по айди

        return allTask.get(id);
    }

    public ArrayList<Task> getAllTask() { // получение списка всех задач
        return new ArrayList<>(allTask.values());
    }

    public void addNewTask(Task task) { // добавление задачи в мапу
        task.setId(++id);

        allTask.put(id, task);
    }

    public void removeAllTask() { // удаление всех задач из мапы
        allTask.clear();
    }

    public void removeTaskId(int id) { // удаление задачи из мапы по айди
        allTask.remove(id);
    }


    public void updateTask(Task task) { // обновление задачи
        allTask.put(task.getId(), task);
    }


    // РАБОТА С ЭПИКАМИ
    public HashMap<Integer, Epic> allEpic = new HashMap<>(); // мапа с эпиками


    public Epic getEpic(int id) { // получение эпика по айди
        return allEpic.get(id);
    }

    public ArrayList<Epic> getAllEpic() { // получение списка всех эпиков
        return new ArrayList<>(allEpic.values());
    }

    public void addNewEpic(Epic epic) { // дробавляем новый эпик
        epic.setId(++id);
        allEpic.put(id, epic);
    }

    public void removeAllEpic() { // удаление всех эпиков из мапы

        allEpic.clear();
    }

    public void removeEpicId(int id) { // удаление эпика из мапы по айди
        allEpic.remove(id);
    }

    public void updateEpic(Epic epic) { // обновление эпика
        allEpic.put(epic.getId(), epic);
    }


    public ArrayList<Subtask> getIdSubtask(int id) { // Получение списка всех подзадач определённого эпика.
        ArrayList<Subtask> subtasksEpic = new ArrayList<>();
        for (Integer idSub : allEpic.get(id).getSubTaskId()) {
            subtasksEpic.add(allSubtask.get(idSub));
        }
        return subtasksEpic;
    }

    public void getStatusEpic(int id) { // Управление статусами эпиков
        int statusSubNew = 0;
        int statusSubDone = 0;

        for (Integer idSub : allEpic.get(id).getSubTaskId()) {
            if (allSubtask.get(idSub).getStatus().equals("NEW")) {
                statusSubNew++;
            } else if (allSubtask.get(idSub).getStatus().equals("DONE")) {
                statusSubDone++;
            }
            if ((allEpic.get(id).getSubTaskId() == null) || (statusSubNew == allEpic.get(id).getSubTaskId().size())) {
                allEpic.get(id).setStatus("NEW");
            } else if (statusSubDone == allEpic.get(id).getSubTaskId().size()) {
                allEpic.get(id).setStatus("DONE");
            } else {
                allEpic.get(id).setStatus("IN_PROGRESS");
            }
        }
    }


    // РАБОТА С ПОДЗАДАЧАМИ
    HashMap<Integer, Subtask> allSubtask = new HashMap<>(); // мапа с подзадачами

    public void addNewSubtask(Subtask subtask) { // добавляем новые подзадачи
        if (allEpic.containsKey(subtask.getEpicId())) {
            subtask.setId(++id);
            allSubtask.put(subtask.getId(), subtask);
            allEpic.get(subtask.getEpicId()).getSubTaskId().add(subtask.getId());
        }
    }

    public Subtask getSubtask(int id) { // получение подзадачи по айди
        return allSubtask.get(id);
    }

    public ArrayList<Subtask> getAllSubtask() { // получение списка всех подзадач
        return new ArrayList<>(allSubtask.values());
    }

    public void removeAllSubtask() { // удаление всех подзадач из мапы
        allSubtask.clear();
    }

    public void removeSubtaskId(int id) { // удаление подзадачи из мапы по айди
        allSubtask.remove(id);
    }

    public void updateSubtask(Subtask subtask) { // обновление подзадачи
        allSubtask.put(subtask.getId(), subtask);
    }


}



























