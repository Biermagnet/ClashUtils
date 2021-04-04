package de.michi.clashutils.utils;

import de.michi.clashutils.clashofclans.WarPlayer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String getCurrentFormattedDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String date = formatter.format(new Date());
        return date;
    }
}
