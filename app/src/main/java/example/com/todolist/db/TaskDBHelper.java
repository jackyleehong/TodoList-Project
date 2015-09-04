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
        String sqlQuery = String.format("CREATE TABLE %s (" + "%s INTEGER PRIMARY KEY AUTOINCREMENT, " + "%s TEXT , %s TEXT ,%s TEXT )",TaskContract.TABLE,TaskContract.Columns._ID,TaskContract.Columns.TASK,TaskContract.Columns.DATE,TaskContract.Columns.TIME);
        Log.d("TaskDBHelper", "Query to form table: " + sqlQuery);
        sqlDB.execSQL(sqlQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2){
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TaskContract.TABLE);
        onCreate(sqlDB);
    }


    public boolean insertTask(String task, String time, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskContract.Columns.TASK, task);
        contentValues.put(TaskContract.Columns.DATE,date);
        contentValues.put(TaskContract.Columns.TIME, time);
        db.insert(TaskContract.TABLE, null, contentValues);
        return true;
    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(String.format("SELECT * FROM %s WHERE id = %s",TaskContract.TABLE,TaskContract.Columns._ID),null);
        return res;
    }

    public int numberOfRows(){

        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db,TaskContract.TABLE);
        return numRows;
    }
    public boolean updateTask(Integer id,String task, String time, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskContract.Columns.TASK, task);
        contentValues.put(TaskContract.Columns.DATE,date);
        contentValues.put(TaskContract.Columns.TIME,time);
        db.update(TaskContract.TABLE, contentValues, "id= ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public Integer deleteTask(Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TaskContract.TABLE, "id = ? ", new String[]{Integer.toString(id)});

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


}
