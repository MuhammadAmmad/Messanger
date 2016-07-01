package com.example.pavlo.messanger.local_db_manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.pavlo.messanger.remote_db_manager.models.Message;
import com.example.pavlo.messanger.util.Config;

import java.util.ArrayList;

/**
 * Created by pavlo on 01.07.16.
 */
public class LocalDatabaseManager {

    private static final String LOG_TAG = "Local database";

    private static final String TABLE = "messages";

    private Context context;

    private DatabaseHelper helper;
    private SQLiteDatabase database;

    public LocalDatabaseManager(Context context) {
        this.context = context;
        helper = new DatabaseHelper(context);
        database = helper.getWritableDatabase();
    }

    public ArrayList<Message> getAllMessages() {
        ArrayList<Message> messages = new ArrayList<>();
        Cursor cursor = database.query(TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst() && cursor != null) {
            int textIndex = cursor.getColumnIndex("text");
            int phoneIndex = cursor.getColumnIndex("phone");
            int statusIndex = cursor.getColumnIndex("status");
            do {
                String text = cursor.getString(textIndex);
                String phone = cursor.getString(phoneIndex);
                String status = cursor.getString(statusIndex);
                Message message = new Message(text, phone, status);
                messages.add(message);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return messages;
    }

    public void createMessage(String text, String phone, int id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("text", text);
        contentValues.put("phone", phone);
        contentValues.put("status", "stored");
        contentValues.put("remote_id", id);
        database.insert(TABLE, null, contentValues);
    }

    public void deleteMessage(int id) {
        database.delete(TABLE, "'remote_id'=" + id, null);
    }

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, Config.DATABASE_NAME, null, Config.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "onCreate database");

            db.execSQL("create table " + TABLE
                    + " ("
                    + "id integer primary key autoincrement,"
                    + "messae text,"
                    + "phone text"
                    + "status text"
                    + "remote_id integer"
                    + ")");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
