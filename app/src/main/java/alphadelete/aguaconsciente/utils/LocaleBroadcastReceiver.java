package alphadelete.aguaconsciente.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import alphadelete.aguaconsciente.dao.TypeDS;

public class LocaleBroadcastReceiver extends BroadcastReceiver {
    private TypeDS typeDatasource;

    @ Override
    public void onReceive (Context context, Intent intent) {

        if (intent.getAction().compareTo(Intent.ACTION_LOCALE_CHANGED) == 0)
        {
            // Open connection to timer database
            typeDatasource = new TypeDS(context);
            typeDatasource.open();

            // Update Types for current Locale
            typeDatasource.updateLocaleTypes();

            // Close connection to timer database
            typeDatasource.close();
        }
    }
}
