package alphadelete.aguaconsciente.utils;

import java.text.DateFormat;
import java.util.Date;

public class StringFormat {

    public static String millisToString(long millisTimer){
        // Calc the Hours/Minutes/Seconds of the millisecond
        int real_seconds = (int) (millisTimer / 1000);
        int real_minutes = real_seconds / 60;
        int hours = real_minutes / 60;
        int seconds = real_seconds % 60;
        int minutes = real_minutes % 60;
        // Define the use of hours in the return format
        String format;
        if (hours > 0) {
            format = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            format = String.format("%02d:%02d", minutes, seconds);
        }
        // Return formated String
        return format;
    }

    public static String dateToString(Date dateTimer){
        // Get default date format
        DateFormat dtFormat = DateFormat.getDateTimeInstance();
        // Return formated String
        return dtFormat.format(dateTimer);
    }

    public static String literToString (float itemLiter, long itemMillis){
        // Calc the liter vs timer
        float liter = (itemLiter / 60) * ((float) itemMillis / 1000);
        // Return formated String
        return String.format("%.2f", liter);
    }
}
