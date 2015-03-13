package alphadelete.aguaconsciente.dao;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ConfigTable {
    public static final String TABLE_CONFIG = "config";
    public static final String CONFIG_ID    = "id";
    public static final String CONFIG_VALUE = "desc";

    // Database creation sql statement
    private static final String TABLE_CREATE =
        "create table " + TABLE_CONFIG + "("
            + CONFIG_ID + " text primary key, "
            + CONFIG_VALUE + " text not null);";

    public static void onCreate(SQLiteDatabase database) {
        // Create table
        database.execSQL(TABLE_CREATE);

        // Database initial data
        String TABLE_INSERT_1 =
                "insert into " + TABLE_CONFIG +
                        " (" + CONFIG_ID + "," + CONFIG_VALUE + ") " +
                        "values ('KEEP_SCREEN', 'Y');";

        // Insert initial data
        String[] statements = new String[]{
                TABLE_INSERT_1
        };
        for(String sql : statements){
            database.execSQL(sql);
        }
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(TimerTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFIG);
        onCreate(database);
    }
}
