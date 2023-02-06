package manager;

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
import java.time.Instant;
import java.nio.charset.StandardCharsets;

public class FileBackedTasksManager extends InMemoryTaskManager {



    public static void main(String[] args) {
        FileBackedTasksManager manager = new FileBackedTasksManager("resourses/list.csv");


        manager.fromString("1,TASK,Таск1,NEW,описание1,2022-02-06T19:42:02.234702Z,0");
        manager.fromString("2,TASK,таск2,NEW,описание2,2021-02-06T19:42:02.234702Z,0");
        manager.fromString("3,EPIC,эпик1,NEW,эпикпервыйсдвумясабами,2020-02-06T19:42:02.234702Z,0");
        manager.fromString("4,SUBTASK,Сабстак1,NEW,Сабстак1первогоэпика,2019-02-06T19:42:02.234702Z,0,3");
        manager.fromString("5,SUBTASK,Сабстак2,NEW,Сабстак2первогоэпика,2018-02-06T19:42:02.234702Z,0,3");
        manager.fromString("6,SUBTASK,Сабстак3,NEW,Сабстак3первогоэпика,2017-02-06T19:42:02.234702Z,0,3");
        manager.getTask(1);
        manager.getTask(2);
        manager.getEpic(3);
        manager.getSubtask(4);
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
        String title = "id,type,name,status,description,startTime,duration,epic";

        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(String.valueOf(pathToList)))) {
          writer.write(title);
          writer.newLine();

            for (Object task : getAllTask()) {
                writer.write(task.toString());
                writer.newLine();
            }
            for (Object epic : getAllEpic()) {
                writer.write(epic.toString());
                writer.newLine();
            }
            for (Object subtask : getAllSubtask()) {
                writer.write(subtask.toString());
                writer.newLine();
            }

            writer.newLine();
            writer.write(historyInString);

        } catch (IOException e) {
            throw new HistoryManager.ManagerSaveException(e.getMessage());
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
        Instant startTime = Instant.parse(split[5]);
        long duration = Long.parseLong(split[6]);
        Integer epicId = type.equals("SUBTASK") ? Integer.parseInt(split[7]) : null;

        switch (type) {
            case EPIC:
                task = new Epic(id, type, name, status, description, startTime, duration);
                task.setId(id);
                task.setStatus(status);
                allEpics.put(id, (Epic) task);
                allTask.put(id, task);
                return task;

            case SUBTASK:
                task = new Subtask(id, type, name,  status,  description, startTime, duration, epicId);
                task.setId(id);
                allSubtasks.put(id, (Subtask) task);
                allTask.put(id, task);
                return task;

            case TASK:
                task = new Task(id, type, name, status,  description, startTime, duration);
                task.setId(id);
                allTasks.put(id, task);
                allTask.put(id, task);
                return task;
            default:
                throw new IllegalArgumentException();
        }
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
        try (BufferedReader fileReader = new BufferedReader(new FileReader("resourses/list.csv", StandardCharsets.UTF_8))) {
            while (fileReader.ready()) {
                String line = fileReader.readLine();
                list.add(line);
            }

        } catch (IOException e) {
            throw new HistoryManager.ManagerSaveException(e.getMessage());
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


    private String getParentEpicId(Task task) {
        if (task instanceof Subtask) {
            return Integer.toString(((Subtask) task).getEpicId());
        }
        return "";
    }


    // Другой вариант метода сохранения задачи в строку
    private String toString(Task task) {
        String[] toJoin = {Integer.toString(task.getId()), getType(task).toString(), task.getName(),
                task.getStatus().toString(), task.getDescription(), String.valueOf(task.getStartTime()),
                String.valueOf(task.getDuration()), getParentEpicId(task)};
        return String.join(",", toJoin);
    }

    @Override
    public List<Task> getHistory() { // Получение списка истории просмотренных задач
        List<Task> listTask = super.getHistory();
        return listTask;
    }

    private TasksType getType(Task task) {
        if (task instanceof Epic) {
            return TasksType.EPIC;
        } else if (task instanceof Subtask) {
            return TasksType.SUBTASK;
        }
        return TasksType.TASK;
    }

    @Override
    public Task getTask(int id) { // получение задачи по айди
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public List<Task> getAllTask() { // получение списка всех задач
        List<Task> tasks = super.getAllTask();

        return tasks;
    }

    @Override
    public Task addNewTask(Task task) { // добавление задачи в мапу
        super.addNewTask(task);
        save();
        return task;
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
    public List<Epic> getAllEpic() { // получение списка всех эпиков
        List<Epic> epics = super.getAllEpic();

        return epics;
    }

    @Override
    public Epic addNewEpic(Epic epic) { // дробавляем новый эпик
        super.addNewEpic(epic);
        save();
        return epic;
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
    public List<Subtask> getIdSubtask(int id) { // Получение списка всех подзадач определённого эпика.
        List<Subtask> subtasks = super.getIdSubtask(id);
        save();
        return subtasks;
    }

    public void getStatusEpic(int id) { // Управление статусами эпиков
        super.getStatusEpic(id);
        save();
    }


    // РАБОТА С ПОДЗАДАЧАМИ

    @Override
    public Subtask addNewSubtask(Subtask subtask) { // добавляем новые подзадачи
        super.addNewSubtask(subtask);
        save();
        return subtask;
    }

    @Override
    public Subtask getSubtask(int id) {// получение подзадачи по айди
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public List<Subtask> getAllSubtask() { // получение списка всех подзадач
        List<Subtask> subtasks = super.getAllSubtask();

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



