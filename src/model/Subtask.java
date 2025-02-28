package model;

import java.time.Instant;
import java.util.Objects;
public class Subtask extends Task {
    private int epicId;

    public Subtask(TasksType type, String title, String description, Instant startTime,
                   long duration,int epicId) {
        super(type, title, description, startTime, duration);
        validateEpicId(epicId);
        this.epicId = epicId;
    }

    public Subtask(TasksType type, String title, String description, StatusTasks statusTasks, Instant startTime,
                   long duration,int epicId) {
        super(type, title, description, statusTasks, startTime, duration);
        validateEpicId(epicId);
        this.epicId = epicId;
    }

    public Subtask(int id, TasksType type, String title, String description, StatusTasks statusTasks, Instant startTime,
                   long duration, int epicId) {
        super(id, type, title, description, statusTasks,startTime,duration);
        validateEpicId(epicId);
        this.epicId = epicId;
    }
    private void validateEpicId(int epicId) {
        if (epicId < 0) {
            throw new IllegalArgumentException("Epic ID cannot be negative");
        }
    }
    public int getEpicId() {
        return epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d,%d,%d",
                id,
                type != null ? type.toString() : "null",
                title != null ? title : "null",
                description != null ? description : "null",
                statusTasks != null ? statusTasks.toString() : "null",
                startTime != null ? startTime.toEpochMilli() : 0,
                duration,
                epicId);
    }
}

