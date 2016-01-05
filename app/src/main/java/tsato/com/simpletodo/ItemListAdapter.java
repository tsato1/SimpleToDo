package tsato.com.simpletodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

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

        TextView taskNameTxv = (TextView) convertView.findViewById(R.id.txv_taskname);
        taskNameTxv.setText(item.getTaskName());

        TextView priorityTxv = (TextView) convertView.findViewById(R.id.txv_priority);
        priorityTxv.setText(item.getPriority().toString());

        TextView dueTxv = (TextView) convertView.findViewById(R.id.txv_due);
        dueTxv.setText(item.getDueDate());

        return convertView;
    }
}
