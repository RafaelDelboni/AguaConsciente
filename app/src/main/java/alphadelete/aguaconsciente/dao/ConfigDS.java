package alphadelete.aguaconsciente.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ConfigDS {
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
        ConfigTable.CONFIG_ID,
        ConfigTable.CONFIG_VALUE
    };

    public ConfigDS(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void setConfigValue(String configId, String configValue) {

        ContentValues values = new ContentValues();
        values.put(ConfigTable.CONFIG_VALUE, configValue);

        System.out.println("Config update with id: " + configId);
        database.update(ConfigTable.TABLE_CONFIG, values, ConfigTable.CONFIG_ID
                + " = '" + configId + "'", null);
    }

    public String getConfigValue(String configId) {
        String value = null;

        Cursor cursor = database.query(
                ConfigTable.TABLE_CONFIG,
                allColumns,
                ConfigTable.CONFIG_ID + " = '" + configId + "'",
                null, null, null,null);

        if(cursor.moveToFirst())
            value = cursor.getString(1);

        // make sure to close the cursor
        cursor.close();

        return value;
    }

}
