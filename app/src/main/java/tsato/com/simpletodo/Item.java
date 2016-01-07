package tsato.com.simpletodo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by T on 2016/01/04.
 */
public class Item implements Serializable {
    public enum Priority {HIGH, MID, LOW,;}
    public enum Status {TODO, DONE, PENDING}

    private String id;
    private String taskName;
    private String dueDate;
    private String memo;
    private Priority priority;
    private Status status;

    public Item(String id, String taskName, String dueDate, String memo, Priority priority, Status status) {
        this.id = id;
        this.taskName = taskName;
        this.dueDate = dueDate;
        this.memo = memo;
        this.priority = priority;
        this.status = status;
    }

    public String getId() {
        return id;
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
