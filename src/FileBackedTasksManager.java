import manager.HistoryManager;
import manager.InMemoryTaskManager;
import tasks.*;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static manager.Managers.getDefaultHistory;
import static tasks.Status.NEW;
import static tasks.TasksType.*;
import static tasks.TasksType.SUBTASK;


public class FileBackedTasksManager extends InMemoryTaskManager {

    static void main(String[] args) {
        HistoryManager historyManager = (HistoryManager) getDefaultHistory();



        Task taskFirst = new Task(TASK, NEW, "Таск1", "описание1" );
        Task taskSecond = new Task(TASK, NEW, "таск2", "описание2");
        System.out.println("Эпики с сабстасками");
        Epic epicFirst = new Epic(EPIC, NEW, "эпик1", "эпик первый с двумя сабами");
        Subtask subtaskFirst = new Subtask(SUBTASK,NEW, "Сабстак1", "Сабстак1 первого эпика", epicFirst.getId());
        Subtask subtaskSecond = new Subtask(SUBTASK, NEW, "Сабстак2", "Сабстак2 первого эпика", epicFirst.getId());
        Subtask subtaskFhird = new Subtask(SUBTASK, NEW, "Сабстак3", "Сабстак3 первого эпика", epicFirst.getId());

        historyManager.add(taskFirst);
        historyManager.add(taskSecond);
        historyManager.add(epicFirst);
        historyManager.add(subtaskFirst);
        historyManager.add(subtaskSecond);
        historyManager.add(subtaskFhird);
        FileBackedTasksManager.loadFromFile(new File("list.txt"));
        System.out.println("ddddddddddd");
        System.out.println(historyManager.getHistory());
    }

    HistoryManager historyManager = (HistoryManager) getDefaultHistory();


    protected void save() { // сохранение
        final String historyInString = historyToString(historyManager);
        Path pathToList = Paths.get("C:\\Users\\serge\\dev\\java-kanban\\Resourses");

        try (FileWriter fileWriter = new FileWriter(String.valueOf(pathToList))) {
            fileWriter.write("id,type,name,status,description,epic\n");

            for (Task task : allTasks.values()) {
                fileWriter.write(task.toString());
                fileWriter.write("\n");
            }
            for (Epic epic : allEpics.values()) {
                fileWriter.write(epic.toString());
                fileWriter.write("\n");
            }
            for (Subtask subtask : allSubtasks.values()) {
                fileWriter.write(subtask.toString());
                fileWriter.write("\n");
            }
            fileWriter.write("\n");
            fileWriter.write(historyInString);

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка ввода информации.");
        }
    }

    static String historyToString(HistoryManager historyManager) { //преобразование истории в строку с айди задач
        List<String> list = new ArrayList<>();

        for (Task x : historyManager.getHistory()) {
            list.add((String.valueOf(x.getId())));
        }
        String[] array = new String[list.size()];
        list.toArray(array);
        return String.join(",", array);
    }


    public void fromString(String value) {

        String[] split = value.split(",");
        int id = Integer.parseInt(split[0]);
        TasksType type = TasksType.valueOf(split[1]);
        String name = split[2];
        Status status = Status.valueOf(split[3]);
        String description = split[4];
        int epicId = Integer.parseInt(split[5]);

        switch (type) {
            case TASK:
                Task task = new Task(id, type, status, name, description);
                allTasks.put(task.getId(), task);

            case EPIC:
                Epic epic = new Epic(id, type, status, name, description);
                allEpics.put(epic.getId(), epic);

            case SUBTASK:
                Subtask subtask = new Subtask(id, type, status, name, description, epicId);
                allSubtasks.put(subtask.getId(), subtask);
                allEpics.get(subtask.getEpicId()).getSubTaskId().add(subtask.getId());
                default:
                throw new IllegalArgumentException();
        }
    }

    static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        if (!value.isEmpty() && !value.isBlank()) {

            String[] split = value.split(",");
            int id = Integer.parseInt(split[0]);
            history.add(id);
        } else {
            System.out.println("Строка пустая");
        }
        return history;
    }


    static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        List<String> list = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            while (fileReader.ready()) {
                String line = fileReader.readLine();
                list.add(line);
            }

        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }

        for (int i = 1; i < list.size() - 2; i++) {
            fileBackedTasksManager.fromString(list.get(i));
        }
        String lastList = list.get(list.size() - 1);
        String[] split = lastList.split(",");

        for (String id : split) {
            int newId = Integer.parseInt(id);
            if (allTasks.containsKey(newId)) {
                fileBackedTasksManager.historyManager.add(allTasks.get(newId));
            } else if (allEpics.containsKey(newId)) {
                fileBackedTasksManager.historyManager.add(allEpics.get(newId));
            } else if (allSubtasks.containsKey(newId)) {
                fileBackedTasksManager.historyManager.add(allSubtasks.get(newId));
            } else {
                System.out.println("Произошла ошибка");
            }
        }
        return fileBackedTasksManager;
    }

    // РАБОТА С ЗАДАЧАМИ
    public static Map<Integer, Task> allTasks = new HashMap<>(); // мапа с задачами

    @Override
    public List<Task> getHistory() { // Получение списка истории просмотренных задач
        List<Task> listTask = super.getHistory();
        save();
        return listTask;
    }

    // ВСЕ ПРО ЗАДАЧИ
    @Override
    public Task getTask(int id) { // получение задачи по айди
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public ArrayList<Task> getAllTask() { // получение списка всех задач
        ArrayList<Task> tasks = super.getAllTask();
        save();
        return tasks;
    }

    @Override
    public void addNewTask(Task task) { // добавление задачи в мапу
        super.addNewTask(task);
        save();
    }

    @Override
    public void removeAllTask() { // удаление всех задач из мапы
        super.removeAllTask();
        save();
    }

    @Override
    public void removeTaskId(int id) { // удаление задачи из мапы по айди
        super.removeTaskId(id);
        save();
    }

    @Override
    public void updateTask(Task task) { // обновление задачи
        super.updateTask(task);
        save();
    }


    // РАБОТА С ЭПИКАМИ
    public static Map<Integer, Epic> allEpics = new HashMap<>(); // мапа с эпиками

    @Override
    public Epic getEpic(int id) { // получение эпика по айди
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public ArrayList<Epic> getAllEpic() { // получение списка всех эпиков
        ArrayList<Epic> epics = super.getAllEpic();
        save();
        return epics;
    }

    @Override
    public void addNewEpic(Epic epic) { // дробавляем новый эпик
        super.addNewEpic(epic);
        save();
    }

    @Override
    public void removeAllEpic() { // удаление всех эпиков из мапы
        super.removeAllEpic();
        save();
    }

    @Override
    public void removeEpicId(int id) { // удаление эпика из мапы по айди
        super.removeEpicId(id);
        save();
    }

    @Override
    public void updateEpic(Epic epic) { // обновление эпика
        super.updateEpic(epic);
        save();
    }

    @Override
    public ArrayList<Subtask> getIdSubtask(int id) { // Получение списка всех подзадач определённого эпика.
        ArrayList<Subtask> subtasks = super.getIdSubtask(id);
        save();
        return subtasks;
    }

    public void getStatusEpic(int id) { // Управление статусами эпиков
        super.getStatusEpic(id);
        save();
    }


    // РАБОТА С ПОДЗАДАЧАМИ
    public static Map<Integer, Subtask> allSubtasks = new HashMap<>(); // мапа с подзадачами

    @Override
    public void addNewSubtask(Subtask subtask) { // добавляем новые подзадачи
        super.addNewSubtask(subtask);
        save();
    }

    @Override
    public Subtask getSubtask(int id) {// получение подзадачи по айди
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public ArrayList<Subtask> getAllSubtask() { // получение списка всех подзадач
        ArrayList<Subtask> subtasks = super.getAllSubtask();
        save();
        return subtasks;
    }

    @Override
    public void removeAllSubtask() { // удаление всех подзадач из мапы
        super.removeAllSubtask();
        save();
    }

    @Override
    public void removeSubtaskId(int id) { // удаление подзадачи из мапы по айди
        super.removeSubtaskId(id);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) { // обновление подзадачи
        super.updateSubtask(subtask);
        save();
    }

}



