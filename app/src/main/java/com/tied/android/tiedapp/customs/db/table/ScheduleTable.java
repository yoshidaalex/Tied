package com.tied.android.tiedapp.customs.db.table;

/**
 * Created by Emmanuel on 7/1/2016.
 */
public class ScheduleTable {

    // Table Names
    public static final String TABLE_SCHEDULE = "schedule";

    // Common column names
    public static final String KEY_ID = "id";
    public static final String KEY_CREATED_AT = "created_at";

    // Schedule Table - column names
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_CLIENT_ID = "client_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_REMINDER = "reminder";
    public static final String KEY_VISITED = "visited";
    public static final String KEY_DATE = "date";
    public static final String KEY_START_TIME = "start_time";
    public static final String KEY_END_TIME = "end_time";
    public static final String KEY_LAT = "lat";
    public static final String KEY_LON = "lon";

    // Database creation sql statement
    public static final String CREATE_TABLE_SCHEDULES = "CREATE TABLE " + TABLE_SCHEDULE
            + "(" + KEY_ID + " TEXT PRIMARY KEY,"
            + KEY_USER_ID + " TEXT,"
            + KEY_CLIENT_ID + " TEXT,"
            + KEY_TITLE + " TEXT,"
            + KEY_REMINDER + " INTEGER,"
            + KEY_VISITED + " INTEGER,"
            + KEY_DATE + " TEXT,"
            + KEY_START_TIME + " TEXT,"
            + KEY_END_TIME + " TEXT,"
            + KEY_LAT + " TEXT,"
            + KEY_LON + " TEXT,"
            + KEY_CREATED_AT + " TEXT"
            + ")";

}
