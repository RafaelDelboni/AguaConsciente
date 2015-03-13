package alphadelete.aguaconsciente.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "alphadelete.aguaconsciente.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        TypeTable.onCreate(db, context);
        TimerTable.onCreate(db);
        ConfigTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        TimerTable.onUpgrade(db, oldVersion, newVersion);
        TypeTable.onUpgrade(db, oldVersion, newVersion, context);
        ConfigTable.onUpgrade(db, oldVersion, newVersion);
    }
}
