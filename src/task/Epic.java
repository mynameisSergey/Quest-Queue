package task;

import task.TasksStatus.Status;
import task.TasksType.TasksType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.time.Instant;

import static task.TasksStatus.Status.IN_PROGRESS;

public class Epic extends Task {
    private int epicId;
    private ArrayList<Integer> subTaskIds;
    private Instant endTime;
    private final ArrayList<Integer> subtasksIds = new ArrayList<>();
    public Epic(TasksType type, String name, Status status, String description) {
        super(type, name, status,  description);
        this.subTaskIds = new ArrayList<>();
    }

    public Epic(int id, TasksType type, String name, Status status, String description, Instant startTime, long duration) {
        super(id, type, name, status, description, startTime, duration);
        this.subTaskIds = new ArrayList<>();
        this.endTime = super.getEndTime();
    }
    public void addSubtask(int subtaskId) {
        subtasksIds.add(subtaskId);
    }
    public void calculateStatus(HashMap<Integer, Subtask> subtasksMap) {
        boolean newExist = false;
        status = Status.NEW;
        if (!subtasksIds.isEmpty()) {
            for (Integer id : subtasksIds) {
                Subtask subtask = subtasksMap.get(id);
                if (subtask != null) {
                    switch (subtask.getStatus()) {
                        case IN_PROGRESS:
                            this.status = IN_PROGRESS;
                            return;
                        case DONE:
                            if (newExist) {
                                this.status = IN_PROGRESS;
                                return;
                            }
                            this.status = Status.DONE;
                            break;
                        case NEW:
                            newExist = true;
                            if (this.status.equals(Status.DONE)) {
                                this.status = IN_PROGRESS;
                                return;
                            }
                            break;
                    }
                }
            }
        }
    }

    public ArrayList<Integer> getSubTaskId() {
        return subTaskIds;
    }

    public void setSubtaskIds(int id) {
        subTaskIds.add(id);
    }

    public void addSubtaskId(int id) {
        subTaskIds.add(id);
    }
    @Override
    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
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
        return Objects.hash(super.hashCode(), subTaskIds);
    }

    @Override
    public String toString() {
        return "" + getId() + "," + getType() + "," + getName() +
                "," + getStatus() + "," + getDescription() + "," + getStartTime().toEpochMilli() +
                "," + getEndTime().toEpochMilli() + "," +  getDuration() + "";
    }
}
