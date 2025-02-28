package service;

import exception.ManagerSaveException;
import model.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager { // прописывается логика автосохранения в файл

    protected static String pathToList;
    public FileBackedTasksManager(String pathToList) {
        FileBackedTasksManager.pathToList = pathToList;
    }
    static HashMap<Integer, Task> allTask = new HashMap<>();

    /**/
    public static String historyToString(HistoryManager manager) { // преобразование истории просмотров в строку c id задач

        List<String> listString = new ArrayList<>();

        for (Task task : manager.getHistory()) {
            listString.add(String.valueOf(task.getId()));
        }

        return String.join(",", listString);
    }

    /**/
    public void save() { // сохраняет текущее состояние менеджера в файл

        String title = "id,type,title,description,status,startTime,duration,epicId";
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(Path.of(pathToList))) {

            bufferedWriter.write(title);
            bufferedWriter.newLine();

            for (Task task : getArrayTask()) {
                bufferedWriter.write(task.toString());
                bufferedWriter.newLine();
            }
            for (Epic epic : getArrayEpic()) {
                bufferedWriter.write(epic.toString());
                bufferedWriter.newLine();
            }
            for (Subtask subtask : getArraySubtask()) {
                bufferedWriter.write(subtask.toString());
                bufferedWriter.newLine();
            }
            bufferedWriter.newLine();
            bufferedWriter.write(historyToString(historyManager));

        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    /**/
    public Task fromString(String value) { // метод создания задачи из строки / разносятся по мапам
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Значение не может быть null или пустым");
        }

        Task task = null;
        String[] taskElement = value.split(",");

        if (taskElement.length < 7) {
            throw new IllegalArgumentException("Недостаточно элементов в строке: " + value);
        }

        try {
            int id = Integer.parseInt(taskElement[0].trim());
            TasksType type = TasksType.valueOf(taskElement[1].trim());
            String title = taskElement[2].trim();
            String description = taskElement[3].trim();
            StatusTasks statusTasks = StatusTasks.valueOf(taskElement[4].trim());
            Instant startTime = Instant.ofEpochSecond(Long.parseLong(taskElement[5].trim()));
            long duration = Long.parseLong(taskElement[6].trim());
            Integer epicId = null;

            if (taskElement.length == 8) {
                epicId = Integer.parseInt(taskElement[7].trim());
            }

            switch (type) {
                case EPIC:
                    task = new Epic(id, type, title, description, statusTasks, startTime, duration);
                    epicMap.put(id, (Epic) task);
                    break;

                case SUBTASK:
                    if (epicId == null) {
                        throw new IllegalArgumentException("Для подзадачи требуется epicId");
                    }
                    task = new Subtask(id, type, title, description, statusTasks, startTime, duration, epicId);
                    subtaskMap.put(id, (Subtask) task);
                    break;

                case TASK:
                    task = new Task(id, type, title, description, statusTasks, startTime, duration);
                    taskMap.put(id, task);
                    break;

                default:
                    throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
            }

            allTask.put(id, task); // Добавляем задачу в общий список

        } catch (NumberFormatException e) {
            System.out.println("Некорректный ввод: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при создании задачи: " + e.getMessage());
            e.printStackTrace();
        }

        return task;
    }


    /**/
    public static List<Integer> historyFromString(String value) {
        // метод для преобразования строки в список истории просмотров
        List<Integer> list = new ArrayList<>();
        try {
            if (!value.isEmpty() && !value.isBlank()) {
                String[] str2 = value.split(",");

                for (String s : str2) {
                    list.add(Integer.parseInt(s));
                }

            } else {
                System.out.println("Пустая строка");
            }
        } catch (NumberFormatException e) {
            System.out.println("Некорректный ввод: " + e);
        }
        return list;
    }

    public static FileBackedTasksManager loadFromFile(File file) throws IOException { // восстанавливает данные менеджера из файла при запуске программы
        FileBackedTasksManager manager = new FileBackedTasksManager(file.getPath());

        List<String> listOfTasks = new ArrayList<>();

        try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(pathToList))) {

            String str;
            while ((str = bufferedReader.readLine()) != null) {
                listOfTasks.add(str);
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }

        for (int j = 1; j < listOfTasks.size() - 2; j++) {
            manager.fromString(listOfTasks.get(j));
        }

        String list = listOfTasks.get(listOfTasks.size() - 1);
        List<Integer> listHistory = historyFromString(list);
        for (Integer i : listHistory) {
            manager.historyManager.add(allTask.get(i));
        }
        return manager;
    }

    //     РАБОТА С ЗАДАЧАМИ
    @Override
    public Task getTaskOdId(int id) { // получение задачи по id
        Task task = super.getTaskOdId(id);
        save();
        return task;
    }

    @Override
    public void clearMapOfTask() { // удаление всех задач
        super.clearMapOfTask();
        save();
    }

    @Override
    public void putTask(Task task) { // создание задачи
        super.putTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) { // обновление задачи
        super.updateTask(task);
        save();
    }

    @Override
    public void removeTask(int id) { // удаление по идентификатору
        super.removeTask(id);
        save();
    }
    //    РАБОТА С EPIC

    @Override
    public Epic getEpicOfId(int id) { // получение Epic по id
        Epic epic = super.getEpicOfId(id);
        save();
        return epic;
    }

    @Override
    public void clearMapOfEpic() { // удаление всех Эпиков
        super.clearMapOfEpic();
        save();
    }

    @Override
    public void putEpic(Epic epic) { // добавление Epic
        super.putEpic(epic);
        save();
    }

    @Override
    public void updateEpic(Epic epic) { // обновление Epic и изменение статуса
        super.updateEpic(epic);
        save();
    }

    @Override
    public void removeEpicOfId(int id) { // удаление Epic по идентификатору
        super.removeEpicOfId(id);
        save();
    }

    @Override
    public void getStatusEpic(int id) { // Управление статусами эпиков
        super.getStatusEpic(id);
        save();
    }

    // работа с Subtask

    @Override
    public Subtask getSubtaskOfId(int id) { // получение Subtask по id
        Subtask subtask = super.getSubtaskOfId(id);
        save();
        return subtask;
    }

    @Override
    public void clearMapOfSubtask() { // удаление всех Subtasks из мапы, из списка
        super.clearMapOfSubtask();
        save();
    }

    @Override
    public void clearMapOfSubtaskFromEpic(int id) { // удаление всех Subtasks из Epic
        super.clearMapOfSubtaskFromEpic(id);
        save();
    }

    @Override
    public void putSubtask(Subtask subtask) { // создание Subtask и добавление в список Эпика
        super.putSubtask(subtask);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) { // обновление Subtask
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeSubtask(int id) { // удаление Subtask по идентификатору и обновление статуса Эпика
        super.removeSubtask(id);
        save();
    }
}
