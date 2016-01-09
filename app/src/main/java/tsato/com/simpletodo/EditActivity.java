package tsato.com.simpletodo;

import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by T on 2016/01/04.
 */
public class EditActivity extends AppCompatActivity {
    private EditText mTaskNameEditText;
    private DatePicker mDueDatePicker;
    private EditText mMemoEditText;
    private Spinner mPrioritySpinner;
    private Spinner mStatusSpinner;
    private Item.Priority mSelectedPriority;
    private Item.Status mSelectedStatus;
    private String mItemId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        Item item = (Item) extras.getSerializable("itemSerializable");
        mItemId = item.getId();

        mTaskNameEditText = (EditText) findViewById(R.id.edt_taskname);
        mTaskNameEditText.setText(item.getTaskName());
        mDueDatePicker = (DatePicker) findViewById(R.id.date_picker);
        if (item.getDueDate() != null) {
            int year = Integer.parseInt(item.getDueDate().substring(0, 4));
            int month = Integer.parseInt(item.getDueDate().substring(5, 7));
            int date = Integer.parseInt(item.getDueDate().substring(8, 10));
            mDueDatePicker.updateDate(year, month-1, date);
            //Log.d("EditActivity", "year: " + year + ", month: " + month + ", date: " + date);
        }
        mMemoEditText = (EditText) findViewById(R.id.edt_memo);
        mMemoEditText.setText(item.getMemo());
        mPrioritySpinner = (Spinner) findViewById(R.id.spn_priority);
        mPrioritySpinner.setSelection(Item.Priority.valueOf(item.getPriority().toString()).ordinal());
        mPrioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedPriority = Item.Priority.valueOf(parent.getItemAtPosition(position).toString().toUpperCase());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mStatusSpinner = (Spinner) findViewById(R.id.spn_status);
        mStatusSpinner.setSelection(Item.Status.valueOf(item.getStatus().toString()).ordinal());
        mStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedStatus = Item.Status.valueOf(parent.getItemAtPosition(position).toString().toUpperCase());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private boolean isReadyToSave() {
        if ("".equals(mTaskNameEditText.getText().toString())) {
            Toast.makeText(this, getString(R.string.error_enter_taskname), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void doSaveItem() {
        Item item = new Item(
                mItemId,
                mTaskNameEditText.getText().toString(),
                new SimpleDateFormat(MainActivity.DATE_FORMAT).format(mDueDatePicker.getCalendarView().getDate()),
                mMemoEditText.getText().toString(),
                mSelectedPriority,
                mSelectedStatus
        );

        Log.d("EditActivity on save",
                "task name: " + mTaskNameEditText.getText().toString() + ", "
                        + "date: " + DateFormat.getDateInstance().format(mDueDatePicker.getCalendarView().getDate()) + ", "
                        + "memo: " + mMemoEditText.getText().toString() + ", "
                        + "priority: " + mSelectedPriority.toString() + ", "
                        + "status: " + mSelectedStatus.toString());

        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        if (dbAdapter.saveItem(item)) {
            Toast.makeText(this, getString(R.string.info_save_successful), Toast.LENGTH_SHORT).show();
        };
        dbAdapter.close();

        mItemId = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.action_save) {
            if (isReadyToSave()) {
                doSaveItem();
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
