package example.com.todolist;

import android.app.ListActivity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import example.com.todolist.db.TaskContract;
import example.com.todolist.db.TaskDBHelper;

public class MainActivity extends ListActivity {

    private TaskDBHelper helper;
    private SimpleCursorAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    /*  private void updateUI() {
          helper = new TaskDBHelper(MainActivity.this);
          SQLiteDatabase sqlDB = helper.getReadableDatabase();
          Cursor cursor = sqlDB.query(TaskContract.TABLE, new String[]{TaskContract.Columns._ID, TaskContract.Columns.TASK}, null, null, null, null, null);

          listAdapter = new SimpleCursorAdapter(
                  this,
                  R.layout.task_view,
                  cursor,
                  new String[]{TaskContract.Columns.TASK},
                  new int[]{R.id.taskTextView},
                  0
          );
        // this.setListAdapter(listAdapter);
        //  ArrayAdapterItem adapter= new ArrayAdapterItem(this, R.layout.list_view_row_item,ObjectItemData);

          final ListView listViewItems = (ListView)findViewById(R.id.mainList);
          listViewItems.setAdapter(listAdapter);
          listViewItems.setOnItemClickListener(new OnItemClickListenerListViewItem());
      }*/

    public void onDoneButtonClick(View view){
        View v = (View) view.getParent();
        TextView taskTextView = (TextView) v.findViewById(R.id.taskTextView);
        String task = taskTextView.getText().toString();

        String sql = String.format("DELETE FROM %s WHERE %s ='%s'", TaskContract.TABLE, TaskContract.Columns.TASK, task);
        helper = new TaskDBHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);
       updateList();
    }

    private static boolean doesDatabaseExist(ContextWrapper context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
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
