package tsato.com.simpletodo;

import java.util.Date;

/**
 * Created by T on 2016/01/04.
 */
public class Item {
    public enum Priority {HIGH, MID, LOW}
    public enum Status {DONE, TODO}

    private String taskName;
    private String dueDate;
    private String memo;
    private Priority priority;
    private Status status;

    public Item(String taskName, String dueDate, String memo, Priority priority, Status status) {
        this.taskName = taskName;
        this.dueDate = dueDate;
        this.memo = memo;
        this.priority = priority;
        this.status = status;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public String getDueDate() {
        return this.dueDate;
    }

    public String getMemo() {
        return this.memo;
    }

    public Priority getPriority() {
        return this.priority;
    }

    public Status getStatus() {
        return this.status;
    }
}
