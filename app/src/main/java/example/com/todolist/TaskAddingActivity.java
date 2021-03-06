package example.com.todolist;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;
import java.util.GregorianCalendar;

import example.com.todolist.db.TaskContract;
import example.com.todolist.db.TaskDBHelper;

public class TaskAddingActivity extends Activity implements View.OnClickListener,View.OnFocusChangeListener,SetTimeDialogFragment.OnButtonClickedListener {
    private TaskDBHelper mydb;
    //String id_To_Update = 0;
    Button saveTask;
    EditText setTask;
    EditText setDescription;
    EditText setTime;
    EditText setDate;
    EditText setReminder;
    CheckBox reminderchk;
    int reminderHour = 0,reminderMin = 0 ;
    AlarmManager alarmManager;
    private static TaskAddingActivity inst;
    private PendingIntent pendingIntent;
  //  private TextView alarmTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_adding);
        reminderchk = (CheckBox)findViewById(R.id.reminderChk);
        getActionBar().setDisplayHomeAsUpEnabled(true);

      //  alarmTextView = (TextView) findViewById(R.id.alarmText);
        setTask = (EditText) findViewById(R.id.edtxtTask);
        setDescription = (EditText) findViewById(R.id.edtxtDescription);
        setTime = (EditText) findViewById(R.id.edtxtTime);
        setDate = (EditText) findViewById(R.id.edtxtDate);
        setReminder = (EditText) findViewById(R.id.reminderTime);
        saveTask = (Button) findViewById(R.id.saveBtn);
      //  alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);



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
                String description = rs.getString(rs.getColumnIndex(TaskContract.Columns.DESCRIPTION));
                String date = rs.getString(rs.getColumnIndex(TaskContract.Columns.DATE));
                String time = rs.getString(rs.getColumnIndex(TaskContract.Columns.TIME));
                String reminder = rs.getString(rs.getColumnIndex(TaskContract.Columns.Reminder));

                if (!rs.isClosed()) {
                    rs.close();
                }
                Button b = (Button) findViewById(R.id.saveBtn);
                b.setVisibility(View.INVISIBLE);

                setTask.setText((CharSequence) task);
                setTask.setFocusable(false);
                setTask.setClickable(false);

                setDescription.setText((CharSequence) description);
                setDescription.setFocusable(false);
                setDescription.setClickable(false);

                setDate.setText((CharSequence) date);
                setDate.setFocusable(false);
                setDate.setClickable(false);

                setTime.setText((CharSequence) time);
                setTime.setFocusable(false);
                setTime.setClickable(false);

                setReminder.setText((CharSequence) reminder);


            } else {
                setReminder.setEnabled(false);
                setReminder.setFocusableInTouchMode(false);
                setReminder.setClickable(false);
                reminderchk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked == true) {
                            setReminder.setEnabled(true);
                            setReminder.setFocusableInTouchMode(true);
                            setReminder.setClickable(true);
                          //  setReminder.setOnClickListener(setReminder.getCl);

                        } else {
                            setReminder.setEnabled(false);
                            setReminder.setFocusableInTouchMode(false);
                            setReminder.setClickable(false);
                            setReminder.setFocusable(false);

                        }
                    }
                });
               // saveTask.setOnClickListener(this);
                setTime.setOnClickListener(this);
                setDate.setOnClickListener(this);
                setTime.setOnFocusChangeListener(this);
                setDate.setOnFocusChangeListener(this);
                setReminder.setOnClickListener(this);
                setReminder.setOnFocusChangeListener(this);


            }
        }

    }
   /* public void onToggleClicked(View view) {
        if (((ToggleButton) view).isChecked()) {
            Log.d("MyActivity", "Alarm On");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
            Intent myIntent = new Intent(TaskAddingActivity.this, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(TaskAddingActivity.this, 0, myIntent, 0);
            alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
            setAlarmText("");
            Log.d("MyActivity", "Alarm Off");
        }
    }*/
    public void setAlarmText(String alarmText) {
       Toast.makeText(this, alarmText + "", Toast.LENGTH_LONG).show();
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
        String description = setDescription.getText().toString();
        String date = setDate.getText().toString();
        String time = setTime.getText().toString();
        String reminder = setReminder.getText().toString();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            String value = extras.getString("task");
            if(mydb.getAllTask().contains(value)){
           /* int value = extras.getInt("id");
            if (value > 0) {*/

                if (mydb.updateTask( value,task, description, time, date,reminder)) {
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                 /*   if (reminder != "") {
                        Log.d("MyActivity", "Alarm On");
                       MainActivity m = new MainActivity();

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, reminderHour);
                        calendar.set(Calendar.MINUTE, reminderMin);
                        calendar.set(Calendar.SECOND,0);
                        Intent myIntent = new Intent(this, AlarmReceiver.class);
                        pendingIntent = PendingIntent.getBroadcast(TaskAddingActivity.this, 0, myIntent, 0);
                        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 1000 * 60 * 1, pendingIntent);
                    } else {
                        alarmManager.cancel(pendingIntent);
                        setAlarmText("");
                        Log.d("MyActivity", "Alarm Off");
                    }*/
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (mydb.insertTask(task, description, time, date,reminder)) {
                    /*if (reminder != "") {
                        Log.d("MyActivity", "Alarm On");
                       // TaskAddingActivity t = new TaskAddingActivity();

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.set(Calendar.HOUR_OF_DAY, reminderHour);
                        Log.d("hour", reminderHour + "");

                        calendar.set(Calendar.MINUTE, reminderMin);
                        Log.d("minute", reminderMin + "");
                      //  calendar.set(Calendar.SECOND, 0);
                    //    calendar.getTime();
                        Intent myIntent = new Intent(TaskAddingActivity.this, AlarmReceiver.class);
                        pendingIntent = PendingIntent.getBroadcast(TaskAddingActivity.this, 0, myIntent, 0);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),*//* 1000 * 60 * 2,*//* pendingIntent);
                    } else {
                        alarmManager.cancel(pendingIntent);
                        setAlarmText("");
                        Log.d("MyActivity", "Alarm Off");
                    }*/
                    Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "not done", Toast.LENGTH_SHORT).show();
                }
                setAlarm();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }
    }

//    public void setAlarm(){
//        String time[] = setTime.getText().toString().split(":");
//        int timeInSecond = Integer.parseInt(time[0])*60*60 + Integer.parseInt(time[1])*60;
//        Toast.makeText(this, timeInSecond, Toast.LENGTH_SHORT).show();
//        Long alertTime = new GregorianCalendar().getTimeInMillis() + timeInSecond*1000;
//        Intent alert = new Intent(this, AlarmReceiver.class);
//        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime, pendingIntent.getBroadcast(this, 1, alert, pendingIntent.FLAG_UPDATE_CURRENT));
//    }
    public void setAlarm(){
        Intent intent = new Intent(this, NotifyService.class);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        String date[] = setDate.getText().toString().split("/");
        String time[] = setReminder.getText().toString().replaceAll(" ", ":").split(":");
        Log.d("date", date[0] + ":" + date[1] + ":" + time[0] + ":" + time[1] + ":" + time[2]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));
        calendar.set(Calendar.HOUR, Integer.parseInt(time[0]));
        if(time[2].equals("AM"))
            calendar.set(Calendar.AM_PM, Calendar.AM);
        else
            calendar.set(Calendar.AM_PM, Calendar.PM);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(date[1]) - 1);
        calendar.set(Calendar.YEAR, Integer.parseInt(date[2]));
        Log.d("time", String.valueOf(calendar.getTimeInMillis()));
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000*60*60*24, pendingIntent);
    }

    public void setText(StringBuilder str,int hourOfDay,int min){
        if(setTime.isFocused()){
            setTime.setText(str);
            //setTime.getNextFocusDownId();
        }else if(setReminder.isFocused()){
            setReminder.setText(str);
            reminderHour = hourOfDay;
            reminderMin = min;
        }
    }



    @Override
    public void onClick(View v) {
       if (v.getId() == R.id.edtxtTime) {
            setTime(v);
        } else if (v.getId() == R.id.edtxtDate) {
            setDate(v);
        } else if (v.getId() == R.id.reminderTime) {
           setTime(v);
       }else {
            return;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == R.id.edtxtTime && hasFocus == true) {
            setTime(v);
        } else if (v.getId() == R.id.edtxtDate && hasFocus == true) {
            setDate(v);
        } else if (v.getId() == R.id.reminderTime && hasFocus == true) {
            setTime(v);
        }else {
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

                setDescription.setEnabled(true);
                setDescription.setFocusableInTouchMode(true);
                setDescription.setClickable(true);

                setDate.setEnabled(true);
                setDate.setFocusableInTouchMode(true);
                setDate.setClickable(true);

                setTime.setEnabled(true);
                setTime.setFocusableInTouchMode(true);
                setTime.setClickable(true);
                reminderchk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked == true) {
                            setReminder.setEnabled(true);
                            setReminder.setFocusableInTouchMode(true);
                            setReminder.setClickable(true);

                        } else {
                            setReminder.setEnabled(false);
                            setReminder.setFocusableInTouchMode(false);
                            setReminder.setClickable(false);
                        }
                    }
                });


                setTime.setOnClickListener(this);
                setDate.setOnClickListener(this);
                setTime.setOnFocusChangeListener(this);
                setDate.setOnFocusChangeListener(this);
                setReminder.setOnClickListener(this);
                setReminder.setOnFocusChangeListener(this);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
