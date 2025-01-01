package model;

import java.time.Instant;
import java.util.Objects;

public class Task { // родительский класс

    protected long duration;
    protected Instant startTime;
    protected String title;
    protected String description; // описание
    protected int id;
    protected StatusTasks statusTasks;
    protected TasksType type;


    public Task(TasksType type, String title, String description, Instant startTime, long duration) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.statusTasks = StatusTasks.NEW;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(TasksType type, String title, String description, StatusTasks statusTasks, Instant startTime, long duration) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.statusTasks = statusTasks;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(int id, TasksType type, String title, String description, StatusTasks statusTasks, Instant startTime,
                long duration) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.description = description;
        this.statusTasks = statusTasks;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Instant getEndTime() {
        long SECONDS_IN_MINUTE = 60L;
        return startTime.plusSeconds(duration * SECONDS_IN_MINUTE);
    }


    public TasksType getType() {
        return type;
    }

    public StatusTasks getStatusTasks() {
        return statusTasks;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StatusTasks getStatus() {
        return statusTasks;
    }

    public void setStatus(StatusTasks statusTasks) {
        this.statusTasks = statusTasks;
    }
    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }



    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d,%d",
                id,
                type != null ? type.toString() : "null",
                title != null ? title : "null",
                description != null ? description : "null",
                statusTasks != null ? statusTasks.toString() : "null",
                startTime != null ? startTime.toEpochMilli() : 0, // или другое значение по умолчанию
                duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(title, task.title) && Objects.equals(description, task.description) && statusTasks == task.statusTasks;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id); // Генерация hash-кода на основе id
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStatusTasks(StatusTasks statusTasks) {
        this.statusTasks = statusTasks;
    }

    public void setType(TasksType type) {
        this.type = type;
    }
}
