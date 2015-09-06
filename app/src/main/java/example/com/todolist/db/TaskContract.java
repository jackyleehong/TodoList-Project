package example.com.todolist.db;

import android.provider.BaseColumns;

/**
 * Created by Lee on 2/9/2015.
 */
public class TaskContract {
    public static final String DB_NAME =  "example.com.todolist.db.TODOLIST";
    public static final int DB_VERSION =1;
    public static final String TABLE = "tasks";

    public class Columns{
        public static final String TASK = "task";
        public static final String _ID = BaseColumns._ID;
        public static final String TIME = "time";
        public static final String DATE = "date";
        public static final String Reminder = "reminder";
    }
}
