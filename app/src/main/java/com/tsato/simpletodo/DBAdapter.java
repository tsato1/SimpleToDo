package com.tsato.simpletodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by T on 2016/01/04.
 */
public class DBAdapter {
    private static final String DATABASE_NAME = "simpletodo.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_ITEM = "items";
    public static final String COL_ID = "_id";
    public static final String COL_TASKNAME = "taskname";
    public static final String COL_DUEDATE = "duedate";
    public static final String COL_MEMO = "memo";
    public static final String COL_PRIORITY = "priority";
    public static final String COL_STATUS = "status";

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public DBAdapter (Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(
                        "CREATE TABLE " + TABLE_ITEM + " ("
                        + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + COL_TASKNAME + " TEXT NOT NULL,"
                        + COL_DUEDATE + " TEXT NOT NULL,"
                        + COL_MEMO + " TEXT NOT NULL,"
                        + COL_PRIORITY + " INTEGER,"
                        + COL_STATUS + " INTEGER);"
                );
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
            onCreate(db);
        }
    }

    public DBAdapter open() {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public boolean deleteItem(int id) {
        return db.delete(TABLE_ITEM, COL_ID + "=" + id, null) > 0;
    }

    public boolean deleteAllItems() {
        return db.delete(TABLE_ITEM, null, null) > 0;
    }

    public Cursor getAllItems() {
        return db.query(TABLE_ITEM, null, null, null, null, null, null);
    }

    public Cursor getAllitemsInOrder() {
        String query = "SELECT * FROM " + TABLE_ITEM + " ORDER BY " + COL_STATUS + " ASC, " +COL_PRIORITY + " ASC, " + COL_DUEDATE + " ASC LIMIT 100";
        return db.rawQuery(query, new String[] {});
    }

    public boolean saveItem(Item item) {
        ContentValues values = new ContentValues();
        values.put(COL_ID, item.getId());
        values.put(COL_TASKNAME, item.getTaskName());
        values.put(COL_DUEDATE, item.getDueDate());
        values.put(COL_MEMO, item.getMemo());
        values.put(COL_PRIORITY, item.getPriority().ordinal());
        values.put(COL_STATUS, item.getStatus().ordinal());

        try {
            db.replace(TABLE_ITEM, null, values);
            return  true;
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        }

        return false;
    }
}
