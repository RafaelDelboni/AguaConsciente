package alphadelete.aguaconsciente.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import alphadelete.aguaconsciente.R;
import alphadelete.aguaconsciente.models.TypeItem;

public class TypeDS {
    // Database fields
    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
        TypeTable.TYPE_ID,
        TypeTable.TYPE_DESC,
        TypeTable.TYPE_LITERS,
        TypeTable.TYPE_CUSTOM
    };

    public TypeDS(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public TypeItem createType(String itemDesc, float itemLiter, char itemCustom) {
        ContentValues values = new ContentValues();
        values.put(TypeTable.TYPE_DESC, itemDesc);
        values.put(TypeTable.TYPE_LITERS, itemLiter);
        values.put(TypeTable.TYPE_CUSTOM, String.valueOf(itemCustom));

        long insertId = database.insert(TypeTable.TABLE_TYPES, null,
                values);
        Cursor cursor = database.query(TypeTable.TABLE_TYPES,
                allColumns, TypeTable.TYPE_ID + " = " + insertId,
                null, null, null, null);
        cursor.moveToFirst();
        TypeItem newType = cursorToType(cursor);
        cursor.close();
        return newType;
    }

    public void deleteType(TypeItem timer) {
        long id = timer.getId();

        System.out.println("Type deleted with id: " + id);
        database.delete(TypeTable.TABLE_TYPES, TypeTable.TYPE_ID
                + " = " + id, null);
    }

    public void updateType(TypeItem timer) {
        long id = timer.getId();
        ContentValues values = new ContentValues();
        values.put(TypeTable.TYPE_DESC, timer.getDesc());
        values.put(TypeTable.TYPE_LITERS, timer.getLiter());

        System.out.println("Type update with id: " + id);
        database.update(TypeTable.TABLE_TYPES, values, TypeTable.TYPE_ID
                + " = " + id, null);
    }

    public void updateLocaleTypes() {

        String type_1 = context.getString(R.string.type_1);
        String type_2 = context.getString(R.string.type_2);
        String type_3 = context.getString(R.string.type_3);
        String type_4 = context.getString(R.string.type_4);

        ContentValues values = new ContentValues();
        values.put(TypeTable.TYPE_DESC, type_1);
        database.update(TypeTable.TABLE_TYPES, values, TypeTable.TYPE_ID
                + " = " + 1, null);

        values = new ContentValues();
        values.put(TypeTable.TYPE_DESC, type_2);
        database.update(TypeTable.TABLE_TYPES, values, TypeTable.TYPE_ID
                + " = " + 2, null);

        values = new ContentValues();
        values.put(TypeTable.TYPE_DESC, type_3);
        database.update(TypeTable.TABLE_TYPES, values, TypeTable.TYPE_ID
                + " = " + 3, null);

        values = new ContentValues();
        values.put(TypeTable.TYPE_DESC, type_4);
        database.update(TypeTable.TABLE_TYPES, values, TypeTable.TYPE_ID
                + " = " + 4, null);

        System.out.println("Update locales types");
    }

    public List<TypeItem> getType() {
        List<TypeItem> types = new ArrayList<TypeItem>();

        Cursor cursor = database.query(TypeTable.TABLE_TYPES,
                allColumns, null, null, null, null, TypeTable.TYPE_ID + " ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TypeItem type = cursorToType(cursor);
            types.add(type);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return types;
    }

    public float sumType(long typeId){
        float total;

        Cursor cursor = database.rawQuery(
            "SELECT SUM((" + TimerTable.TIMER_LITERS + "/ 60" + ")*(" + TimerTable.TIMER_MILLIS + "/ 1000" + ")) " +
            "FROM " + TimerTable.TABLE_TIMERS + " WHERE " + TimerTable.TIMER_TYPE_ID + " = " + typeId + ";",
            null
        );
        if(cursor.moveToFirst())
            total = cursor.getFloat (0);
        else
            total = 0;
        // make sure to close the cursor
        cursor.close();

        return total;
    }

    public TypeItem getType(long id) {
        Cursor cursor = database.query(TypeTable.TABLE_TYPES,
                allColumns, TypeTable.TYPE_ID + " = " + id,
                null, null, null, null);

        cursor.moveToFirst();
        TypeItem type = cursorToType(cursor);

        // make sure to close the cursor
        cursor.close();
        return type;
    }

    private TypeItem cursorToType(Cursor cursor) {
        //long itemId, String itemDesc, long itemLiter, char itemCustom
        return new TypeItem(
            cursor.getLong(0),
            cursor.getString(1),
            cursor.getFloat(2),
            cursor.getString(3).charAt(0)
        );
    }


}
