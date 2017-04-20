package com.tied.android.tiedapp.customs.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tied.android.tiedapp.customs.db.table.ScheduleTable;

/**
 * Created by Emmanuel on 7/1/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    public static final String TAG = DatabaseHelper.class
            .getSimpleName();

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ti";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(ScheduleTable.CREATE_TABLE_SCHEDULES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + ScheduleTable.CREATE_TABLE_SCHEDULES);
        // create new tables
        onCreate(db);
    }
}
