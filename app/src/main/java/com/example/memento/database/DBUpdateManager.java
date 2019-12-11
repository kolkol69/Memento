package com.example.memento.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.memento.model.ModelTask;

public class DBUpdateManager {

    private SQLiteDatabase database;

    DBUpdateManager(SQLiteDatabase database) {
        this.database = database;
    }

    public void title(long timeStamp, String title) {
        update(DBHelper.TASK_TITLE_COLUMN, timeStamp, title);
    }

    public void date(long timeStamp, long date) {
        update(DBHelper.TASK_DATE_COLUMN, timeStamp, date);
    }

    public void priority(long timeStamp, int priority) {
        update(DBHelper.TASK_PRIORITY_COLUMN, timeStamp, priority);
    }

    public void status(long timeStamp, int status) {
        update(DBHelper.TASK_STATUS_COLUMN, timeStamp, status);
    }

    public void task(ModelTask task) {
        title(task.getTimeStamp(), task.getTitle());
        date(task.getTimeStamp(), task.getDate());
        priority(task.getTimeStamp(), task.getPriority());
        status(task.getTimeStamp(), task.getStatus());
    }

    private int update(String column, long key, String value) {
        Log.d(">>>String",""+value);
        ContentValues cv = new ContentValues();
        cv.put(column, value);
        return database.update(DBHelper.TASK_TABLE, cv, DBHelper.TASK_TIME_STAMP_COLUMN + " = " + key, null);
        //Log.d(">>>String",""+database.toString());
    }

    private void update(String column, long key, long value) {
        Log.d(">>>long",""+key);
        ContentValues cv = new ContentValues();
        cv.put(column, value);
        int affected = database.update(DBHelper.TASK_TABLE, cv, DBHelper.TASK_TIME_STAMP_COLUMN + " = " + key, null);
        Log.d(">>>affected","RESULT: "+affected);
    }
}
