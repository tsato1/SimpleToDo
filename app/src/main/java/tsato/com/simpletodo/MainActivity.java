package tsato.com.simpletodo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final static int REQUEST_EDIT = 1;

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

    private void loadItems() {
        mItemList.clear();
        mDBAdapter.open();
        Cursor c = mDBAdapter.getAllItems();

        if (c.moveToFirst()) {
            do {
                Item item = new Item(
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new) {
            Intent intent = new Intent(this, EditActivity.class);
            startActivityForResult(intent, REQUEST_EDIT);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT) {
            loadItems();
        }
    }
}
