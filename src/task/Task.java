package task;

import task.TasksStatus.Status;
import task.TasksType.TasksType;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;


public class Task {
    private String name;
    private String description;
    private int id;
    Status status;
    private TasksType type;
    private Instant startTime;
    private long duration;

    public Task(TasksType type, String name, Status status, String description) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = type;
    }

    public Task(int id, TasksType type, String name, Status status, String description, Instant startTime, long duration) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        this.type = type;
        this.startTime = startTime;
        this.duration = duration;
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

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Instant getEndTime() {
        long SECONDS_IN_MINUTE = 60L;
        return startTime.plusSeconds(duration * SECONDS_IN_MINUTE);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(description, task.description) &&
                Objects.equals(name, task.name)
                && status == task.status
                && Objects.equals(startTime.truncatedTo(ChronoUnit.SECONDS),
                task.startTime.truncatedTo(ChronoUnit.SECONDS))
                && Objects.equals(duration, task.duration);
    }



    @Override
    public int hashCode() {
        return Objects.hash(description, id, name, status, startTime, duration);
    }
    public String toString() {
        return "" + id + "," + type + "," + name + "," + status + "," + description + "," + startTime.toEpochMilli() + "," + getEndTime().toEpochMilli() + "," + duration + "";
    }
}













