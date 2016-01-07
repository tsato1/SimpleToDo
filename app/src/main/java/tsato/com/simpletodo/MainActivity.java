package tsato.com.simpletodo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private final static int REQUEST_EDIT = 0;
    public  final static String DATE_FORMAT = "yyyy-MM-dd";

    private ArrayList<Item> mItemList = null;
    private ListView mItemListView = null;
    private ItemListAdapter mItemListAdapter = null;
    private DBAdapter mDBAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mItemList = new ArrayList<>();
        mItemListView = (ListView) findViewById(R.id.lsv_todos);
        mItemListAdapter = new ItemListAdapter(this, 0, mItemList);
        mItemListView.setAdapter(mItemListAdapter);
        mItemListView.setOnItemClickListener(new ListItemClickListener());
        mItemListView.setOnItemLongClickListener(new ListItemLongClickListener());

        mDBAdapter = new DBAdapter(this);
        loadItems();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadItems();
    }

    public class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View itemDetailView = layoutInflater.inflate(R.layout.dialog_item_detail, null);

            final Item item = (Item) parent.getItemAtPosition(position);

            String formatedDate = "";
            try {
                Date date = new SimpleDateFormat(DATE_FORMAT).parse(item.getDueDate());
                formatedDate = DateFormat.getInstance().format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ((TextView) itemDetailView.findViewById(R.id.txv_taskname)).setText(item.getTaskName());
            ((TextView) itemDetailView.findViewById(R.id.txv_due)).setText(formatedDate);
            ((TextView) itemDetailView.findViewById(R.id.txv_memo)).setText(item.getMemo());
            ((TextView) itemDetailView.findViewById(R.id.txv_priority)).setText(item.getPriority().toString());
            ((TextView) itemDetailView.findViewById(R.id.txv_status)).setText(item.getStatus().toString());

            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setIcon(R.mipmap.ic_launcher);
            dialog.setTitle(getApplicationContext().getString(R.string.title_item_detail));
            dialog.setView(itemDetailView);
            dialog.setPositiveButton(R.string.action_edit, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MainActivity.this, EditActivity.class);
                    intent.putExtra("itemSerializable", item);
                    startActivityForResult(intent, REQUEST_EDIT);
                }
            });
            dialog.show();
        }
    }

    public class ListItemLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            final Item item = (Item) parent.getItemAtPosition(position);

            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setIcon(R.mipmap.ic_launcher);
            dialog.setTitle(getApplicationContext().getString(R.string.warning_delete_item));
            dialog.setPositiveButton(R.string.action_delete, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mDBAdapter.open();
                    if (mDBAdapter.deleteItem(Integer.parseInt(item.getId()))) {
                        Toast.makeText(getApplicationContext(), getString(R.string.info_delete_successful), Toast.LENGTH_SHORT).show();
                    }
                    mDBAdapter.close();
                    loadItems();
                }
            });
            dialog.show();
            return true;
        }
    }

    private void loadItems() {
        mItemList.clear();
        mDBAdapter.open();
        Cursor c = mDBAdapter.getAllItems();

        if (c.moveToFirst()) {
            do {
                Item item = new Item(
                        c.getString(c.getColumnIndex(DBAdapter.COL_ID)),
                        c.getString(c.getColumnIndex(DBAdapter.COL_TASKNAME)),
                        c.getString(c.getColumnIndex(DBAdapter.COL_DUEDATE)),
                        c.getString(c.getColumnIndex(DBAdapter.COL_MEMO)),
                        Item.Priority.valueOf(c.getString(c.getColumnIndex(DBAdapter.COL_PRIORITY))),
                        Item.Status.valueOf(c.getString(c.getColumnIndex(DBAdapter.COL_STATUS)))
                );
                mItemList.add(item);
            } while (c.moveToNext());
        }

        mDBAdapter.close();
        mItemListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = menuItem.getItemId();

        if (id == R.id.action_new) {
            Item item = new Item(null, null, null, null, Item.Priority.HIGH, Item.Status.TODO);
            Intent intent = new Intent(this, EditActivity.class);
            intent.putExtra("itemSerializable", item);
            startActivityForResult(intent, REQUEST_EDIT);
            return true;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT) {
            loadItems();
        }
    }
}
