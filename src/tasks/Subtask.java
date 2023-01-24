package tasks;

import java.util.Objects;

public class Subtask extends Task {

    private int epicId;

    public Subtask(int id, TasksType type, Status status, String name, String description, int epicId) {
        super(id, type, status, name, description);
        this.epicId = epicId;
        this.setStatus(status);
    }

    public Subtask(TasksType type, Status status, String name, String description, int epicId) {
        super(type, status, name, description);
        this.epicId = epicId;
        this.setStatus(status);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
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
        return "Subtask{" + ", id=" + getId() + ", type=" + getType() + "name='" + getName() +
                ", status='" + getStatus() + ", description='" +
                getDescription() + ", epicId='" + getEpicId() + '}';
    }
}

