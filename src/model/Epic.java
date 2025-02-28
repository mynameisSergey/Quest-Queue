package model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subtaskArrayList;
    private Instant endTime;

    public Epic(TasksType type, String title, String description, Instant startTime,
                long duration) {
        super(type, title, description, startTime, duration);
        this.subtaskArrayList = new ArrayList<>();
    }
    public Epic(TasksType type, String title, String description, StatusTasks statusTasks, Instant startTime,
                long duration) {
        super(type, title, description, statusTasks, startTime, duration);
        this.subtaskArrayList = new ArrayList<>();
    }

    public Epic(int id, TasksType type, String title, String description, StatusTasks statusTasks, Instant startTime,
                long duration) {
        super(id, type, title, description, statusTasks, startTime, duration);
        this.subtaskArrayList = new ArrayList<>();
        this.endTime = super.getEndTime();
    }
    public ArrayList<Integer> getSubtaskArrayList() {
        return subtaskArrayList;
    }

    @Override
    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }
    public void cleanSubtaskIds() {
        subtaskArrayList.clear();
    }

    public void removeSubtask(int id) {
        subtaskArrayList.remove(Integer.valueOf(id));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskArrayList, epic.subtaskArrayList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskArrayList != null ? subtaskArrayList : Collections.emptyList());
    }

    @Override
    public String toString() {
        return super.toString() + ", Subtasks: " + subtaskArrayList.toString();
    }
}
