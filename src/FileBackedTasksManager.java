import manager.HistoryManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import tasks.*;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FileBackedTasksManager extends InMemoryTaskManager {



    public static void main(String[] args) {
        FileBackedTasksManager manager = new FileBackedTasksManager("resourses/list.csv");


        manager.fromString("1,TASK,Таск1,NEW,описание1");
        manager.fromString("2,TASK,таск2,NEW,описание2");
        manager.fromString("3,EPIC,эпик1,NEW,эпикпервыйсдвумясабами");
        manager.fromString("4,SUBTASK,Сабстак1,NEW,Сабстак1первогоэпика,3");
        manager.fromString("5,SUBTASK,Сабстак2,NEW,Сабстак2первогоэпика,3");
        manager.fromString("6,SUBTASK,Сабстак3,NEW,Сабстак3первогоэпика,3");
        manager.getTask(1);
        manager.getTask(2);
        manager.getEpic(3);
        manager.save();
        loadFromFile(new File("resourses/list.csv"));
        FileBackedTasksManager manager2 = loadFromFile(new File("resourses/list.csv"));
        System.out.println(manager2.getHistory());
        System.out.println(manager2.getAllTask().size());
        System.out.println(manager2.getAllEpic().size());
        System.out.println(manager2.getAllSubtask().size());

        }
    private String path;
    public FileBackedTasksManager(String path) {
        this.path = path;
    }
    static HashMap<Integer, Task> allTask = new HashMap<>();
    Path pathToList = Paths.get("resourses/list.csv");


    protected void save() { // сохранение
        final String historyInString = historyToString(historyManager);
        String title = "id,type,name,status,description,epic";

        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(String.valueOf(pathToList)))) {
          writer.write(title);
          writer.newLine();

            for (Task task : getAllTask()) {
                writer.write(task.toString());
                writer.newLine();
            }
            for (Epic epic : getAllEpic()) {
                writer.write(epic.toString());
                writer.newLine();
            }
            for (Subtask subtask : getAllSubtask()) {
                writer.write(subtask.toString());
                writer.newLine();
            }

            writer.newLine();
            writer.write(historyInString);

        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
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



    public Task fromString(String value) {
        Task task;
        String[] split = value.split(",");
        int id = Integer.parseInt(split[0]);
        TasksType type = TasksType.valueOf(split[1]);
        String name = split[2];
        Status status = Status.valueOf(split[3]);
        String description = split[4];
        Integer epicId = null;
        if (split.length > 5) {
            epicId = Integer.parseInt(split[5]);
        }

        switch (type) {
            case EPIC:
                task = new Epic(id, type, status, name, description);
                task.setId(id);
                allEpics.put(id, (Epic) task);
                allTask.put(id, task);
                break;

            case SUBTASK:
                task = new Subtask(id, type, status, name, description, epicId);
                task.setId(id);
                allSubtasks.put(id, (Subtask) task);
                allTask.put(id, task);
                break;

            case TASK:
                task = new Task(id, type, status, name, description);
                task.setId(id);
                allTasks.put(id, task);
                allTask.put(id, task);
                break;
            default:
                throw new IllegalArgumentException();
        }
        return task;
    }

    static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        if (!value.isEmpty() && !value.isBlank()) {

            String[] split = value.split(",");
            for (int i = 0; i < split.length; i++) {
                int id = Integer.parseInt(split[i]);
                history.add(id);
            }
        } else {
            System.out.println("Строка пустая");
        }
        return history;
    }


    static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file.getPath());
        List<String> list = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader("resourses/list.csv"))) {
            while (fileReader.ready()) {
                String line = fileReader.readLine();
                list.add(line);
            }

        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }

        for (int i = 1; i < list.size() - 2; i++) {
            fileBackedTasksManager.fromString(list.get(i));
        }
        String lastList = list.get(list.size() - 1);
        List<Integer> historyList = historyFromString(lastList);
        for (Integer id : historyList) {
            fileBackedTasksManager.historyManager.add(allTask.get(id));
        }
        return fileBackedTasksManager;
    }

    // РАБОТА С ЗАДАЧАМИ

    @Override
    public List<Task> getHistory() { // Получение списка истории просмотренных задач
        List<Task> listTask = super.getHistory();
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

    @Override
    public Epic getEpic(int id) { // получение эпика по айди
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public ArrayList<Epic> getAllEpic() { // получение списка всех эпиков
        ArrayList<Epic> epics = super.getAllEpic();

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



