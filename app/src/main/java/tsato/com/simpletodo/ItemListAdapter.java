package tsato.com.simpletodo;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by T on 2016/01/04.
 */
public class ItemListAdapter extends ArrayAdapter<Item> {
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public ItemListAdapter(Context context, int id, List<Item> items) {
        super(context, id, items);
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = (Item) getItem(position);

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.row_item_list, null);
        }

        String formatedDate = "";
        try {
            Date date = new SimpleDateFormat(MainActivity.DATE_FORMAT).parse(item.getDueDate());
            formatedDate = DateFormat.getDateInstance().format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TextView taskNameTextView = (TextView) convertView.findViewById(R.id.txv_taskname);
        taskNameTextView.setText(item.getTaskName());

        TextView statusTextView = (TextView) convertView.findViewById(R.id.txv_status);
        statusTextView.setText(item.getStatus().toString() + "  ");

        TextView priorityTextView = (TextView) convertView.findViewById(R.id.txv_priority);
        priorityTextView.setText("< " + item.getPriority().toString() + " >");
        Item.setColorOnPriority(mContext, priorityTextView, item);

        TextView dueTextView = (TextView) convertView.findViewById(R.id.txv_due);
        dueTextView.setText("Due: " + formatedDate);

        return convertView;
    }
}
