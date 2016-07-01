package com.example.pavlo.messanger.local_db_manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.pavlo.messanger.util.Config;

/**
 * Created by pavlo on 30.06.16.
 */
public class LocalDatabaseHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = "Local database";

    private SQLiteDatabase database;

    public LocalDatabaseHelper(Context context) {
        super(context, Config.DATABASE_NAME, null, Config.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "onCreate database");

        db.execSQL("create table messages ("
                + "id integer primary key autoincrement,"
                + "messae text,"
                + "phone text"
                + "status text"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
 }
