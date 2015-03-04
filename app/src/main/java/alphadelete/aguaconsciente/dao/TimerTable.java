package alphadelete.aguaconsciente.dao;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TimerTable {
    public static final String TABLE_TIMERS = "timers";
    public static final String TIMER_ID = "id";
    public static final String TIMER_TYPE_ID = "id_type";
    public static final String TIMER_MILLIS = "millis";
    public static final String TIMER_LITERS = "liters";
    public static final String TIMER_DATE = "date";

    // Database creation sql statement
    private static final String TABLE_CREATE =
        "create table " + TABLE_TIMERS + "("
            + TIMER_ID + " integer primary key autoincrement, "
            + TIMER_TYPE_ID + " integer not null, "
            + TIMER_MILLIS + " float not null, "
            + TIMER_LITERS + " float not null, "
            + TIMER_DATE + " long not null);";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(TABLE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(TimerTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMERS);
        onCreate(database);
    }
}
