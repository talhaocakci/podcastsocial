package com.javathlon.memsoft;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by talha on 01.03.2015.
 */
public class MemsoftUtil {

    public static String getTimeAsString() {
        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        Date dd = null;
        DateFormat parser = new SimpleDateFormat(
                "dd/MM/yyyy hh:mm:ss");
        String result = "";

        result = parser.format(calendar.getTime());

        return result;
    }

    public static Date getTimeFromString(String s) {


        DateFormat parser = new SimpleDateFormat(
                "dd/MM/yyyy hh:mm:ss");
        Date result = null;

        try {
            result = parser.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }
}
