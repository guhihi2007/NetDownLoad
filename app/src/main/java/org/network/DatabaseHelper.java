package org.network;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/5/24.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String name = "NetWork";
    public static final String table = "Record";

    public DatabaseHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE  IF NOT EXISTS " + table + " (_ID INTEGER PRIMARY KEY ,entry_id TEXT NOT NULL, entry_url TEXT NOT NULL,entry_status TEXT NOT NULL,entry_length TEXT NOT NULL,entry_current TEXT NOT NULL)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
