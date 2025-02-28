package service;

import exception.ManagerValidateException;
import model.Epic;
import model.StatusTasks;
import model.Subtask;
import model.Task;

import java.time.Instant;
import java.util.*;

import static service.Managers.getDefaultHistory;

public class InMemoryTaskManager implements TaskManager {
    private final TreeSet<Task> sortedSet = new TreeSet<>(Comparator.comparing(Task::getStartTime,
            Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Task::getId));

    public Set<Task> prioritizedTasks = new TreeSet<>(sortedSet);
      HistoryManager historyManager = getDefaultHistory();

    private int id = 0;
    public HashMap<Integer, Task> taskMap = new HashMap<>();
    public HashMap<Integer, Epic> epicMap = new HashMap<>();
    public HashMap<Integer, Subtask> subtaskMap = new HashMap<>();

    private void addNewPrioritizedTask(Task task) {

        prioritizedTasks.add(task);
        validateTaskPriority();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public List<Task> getHistory() { // Получение списка истории просмотренных задач
        return historyManager.getHistory();
    }

    //     РАБОТА С ЗАДАЧАМИ
    @Override
    public ArrayList<Task> getArrayTask() { // получение списка всех задач
        return taskMap != null ? new ArrayList<>(taskMap.values()) : new ArrayList<>();
    }

    @Override
    public void clearMapOfTask() { // удаление всех задач
        if (!taskMap.isEmpty()) {

            for (Task task : taskMap.values()) {
                historyManager.remove(task.getId());
                prioritizedTasks.clear();
            }
            taskMap.clear();
        }
    }

    @Override
    public Task getTaskOdId(int id) { // получение задачи по id
        Task task = null;
        if (taskMap == null || taskMap.isEmpty() || !taskMap.containsKey(id)) {
            throw new NoSuchElementException("Задача не найдена");
        }
        task = taskMap.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public void putTask(Task task) throws IllegalArgumentException { // создание задачи
        if (task == null) {
            throw new IllegalArgumentException("Задача не может быть NULL");
        }
        if (task.getId() == 0 || id == task.getId()) {
            task.setId(++id);
        }
        addNewPrioritizedTask(task);
        taskMap.put(task.getId(), task);
    }

    @Override
    public void updateTask(Task task) { // обновление задачи
        if (task != null && taskMap.containsKey(task.getId())) {
            addNewPrioritizedTask(task);
            taskMap.put(task.getId(), task);
        }
    }

    @Override
    public void removeTask(int id) { // удаление по идентификатору
        if (taskMap != null && !taskMap.isEmpty() && taskMap.containsKey(id)) { // удаление Task по id
            prioritizedTasks.removeIf(allTasks -> allTasks.getId() == id);
            taskMap.remove(id);
            historyManager.remove(id);
            taskMap.remove(id);
        }
    }

    //    РАБОТА С EPIC
    @Override
    public ArrayList<Epic> getArrayEpic() { // возвращает список Epic из мапы
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public void clearMapOfEpic() { // удаление всех Эпиков
        if (!epicMap.isEmpty()) {
            for (Subtask subtask : subtaskMap.values()) {
                historyManager.remove(subtask.getId());
            }
            subtaskMap.clear();
            for (Epic epic : epicMap.values()) {
                historyManager.remove(epic.getId());
            }
            epicMap.clear();
        }
    }

    @Override
    public Epic getEpicOfId(int id) { // получение Epic по id
        Epic epic = null;
        if (epicMap == null || epicMap.isEmpty() || !epicMap.containsKey(id)) {
            throw new NoSuchElementException("Задача не найдена");
        }
        epic = epicMap.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public void putEpic(Epic epic) { // добавление Epic
        if (epic == null) {
            throw new IllegalArgumentException("Epic не может быть NULL");
        }
        if (epic.getId() == 0 || id == epic.getId()) {
            epic.setId(++id);
        }
        addNewPrioritizedTask(epic);
        epicMap.put(epic.getId(), epic);
    }

    @Override
    public void updateEpic(Epic epic) { // обновление Epic и изменение статуса
        if (epic != null && epicMap.containsKey(epic.getId())) {
            epicMap.put(epic.getId(), epic);
            getStatusEpic(epic.getId());
            updateTimeEpic(epic);
        } else {
            System.out.println("Эпик не найден");
        }
    }

    @Override
    public void removeEpicOfId(int id) { // удаление Epic по идентификатору
        if (epicMap != null && epicMap.containsKey(id)) { // удаление Epic по id
            Epic epic = epicMap.remove(id);
            if (epic != null) {
                epic.getSubtaskArrayList().forEach(subtaskId -> {
                    prioritizedTasks.removeIf(task -> Objects.equals(task.getId(), subtaskId));
                    subtaskMap.remove(subtaskId);
                    historyManager.remove(subtaskId);
                });
                epicMap.remove(id);
                historyManager.remove(id);
            } else {
                System.out.println("Эпик с ID " + id + " не найден.");
            }
        }
    }

    public void updateTimeEpic(Epic epic) { // обновление времени эпика
        List<Subtask> subtasks = getArraySubtaskOfId(epic.getId());

        // Проверка на наличие подзадач
        if (subtasks.isEmpty()) {
            epic.setStartTime(null);
            epic.setEndTime(null);
            epic.setDuration(0); // Устанавливаем длительность в 0, если нет подзадач
            return;
        }

        Instant startTime = null;
        Instant endTime = null;

        for (Subtask subtask : subtasks) {
            // Проверка на null перед сравнением
            if (subtask.getStartTime() != null && (startTime == null || subtask.getStartTime().isBefore(startTime))) {
                startTime = subtask.getStartTime();
            }
            if (subtask.getEndTime() != null && (endTime == null || subtask.getEndTime().isAfter(endTime))) {
                endTime = subtask.getEndTime();
            }
        }

        epic.setStartTime(startTime);
        epic.setEndTime(endTime);

        // Вычисление продолжительности только если startTime и endTime не null
        if (startTime != null && endTime != null) {
            long duration = endTime.toEpochMilli() - startTime.toEpochMilli();
            epic.setDuration(duration);
        } else {
            epic.setDuration(0); // Если нет корректных временных меток, устанавливаем длительность в 0
        }
    }


    @Override
    public void getStatusEpic(int id) { // Управление статусами эпиков
        if (!epicMap.containsKey(id) || epicMap.get(id).getSubtaskArrayList() == null) {
            throw new IllegalArgumentException("Эпик с указанным ID не найден или не имеет подзадач");
        }

        int statusSubNew = 0;
        int statusSubDone = 0;
        List<Integer> subtaskIds = epicMap.get(id).getSubtaskArrayList();

        for (Integer idSub : subtaskIds) {
            StatusTasks status = subtaskMap.get(idSub).getStatus();
            if (status.equals(StatusTasks.NEW)) {
                statusSubNew++;
            } else if (status.equals(StatusTasks.DONE)) {
                statusSubDone++;
            }
        }

        // Установка статуса эпика на основе подсчитанных значений
        if (statusSubNew == subtaskIds.size()) {
            epicMap.get(id).setStatus(StatusTasks.NEW);
        } else if (statusSubDone == subtaskIds.size()) {
            epicMap.get(id).setStatus(StatusTasks.DONE);
        } else {
            epicMap.get(id).setStatus(StatusTasks.IN_PROGRESS);
        }
    }


    // работа с Subtask
    @Override
    public ArrayList<Subtask> getArraySubtask() { // возвращает список Subtask из мапы
        ArrayList<Subtask> t = new ArrayList<>();
        if (subtaskMap != null && !subtaskMap.isEmpty()) {
            t.addAll(subtaskMap.values());
        }
        return t;
    }

    @Override
    public List<Subtask> getArraySubtaskOfId(int id) { // возвращает список Subtask по Id пика
        if (epicMap.containsKey(id)) {
            List<Subtask> t = new ArrayList<>();
            Epic epic = epicMap.get(id);
            for (int i = 0; i < epic.getSubtaskArrayList().size(); i++) {
                t.add(subtaskMap.get((epic.getSubtaskArrayList().get(i))));
            }
            return t;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void clearMapOfSubtask() { // удаление всех Subtasks из мапы, из списка эпика и изменение статуса эпика
        for (Epic epic : epicMap.values()) {
            removeAllSubtaskByEpic(epic);
            getStatusEpic(epic.getId());
        }
    }

    public void removeAllSubtaskByEpic(Epic epic) { //декомпозиция предыдущего кода
        if (epic != null) {
            for (int subtaskId : epic.getSubtaskArrayList()) {
                Subtask subtask = subtaskMap.get(subtaskId);
                prioritizedTasks.remove(subtask);
                subtaskMap.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
            epic.getSubtaskArrayList().clear();
        }
    }

    @Override
    public void clearMapOfSubtaskFromEpic(int id) { // удаление всех Subtasks из Epic по id Epic
        if (epicMap != null && !epicMap.isEmpty() && epicMap.containsKey(id)) {
            for (int subtaskId : epicMap.get(id).getSubtaskArrayList()) {
                Subtask subtask = subtaskMap.get(subtaskId);
                prioritizedTasks.remove(subtask);
                subtaskMap.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
            epicMap.get(id).cleanSubtaskIds();
        }
    }

    @Override
    public void removeSubtaskId(int id) { // удаление подзадачи по id
        Subtask subtask = subtaskMap.get(id);
        if (subtask != null) {
            Epic epic = epicMap.get(subtask.getEpicId());
            epic.removeSubtask(id);
            getStatusEpic(epic.getId());
             prioritizedTasks.remove(subtask);
            subtaskMap.remove(id);
            historyManager.remove(id);
        } else {
            System.out.println("Подзадача не найдена");
        }
    }

    @Override
    public void removeSubtask(int id) { // удаление Subtask по идентификатору и обновление статуса Эпика
        if (subtaskMap != null && !subtaskMap.isEmpty() && subtaskMap.containsKey(id)) { // удаление Epic по id
            Subtask subtask = subtaskMap.remove(id);
            epicMap.get(subtask.getEpicId()).removeSubtask(id);
            getStatusEpic(subtask.getEpicId());

        }
    }

    @Override
    public Subtask getSubtaskOfId(int id) { // получение Subtask по id
        Subtask subtask = null;
        if (subtaskMap == null || subtaskMap.isEmpty() || !subtaskMap.containsKey(id)) {
            throw new NoSuchElementException("Задача не найдена");
        }
        subtask = subtaskMap.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public ArrayList<Subtask> getAllSubtask() { // получение списка всех подзадач
        return new ArrayList<>(subtaskMap.values());

    }

    @Override
    public void putSubtask(Subtask subtask) { // создание Subtask и добавление в список Эпика
        if (subtask == null) {
            throw new IllegalArgumentException("Задача не может быть NULL");
        }
        if (subtask.getId() == 0 || id == subtask.getId()) {
            subtask.setId(++id);
        }
        addNewPrioritizedTask(subtask);
        subtaskMap.put(subtask.getId(), subtask);
        epicMap.get(subtask.getEpicId()).getSubtaskArrayList().add(subtask.getId());
        getStatusEpic(epicMap.get(subtask.getEpicId()).getId());
        updateTimeEpic(epicMap.get(subtask.getEpicId()));
    }

    @Override
    public void updateSubtask(Subtask subtask) { // обновление Subtask
        if (subtask != null && subtaskMap.containsKey(subtask.getId()) && epicMap.containsKey(subtask.getEpicId())) {
            addNewPrioritizedTask(subtask);
            subtaskMap.put(subtask.getId(), subtask);
            getStatusEpic(subtaskMap.get(subtask.getId()).getEpicId());
            updateTimeEpic(epicMap.get(subtask.getEpicId()));
        }
    }

    public boolean checkTime(Task task) {
        if (task == null || task.getStartTime() == null || task.getEndTime() == null) {
            throw new IllegalArgumentException("Задача или ее время начала/конца не могут быть null");
        }

        List<Task> tasks = List.copyOf(prioritizedTasks);

        // Если нет задач для проверки, возвращаем true
        if (tasks.isEmpty()) {
            return true;
        }

        for (Task taskSave : tasks) {
            // Проверяем, что время начала и конца задачи сохраненной задачи задано
            if (taskSave.getStartTime() != null && taskSave.getEndTime() != null) {
                // Проверяем, пересекаются ли временные интервалы
                if (task.getStartTime().isBefore(taskSave.getEndTime()) && task.getEndTime().isAfter(taskSave.getStartTime())) {
                    // Если задачи пересекаются, возвращаем false
                    return false;
                }
            }
        }
        // Если не было пересечений, возвращаем true
        return true;
    }


    private void validateTaskPriority() {
        List<Task> tasks = getPrioritizedTasks();

        for (int i = 0; i < tasks.size(); i++) { // Начинаем с 0
            Task task = tasks.get(i);

            // Проверяем пересечение с предыдущими задачами
            for (int j = 0; j < i; j++) { // Проверяем с каждой предыдущей задачей
                Task previousTask = tasks.get(j);
                boolean taskHasIntersections = !checkTime(previousTask); // Пересечения означают, что checkTime вернет false

                if (taskHasIntersections) {
                    throw new ManagerValidateException(
                            "Задачи #" + task.getId() + " и #" + previousTask.getId() + " пересекаются");
                }
            }
        }
    }

}







