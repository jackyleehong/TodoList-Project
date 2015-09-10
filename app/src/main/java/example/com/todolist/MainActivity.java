package example.com.todolist;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import example.com.todolist.db.TaskContract;
import example.com.todolist.db.TaskDBHelper;

public class MainActivity extends ListActivity {
    int id = 1;

    private TaskDBHelper helper;
    private SimpleCursorAdapter listAdapter;
    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    public CheckBox reminderchk;
    private static MainActivity inst;
    private TextView alarmTextView;

 SQLiteDatabase sql;


   public static MainActivity instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reminderchk = (CheckBox)findViewById(R.id.reminderChk);
       // alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            updateList();







        // updateUI();
    }



    private void updateList() {
        ArrayList<String> array_list = new ArrayList<>();
        if(doesDatabaseExist(this, TaskContract.DB_NAME)) {

            TaskDBHelper dbh = new TaskDBHelper(this);
            array_list = dbh.getAllTask();

            final ListView lv = (ListView)findViewById(android.R.id.list);

            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.task_view,R.id.taskTextView,array_list);


            lv.setAdapter(adapter);
            if(dbh.getAllReminder()!= null) {
                setAlarm();
            }

            lv.setOnItemClickListener(new OnItemClickListenerListViewItem());
        }
        if(array_list.isEmpty()){
            RelativeLayout rl = (RelativeLayout)findViewById(R.id.mainLayout);
            TextView noTask = new TextView(this);
            noTask.setText("No Task added");
            noTask.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            noTask.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            noTask.setTextSize(30);
            rl.addView(noTask);
        }
    }

    public void setAlarm() {
        final TaskDBHelper alarmhelper = new TaskDBHelper(this);
        String[] alarm = new String[alarmhelper.getAllReminder().size()];
        alarm = alarmhelper.getAllReminder().toArray(alarm);
        for (final String eachAlarm : alarm) {

            //find the reminder time
            String[] splitA = eachAlarm.split("\\W+");
           final int hour = Integer.parseInt(splitA[0]);
            final int min = Integer.parseInt(splitA[1]);
           final String format = splitA[2];

            //find the task for the given reminder
            Cursor taskCursor = alarmhelper.findTask(eachAlarm);
            taskCursor.moveToFirst();
            String task = taskCursor.getString(taskCursor.getColumnIndex(TaskContract.Columns.TASK));
            if (!taskCursor.isClosed()) {
                taskCursor.close();
            }
            Log.d("task", task);
            Cursor idCursor = alarmhelper.findID(eachAlarm);
            idCursor.moveToFirst();
            final String id = idCursor.getString(idCursor.getColumnIndex(TaskContract.Columns._ID));
            if (!idCursor.isClosed()) {
                idCursor.close();
            }
                        Calendar calendar = Calendar.getInstance();
                        if (format == "AM" && hour == 12) {
                            calendar.set(Calendar.HOUR_OF_DAY, 0);
                        }
                        if (format == "PM" && hour == 12) {
                            calendar.set(Calendar.HOUR_OF_DAY, 12);
                        }else{
                            calendar.set(Calendar.HOUR_OF_DAY, hour);
                        }
                        calendar.set(Calendar.MINUTE, min);
                        calendar.set(Calendar.SECOND,0);
                        calendar.set(Calendar.MILLISECOND,0);
                        // calendar.setTimeInMillis(System.currentTimeMillis());
                        Log.d("time", hour + " " + min + " " + format + " ");
                        Intent myIntent = new Intent(MainActivity.this, AlarmReceiver.class);
                        Log.d("What is intent", myIntent + "");
                        pendingIntent = PendingIntent.getBroadcast(MainActivity.this,Integer.parseInt(id), myIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                        Log.d("id",id+"");
                        Log.d("What is pending intent", pendingIntent + "");


                    alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, pendingIntent);

                    Log.d("What is set", calendar.getTimeInMillis() + "");

                    }


    }
    public void setAlarmText(String alarmText) {
        Toast.makeText(this, alarmText + "", Toast.LENGTH_LONG).show();
    }


    public void onDoneButtonClick(View view){
        View v = (View) view.getParent();
        TextView taskTextView = (TextView) v.findViewById(R.id.taskTextView);
        String task = taskTextView.getText().toString();
        alarmManager.cancel(pendingIntent);
        // setAlarmText("");
        Log.d("MyActivity", "Alarm Off");

       // String sql = String.format("DELETE FROM %s WHERE %s ='%s'", TaskContract.TABLE, TaskContract.Columns.TASK, task);
        helper = new TaskDBHelper(MainActivity.this);
        helper.deleteTask(task);
        Log.d("Task delete", "Deleted");

       /* SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);*/
       updateList();
    }

    private static boolean doesDatabaseExist(ContextWrapper context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }
    public void onDestroy(){
        super.onDestroy();
    finish();

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
      switch(item.getItemId()){
          case R.id.action_add_task:
              Bundle dataBundle = new Bundle();
              dataBundle.putInt("id", 0);
              Intent addNewTask = new Intent(this, TaskAddingActivity.class);
              addNewTask.putExtras(dataBundle);
              startActivity(addNewTask);
             /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
              builder.setTitle("Add a task");
              builder.setMessage("What do you want to do? ");
              final EditText inputField =new EditText(this);
              builder.setView(inputField);
              builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                      String task = inputField.getText().toString();
                      Log.d("MainActivity", task);
                      TaskDBHelper helper = new TaskDBHelper(MainActivity.this);
                      SQLiteDatabase db = helper.getWritableDatabase();
                      ContentValues values = new ContentValues();

                      values.clear();
                      values.put(TaskContract.Columns.TASK, task);

                      db.insertWithOnConflict(TaskContract.TABLE,null, values,SQLiteDatabase.CONFLICT_IGNORE);
                      Intent intent = new Intent(MainActivity.this, MainActivity.class);
                      startActivity(intent);
                  }
              });

              builder.setNegativeButton("Cancel", null);
              builder.create().show();
*/

              return true;
          default:
              return false;
      }
    }
}
