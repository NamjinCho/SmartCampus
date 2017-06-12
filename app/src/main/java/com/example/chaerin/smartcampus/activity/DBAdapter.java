package com.example.chaerin.smartcampus.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



/**
 * Created by cjf90 on 2016-08-18.
 */
public class DBAdapter {
    static final String KEY_ROWID = "_id";
    static final String KEY_NAME = "name";
    static final String KEY_TIME = "time";
    static final String TAG = "DBAdapter";
    static final String DATABASE_NAME = "MyDB";
    static final String DATABASE_TABLE = "contacts";
    static final int DATABASE_VERSION = 2;
    static final String DATABASE_CREATE =
            "create table " + DATABASE_TABLE + " (_id text, "
                    + "name text not null, time text not null);";
    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;
    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("테이블 생성","확인");
            try {
                db.execSQL(DATABASE_CREATE);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }
    // ---opens the database---
    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }
    // ---closes the database---
    public void close() {
        DBHelper.close();
    }
    public long insertContact(String i, String nam,String tim) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ROWID, i);
        initialValues.put(KEY_NAME, nam);
        initialValues.put(KEY_TIME, tim);
        Log.d("디비1",i+" "+nam+" "+tim);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
    // ---deletes a particular contact---
    public boolean deleteAllContact() {
        return db.delete(DATABASE_TABLE,null,null) > 0;
    }
    // ---retrieves all the contacts---
    public Cursor getAllContacts() {
        return db.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NAME,
                KEY_TIME }, null, null, null, null, null);
    }
}