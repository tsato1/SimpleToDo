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
import android.widget.Toast;
import android.util.Log;

import java.text.DateFormat;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mTaskNameEditText = (EditText) findViewById(R.id.edt_taskname);
        mDueDatePicker = (DatePicker) findViewById(R.id.date_picker);
        mMemoEditText = (EditText) findViewById(R.id.edt_memo);
        mPrioritySpinner = (Spinner) findViewById(R.id.spn_priority);
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
        DBAdapter dbAdapter = new DBAdapter(this);

        Item item = new Item(
                "",
                mTaskNameEditText.getText().toString(),
                DateFormat.getDateInstance().format(mDueDatePicker.getCalendarView().getDate()),
                mMemoEditText.getText().toString(),
                mSelectedPriority,
                mSelectedStatus
        );

        Log.d("taskName", mTaskNameEditText.getText().toString());
        Log.d("date", DateFormat.getDateInstance().format(mDueDatePicker.getCalendarView().getDate()));
        Log.d("memo", mMemoEditText.getText().toString());
        Log.d("priority", mSelectedPriority.toString());
        Log.d("status", mSelectedStatus.toString());

        dbAdapter.open();
        if (dbAdapter.saveItem(item)) {
            Toast.makeText(this, getString(R.string.info_save_successful), Toast.LENGTH_SHORT).show();
        };
        dbAdapter.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {
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
