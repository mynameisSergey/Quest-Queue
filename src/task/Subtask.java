package task;

import task.TasksStatus.Status;
import task.TasksType.TasksType;

import java.util.Objects;

import java.time.Instant;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(int id, TasksType type, String name, Status status, String description, Instant startTime, long duration, int epicId) {
        super(id, type, name, status, description, startTime, duration);
        this.epicId = epicId;
        this.setStatus(status);
    }

    public Subtask(TasksType type, String name, Status status,  String description, int epicId) {
        super(type, name, status, description);
        this.epicId = epicId;
        this.setStatus(status);
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
        return "" + getId() + "," + getType() + "," + getName() +
                "," + getStatus() + "," + getDescription() + "," +  getStartTime().toEpochMilli() +
                "," + getEndTime().toEpochMilli() + "," + getDuration() + "," + getEpicId() +"";
    }
}

