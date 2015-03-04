package alphadelete.aguaconsciente.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.SQLException;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import alphadelete.aguaconsciente.models.TimerItem;

public class TimerDS {
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
        TimerTable.TIMER_ID,
        TimerTable.TIMER_TYPE_ID,
        TimerTable.TIMER_MILLIS,
        TimerTable.TIMER_LITERS,
        TimerTable.TIMER_DATE
    };

    public TimerDS(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public int getCount(long itemTypeId) {
        String[] projection = { TimerTable.TIMER_ID };
        Cursor cursor = database.query(
                TimerTable.TABLE_TIMERS,
                projection,
                TimerTable.TIMER_TYPE_ID + " = " + itemTypeId,
                null, null, null,
                TimerTable.TIMER_DATE + " DESC"
        );
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public TimerItem getItemAt(long itemTypeId, int position) {

        Cursor cursor = database.query(
                TimerTable.TABLE_TIMERS,
                allColumns,
                TimerTable.TIMER_TYPE_ID + " = " + itemTypeId,
                null, null, null,
                TimerTable.TIMER_DATE + " DESC");

        if (cursor.moveToPosition(position)) {
            TimerItem timer = cursorToTimer(cursor);
            cursor.close();
            return timer;
        }
        return null;
    }

    public TimerItem createTimer(long itemTypeId, float itemLiter, float itemMillis, long itemDate) {
        ContentValues values = new ContentValues();
        values.put(TimerTable.TIMER_TYPE_ID, itemTypeId);
        values.put(TimerTable.TIMER_MILLIS, itemMillis);
        values.put(TimerTable.TIMER_LITERS, itemLiter);
        values.put(TimerTable.TIMER_DATE, itemDate);

        long insertId = database.insert(TimerTable.TABLE_TIMERS, null,
                values);
        Cursor cursor = database.query(TimerTable.TABLE_TIMERS,
                allColumns, TimerTable.TIMER_ID + " = " + insertId,
                null, null, null, null);
        cursor.moveToFirst();
        TimerItem newTimer = cursorToTimer(cursor);
        cursor.close();
        return newTimer;
    }

    public void deleteTimer(TimerItem timer) {
        long id = timer.getId();
        System.out.println("Timer deleted with id: " + id);
        database.delete(TimerTable.TABLE_TIMERS, TimerTable.TIMER_ID
                + " = " + id, null);
    }

    public List<TimerItem> getAllTimerItems(long itemTypeId) {
        List<TimerItem> timers = new ArrayList<TimerItem>();

        Cursor cursor = database.query(TimerTable.TABLE_TIMERS,
                allColumns, TimerTable.TIMER_TYPE_ID + " = " + itemTypeId
                , null, null, null, TimerTable.TIMER_DATE + " DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TimerItem timer = cursorToTimer(cursor);
            timers.add(timer);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return timers;
    }

    private TimerItem cursorToTimer(Cursor cursor) {
        //long itemId, int itemTypeId, float itemMillis, float itemLiter, Date itemDate
        return new TimerItem(
            cursor.getLong(0),
            cursor.getInt(1),
            cursor.getFloat(2),
            cursor.getFloat(3),
            new Date(cursor.getLong(4))
        );
    }


}
