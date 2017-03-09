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
        return dateToString(longToDate(now));
    }

    public static String dateToString(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        DateFormat parser = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String result = "";
        result = parser.format(calendar.getTime());
        return result;
    }

    public static Date longToDate(long time) {
        return new Date(time);
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
