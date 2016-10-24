package com.javathlon;

import android.content.Context;
import android.database.Cursor;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.javathlon.db.DBAccessor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ocakcit on 22/10/2016.
 */

public class NoteUtil {

    protected static DBAccessor dbHelper = null;

    public static void saveNote(Context context, String filePath, long podcastId, int beginpos, int endpos, String noteText) {

        if (dbHelper == null) {
            dbHelper = new DBAccessor(context);
            dbHelper.open();
        }

            long podnumber = podcastId;

            if(podcastId <=0)
                podnumber = getPodcastNumber(filePath);
            long id = dbHelper.createNote(
                    podnumber,
                    0,
                    beginpos,
                    endpos,
                    filePath, noteText,
                    "me", getCurrentDate(), new Date().getTime(), "");

            Toast.makeText(context,
                    "Your bookmark has been saved",
                    Toast.LENGTH_SHORT).show();

        }

    private static long getPodcastNumber(String path) {
        String sql = "";

        if(path.startsWith("http"))
            sql= "Select _id from podcast where download_link = '" + path+"'";
        else
            sql = "Select _id from podcast where full_device_path = '" + path+"'";

        Cursor mCursor = null;
        long num = -1l;
        try {
            mCursor = dbHelper.executeQuery(sql);
            if (mCursor.moveToFirst()) {
                do {
                    num = mCursor.getLong(mCursor.getColumnIndex("_id"));
                    return num;
                } while (mCursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("hata", e.getLocalizedMessage());
            // TODO: handle exception
        } finally {
            if (null != mCursor)
                mCursor.close();
        }

        return num;
    }



    public static String getCurrentDate() {
        DateFormat parser = new SimpleDateFormat("dd MM yyyy hh:mm:ss");
        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        Date d1 = null;
        try {
            d1 = parser.parse(parser.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d1.toString();
    }
}
