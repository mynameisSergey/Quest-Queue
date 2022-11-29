import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private int epicId;
    private ArrayList<Integer> subTaskIds;

    public Epic (String title, String description, String status) {
        super(title, description, status);
        this.subTaskIds = new ArrayList<>();
    }

    public Epic (String title, String description, int epicId, String status) {
        super(title, description, status);
        this.epicId = epicId;
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
        return "Epic{" +
                "title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                '}';
    }
}