package tasks;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private int epicId;
    private ArrayList<Integer> subTaskIds;

    public Epic(TasksType type, Status status, String name, String description) {
        super(type, status, name, description);
        this.subTaskIds = new ArrayList<>();
    }

    public Epic(int id, TasksType type, Status status, String name, String description) {
        super(id, type, status, name, description);
        this.subTaskIds = new ArrayList<>();
    }


    public ArrayList<Integer> getSubTaskId() {
        return subTaskIds;
    }

    public void addSubtaskId(int id) {
        subTaskIds.add(id);
    }

    public void cleanSubtaskIds() {
        subTaskIds.clear();
    }

    public void removeSubtask(int id) {
        subTaskIds.remove(id);
    }

    void getStatusEpic(int id) { // Управление статусами эпиков
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return epicId == epic.epicId && Objects.equals(subTaskIds, epic.subTaskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId, subTaskIds);
    }

    @Override
    public String toString() {
        return "Epic{" + ", id=" + getId() + ", type=" + getType() + "name='" + getName() +
                ", status='" + getStatus() + ", description='" + getDescription() + '}';
    }
}
