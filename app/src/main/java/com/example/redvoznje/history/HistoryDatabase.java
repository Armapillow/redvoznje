package com.example.redvoznje.history;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HistoryDatabase {
    private final SQLiteDatabase db;
    private static final String TABLE_HISTORY = "history";
    public static final String ROW_ID = "_id";
    public static final String ROW_FROM_ID = "fromID";
    public static final String ROW_TO_ID = "toID";
    public static final String ROW_FROM_STATION = "fromStation";
    public static final String ROW_TO_STATION = "toStation";

    public HistoryDatabase(Context context) {
        CustomHistoryHelper helper = new CustomHistoryHelper(context);
        db = helper.getWritableDatabase();
    }

    public void insert(String idFrom, String idTo, String from, String to) {
        String insertQuery = "INSERT OR IGNORE INTO " + TABLE_HISTORY + " ("
                + ROW_FROM_ID + ", " + ROW_TO_ID + ", "
                + ROW_FROM_STATION + ", " + ROW_TO_STATION + ") "
                + "VALUES ("
                + "'" + idFrom + "', "
                + "'" + idTo   + "', "
                + "'" + from + "', "
                + "'" + to + "'" + ");";

        Log.i("insert() = ", insertQuery);
        db.execSQL(insertQuery);
    }

    public Cursor selectAllStations() {
        String selectAllQuery = "SELECT * FROM " + TABLE_HISTORY;

        Log.i("select() = ", selectAllQuery);
        return db.rawQuery(selectAllQuery, null);
    }

    public void deleteAll() {
        db.delete(TABLE_HISTORY, null, null);
        db.close();
    }


    private class CustomHistoryHelper extends SQLiteOpenHelper {
        private static final String DB_NAME = "redvoznje";
        private static final int    DB_VERSION = 1;
        private static final String DB_CREATE = "CREATE TABLE " + TABLE_HISTORY + " ("
                + ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ROW_FROM_ID + " TEXT NOT NULL, "
                + ROW_TO_ID + " TEXT NOT NULL, "
                + ROW_FROM_STATION + " TEXT NOT NULL, "
                + ROW_TO_STATION + " TEXT NOT NULL, "
                + "CONSTRAINT UNIQUE_FROM_TO UNIQUE ("
                + ROW_FROM_ID + ", " + ROW_TO_ID + ")"
                + ");";

        public CustomHistoryHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
