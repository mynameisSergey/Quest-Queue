package tasks;

import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private int id;
    private Status status;
    private TasksType type;

    public Task(TasksType type, Status status, String name, String description) {
        this.name = name;
        this.description = description;
        this.setStatus(status);
        this.type = type;
    }

    public Task(int id, TasksType type, Status status, String name, String description) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.setStatus(status);
        this.type = type;
    }

    public TasksType getType() {
        return type;
    }

    public void setType(TasksType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setTitle(String title) {
        this.name = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    public int getId() {
        return id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status);
    }

    public String toString() {
        return "" + id+"," + type + "," + name + "," + status + "," + description +"";
    }
}













