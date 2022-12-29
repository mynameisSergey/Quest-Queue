package tasks;

import java.util.Objects;

public class Subtask extends Task {

    private int epicId;

    public Subtask(String title, String description, int epicId, int id, Status status) {
        super(title, description, id, status);
        this.epicId = epicId;
        this.setStatus(status);
    }

    public Subtask(String title, String description, int epicId, Status status) {
        super(title, description, status);
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
        return "Subtask{" +
                "title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                '}';
    }
}

