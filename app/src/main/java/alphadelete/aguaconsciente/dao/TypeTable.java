package alphadelete.aguaconsciente.dao;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TypeTable {
    public static final String TABLE_TYPES = "types";
    public static final String TYPE_ID = "id_type";
    public static final String TYPE_DESC = "desc";
    public static final String TYPE_LITERS = "liters";
    public static final String TYPE_CUSTOM = "custom";

    // Database creation sql statement
    private static final String TABLE_CREATE =
        "create table " + TABLE_TYPES + "("
            + TYPE_ID + " integer primary key autoincrement, "
            + TYPE_DESC + " text not null, "
            + TYPE_LITERS + " float not null, "
            + TYPE_CUSTOM + " char not null);";

    // Fonte: http://site.sabesp.com.br/site/interna/Default.aspx?secaoId=140
    /*
    - Escovar os dentes
    Se uma pessoa escova os dentes em 5 minutos com a torneira não muito aberta, gasta 12 litros de água.
    No entanto, se molhar a escova e fechar a torneira enquanto escova os dentes e, ainda,
    enxaguar a boca com um copo de água, consegue economizar mais de 11,5 litros de água.
    (2,4 litros por minuto no pior caso).
    - Lavando louça
    Numa casa, lavando louça com a torneira meio aberta em 15 minutos, são utilizados 117 litros de água.
    Com economia o consumo pode chegar a 20 litros.
    (7,8 litros por minuto no pior caso).
    - Lavando roupa no tanque
    No tanque, com a torneira aberta por 15 minutos, o gasto de água pode chegar a 279 litros.
    O melhor é deixar acumular roupa, colocar a água no tanque para ensaboar e manter a torneira fechada.
    (18 litros por minuto no pior caso).
    - Hora do banho
    Banho de ducha por 15 minutos, com o registro meio aberto, consome 135 litros de água.
    Se fechamos o registro, ao se ensaboar, e reduzimos o tempo para 5 minutos, o consumo cai para 45 litros.
    (9 litros por minuto no pior caso).
     */

    // Database initial data
    private static final String TABLE_INSERT_1 =
        "insert into " + TABLE_TYPES +
            " (" + TYPE_ID + "," + TYPE_DESC + "," + TYPE_LITERS + "," + TYPE_CUSTOM + ") " +
                "values (1, 'Escovar dentes', 2.4, 'N');";

    private static final String TABLE_INSERT_2 =
        "insert into " + TABLE_TYPES +
            " (" + TYPE_ID + "," + TYPE_DESC + "," + TYPE_LITERS + "," + TYPE_CUSTOM + ") " +
                "values (2, 'Lavar louça', 7.8, 'N');";

    private static final String TABLE_INSERT_3 =
        "insert into " + TABLE_TYPES +
            " (" + TYPE_ID + "," + TYPE_DESC + "," + TYPE_LITERS + "," + TYPE_CUSTOM + ") " +
                "values (3, 'Lavar roupa', 18, 'N');";

    private static final String TABLE_INSERT_4 =
        "insert into " + TABLE_TYPES +
            " (" + TYPE_ID + "," + TYPE_DESC + "," + TYPE_LITERS + "," + TYPE_CUSTOM + ") " +
                "values (4, 'Tomar banho', 9, 'N');";


    public static void onCreate(SQLiteDatabase database) {
        // Create table
        database.execSQL(TABLE_CREATE);

        // Insert initial data
        String[] statements = new String[]{
                TABLE_INSERT_1,
                TABLE_INSERT_2,
                TABLE_INSERT_3,
                TABLE_INSERT_4
        };
        for(String sql : statements){
            database.execSQL(sql);
        }
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(TimerTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPES);
        onCreate(database);
    }
}
