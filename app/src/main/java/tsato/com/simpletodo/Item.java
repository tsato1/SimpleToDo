package tsato.com.simpletodo;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by T on 2016/01/04.
 */
public class Item implements Serializable {
    public static <E extends Enum<E>> E fromOrdinal(Class<E> enumClass, int ordinal) {
        E[] enumArray = enumClass.getEnumConstants();
        return enumArray[ordinal];
    }
    public enum Priority {HIGH, MID, LOW;}
    public enum Status {TODO, PEND, DONE;}

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

    static public TextView setColorOnPriority(Context context, TextView txv, Item item) {
        switch (item.getPriority()) {
            case HIGH:
                txv.setTextColor(ContextCompat.getColor(context, R.color.colorPriorityHigh));
                break;
            case MID:
                txv.setTextColor(ContextCompat.getColor(context, R.color.colorPriorityMid));
                break;
            case LOW:
                txv.setTextColor(ContextCompat.getColor(context, R.color.colorPriorityLow));
                break;
        }
        return txv;
    }
}
