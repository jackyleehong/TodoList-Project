package example.com.todolist;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import example.com.todolist.db.TaskContract;
import example.com.todolist.db.TaskDBHelper;

public class TaskAddingActivity extends Activity implements View.OnClickListener,View.OnFocusChangeListener {
    private TaskDBHelper mydb;
    //String id_To_Update = 0;
    Button saveTask;
    EditText setTask;
    EditText setTime;
    EditText setDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_adding);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setTask = (EditText) findViewById(R.id.edtxtTask);
        setTime = (EditText) findViewById(R.id.edtxtTime);
        setDate = (EditText) findViewById(R.id.edtxtDate);
        saveTask = (Button) findViewById(R.id.saveBtn);




        mydb = new TaskDBHelper(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("task");
            if(mydb.getAllTask().contains(value)){
           /* int value = extras.getInt("id");
            if (value > 0) {*/
                getActionBar().setTitle("Task Info");
                Cursor rs = mydb.getData(value);
               // id_To_Update = value;
                rs.moveToFirst();
                Toast.makeText(this, rs + "", Toast.LENGTH_SHORT).show();
                String task = rs.getString(rs.getColumnIndex(TaskContract.Columns.TASK));
                String date = rs.getString(rs.getColumnIndex(TaskContract.Columns.DATE));
                String time = rs.getString(rs.getColumnIndex(TaskContract.Columns.TIME));

                if (!rs.isClosed()) {
                    rs.close();
                }
                Button b = (Button) findViewById(R.id.saveBtn);
                b.setVisibility(View.INVISIBLE);

                setTask.setText((CharSequence) task);
                setTask.setFocusable(false);
                setTask.setClickable(false);

                setDate.setText((CharSequence) date);
                setDate.setFocusable(false);
                setDate.setClickable(false);

                setTime.setText((CharSequence) time);
                setTime.setFocusable(false);
                setTime.setClickable(false);
            } else {
               // saveTask.setOnClickListener(this);
                setTime.setOnClickListener(this);
                setDate.setOnClickListener(this);
                setTime.setOnFocusChangeListener(this);
                setDate.setOnFocusChangeListener(this);
            }
        }

    }


    /* private void addTask() {
         EditText inputField =(EditText)findViewById(R.id.edtxtTask);
         EditText inputDate = (EditText)findViewById(R.id.edtxtDate);
         EditText inputTime = (EditText)findViewById(R.id.edtxtTime);
         String task = inputField.getText().toString();
         String date = inputDate.getText().toString();
         Log.d("Date", date);
         String time = inputTime.getText().toString();
         Log.d("Time", time);
         Log.d("MainActivity", task);
         TaskDBHelper helper = new TaskDBHelper(TaskAddingActivity.this);
         helper.insertTask(task,time,date);
         Intent intent = new Intent(TaskAddingActivity.this, MainActivity.class);
         startActivity(intent);
     }*/
    public void addTask(View v) {
       /* EditText inputField =(EditText)findViewById(R.id.edtxtTask);
        EditText inputDate = (EditText)findViewById(R.id.edtxtDate);
        EditText inputTime = (EditText)findViewById(R.id.edtxtTime);*/
        String task = setTask.getText().toString();
        String date = setDate.getText().toString();
        String time = setTime.getText().toString();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            String value = extras.getString("task");
            if(mydb.getAllTask().contains(value)){
           /* int value = extras.getInt("id");
            if (value > 0) {*/

                if (mydb.updateTask( value,task, time, date)) {
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (mydb.insertTask(task, time, date)) {
                    Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "not done", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onClick(View v) {
       if (v.getId() == R.id.edtxtTime) {
            setTime(v);
        } else if (v.getId() == R.id.edtxtDate) {
            setDate(v);
        } else {
            return;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == R.id.edtxtTime && hasFocus == true) {
            setTime(v);
        } else if (v.getId() == R.id.edtxtDate && hasFocus == true) {
            setDate(v);
        } else {
            return;
        }
    }

    public void setTime(View v) {
        DialogFragment newFragment = new SetTimeDialogFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void setDate(View v) {
        DialogFragment newFragment = new SetDateDialogFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task_adding, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.Edit_Task:
                getActionBar().setTitle("Edit Task");
                Button b = (Button) findViewById(R.id.saveBtn);
                b.setVisibility(View.VISIBLE);
                setTask.setEnabled(true);
                setTask.setFocusableInTouchMode(true);
                setTask.setClickable(true);

                setDate.setEnabled(true);
                setDate.setFocusableInTouchMode(true);
                setDate.setClickable(true);

                setTime.setEnabled(true);
                setTime.setFocusableInTouchMode(true);
                setTime.setClickable(true);
                setTime.setOnClickListener(this);
                setDate.setOnClickListener(this);
                setTime.setOnFocusChangeListener(this);
                setDate.setOnFocusChangeListener(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
