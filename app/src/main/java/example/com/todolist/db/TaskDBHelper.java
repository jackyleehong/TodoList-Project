package example.com.todolist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Lee on 2/9/2015.
 */
public class TaskDBHelper extends SQLiteOpenHelper {

    public TaskDBHelper(Context context){
        super(context,TaskContract.DB_NAME,null,TaskContract.DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB){
        String sqlQuery = String.format("CREATE TABLE %s (" + "%s INTEGER PRIMARY KEY AUTOINCREMENT, " + "%s TEXT , %s TEXT ,%s TEXT, %s TEXT )",TaskContract.TABLE,TaskContract.Columns._ID,TaskContract.Columns.TASK,TaskContract.Columns.DATE,TaskContract.Columns.TIME,TaskContract.Columns.Reminder);
        Log.d("TaskDBHelper", "Query to form table: " + sqlQuery);
        sqlDB.execSQL(sqlQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2){
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TaskContract.TABLE);
        onCreate(sqlDB);
    }


    public boolean insertTask(String task, String time, String date,String reminder){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskContract.Columns.TASK, task);
        contentValues.put(TaskContract.Columns.DATE,date);
        contentValues.put(TaskContract.Columns.TIME, time);
        contentValues.put(TaskContract.Columns.Reminder, reminder);
        db.insert(TaskContract.TABLE, null, contentValues);
        return true;
    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(String.format("SELECT * FROM %s WHERE id = %s",TaskContract.TABLE,TaskContract.Columns._ID),null);
        return res;
    }
    public Cursor getData(String task){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(String.format("SELECT * FROM %s WHERE %s = '%s'", TaskContract.TABLE, TaskContract.Columns.TASK,task), null);
        return res;
    }
    public Cursor findTask(String reminder){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(String.format("SELECT %s FROM %s WHERE %s = '%s'",TaskContract.Columns.TASK ,TaskContract.TABLE, TaskContract.Columns.Reminder,reminder), null);
        return res;
    }
    public Cursor findID(String reminder){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(String.format("SELECT %s FROM %s WHERE %s = '%s'",TaskContract.Columns._ID ,TaskContract.TABLE, TaskContract.Columns.Reminder,reminder), null);
        return res;
    }

    public int numberOfRows(){

        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db,TaskContract.TABLE);
        return numRows;
    }
    public boolean updateTask(String oriTask,String task, String time, String date,String reminder){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskContract.Columns.TASK, task);
        contentValues.put(TaskContract.Columns.DATE,date);
        contentValues.put(TaskContract.Columns.TIME,time);
        contentValues.put(TaskContract.Columns.Reminder, reminder);
        db.update(TaskContract.TABLE, contentValues, "task= ? ", new String[]{oriTask});
        return true;
    }

    public Integer deleteTask(String task){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TaskContract.TABLE, "task = ? ", new String[]{task});

    }
    public ArrayList<String> getAllTask()
    {
        ArrayList<String> array_list = new ArrayList<String>();
        ArrayList<String> array_list1 = new ArrayList<String>();
        ArrayList<String> array_list2 = new ArrayList<String>();
        ArrayList<String> listAddAll = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( String.format("select * from %s", TaskContract.TABLE),null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(TaskContract.Columns.TASK)));
          /*  array_list1.add(res.getString(res.getColumnIndex(TaskContract.Columns.DATE)));
            array_list2.add(res.getString(res.getColumnIndex(TaskContract.Columns.TIME)));
            listAddAll.addAll(array_list);
            listAddAll.addAll(array_list1);
            listAddAll.addAll(array_list2);*/

            res.moveToNext();
        }
        return array_list;
    }
    public ArrayList<String> getAllReminder()
    {
        ArrayList<String> array_list = new ArrayList<String>();
        ArrayList<String> array_list1 = new ArrayList<String>();
        ArrayList<String> array_list2 = new ArrayList<String>();
        ArrayList<String> listAddAll = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( String.format("select * from %s", TaskContract.TABLE),null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(TaskContract.Columns.Reminder)));
          /*  array_list1.add(res.getString(res.getColumnIndex(TaskContract.Columns.DATE)));
            array_list2.add(res.getString(res.getColumnIndex(TaskContract.Columns.TIME)));
            listAddAll.addAll(array_list);
            listAddAll.addAll(array_list1);
            listAddAll.addAll(array_list2);*/

            res.moveToNext();
        }
        return array_list;
    }



}
