package com.javathlon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.javathlon.R;
import com.javathlon.components.LastPlayClass;
import com.javathlon.db.DBAccessor;
import com.javathlon.upload.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseActivity extends Activity {
    public static final int UPLOAD_ID = Menu.FIRST + 1;
    public static final int ONLINE_ID = Menu.FIRST + 2;
    public static final int EXIT_ID = Menu.FIRST + 3;
    public static final int CONTEXTMENU_UPLOAD = 11, CONTEXTMENU_EDIT = 12,
            CONTEXTMENU_LIKE = 13, CONTEXTMENU_DELETE = 14;
    protected DBAccessor dbHelper = null;
    protected String path;
    public static ArrayList<Integer> uploadQueue = new ArrayList<Integer>();
    public static HashMap<Integer, UploadTask> uploadmap = new HashMap<Integer, UploadTask>();
    public HashMap<String, String> noteSelect;
    int notifyUserForUpload = 0;
    LastPlayClass lastPlayObj = null;

    public int uploadNotePos;
    protected boolean exited = false;
    Context con;
    protected volatile boolean musicThreadFinished = false;
    protected ArrayList<Runnable> playrCouter = new ArrayList<Runnable>();
    AlertDialog.Builder builder = null;
    AlertDialog alert = null;
    ContextThemeWrapper themeWrapper;

    protected void fillLastPlayedList() {
        lastPlayObj = new LastPlayClass();

        Cursor mCursor = null;
        try {
            mCursor = dbHelper.getLastPlayedNotesListResult();
            if (mCursor.moveToFirst()) {
                do {

                    String name = mCursor.getString(mCursor
                            .getColumnIndex("name"));

                    String podcast_id = mCursor.getString(mCursor
                            .getColumnIndex("podcast_id"));
                    String song_sp_id = mCursor.getString(mCursor
                            .getColumnIndex("song_sp_id"));
                    String begin_sec = mCursor.getString(mCursor
                            .getColumnIndex("begin_sec"));
                    String end_sec = mCursor.getString(mCursor
                            .getColumnIndex("end_sec"));
                    String beginend = mCursor.getString(mCursor
                            .getColumnIndex("beginend"));
                    String songpath = mCursor.getString(mCursor
                            .getColumnIndex("songpath"));
                    String note_text = mCursor.getString(mCursor
                            .getColumnIndex("note_text"));
                    String author = mCursor.getString(mCursor
                            .getColumnIndex("author"));
                    String create_date = mCursor.getString(mCursor
                            .getColumnIndex("create_date"));
                    String last_listen_date_mil = mCursor.getString(mCursor
                            .getColumnIndex("last_listen_date_mil"));
                    String last_listen_date = mCursor.getString(mCursor
                            .getColumnIndex("last_listen_date"));
                    lastPlayObj.setPodcast_id(podcast_id);
                    lastPlayObj.setSong_sp_id(song_sp_id);
                    lastPlayObj.setBegin_sec(begin_sec);
                    lastPlayObj.setEnd_sec(end_sec);
                    lastPlayObj.setBeginend(beginend);
                    lastPlayObj.setSongpath(songpath);
                    lastPlayObj.setNote_text(note_text);
                    lastPlayObj.setAuthor(author);
                    lastPlayObj.setCreate_date(create_date);
                    lastPlayObj.setLast_listen_date_mil(last_listen_date_mil);
                    lastPlayObj.setLast_listen_date(last_listen_date);

                } while (mCursor.moveToNext());
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (mCursor != null && !mCursor.isClosed()) {
                mCursor.close();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        con = this;

    }

    /**
     * ******************************************************************************************
     * ** MENUS ***
     * *******************************************************************************************
     */

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ExitMenuItem:
                exited = true;
                musicThreadFinished = true;
                stopService(new Intent("com.paperify.podmark.ACTION_PLAY_MUSIC"));
                onDestroy();
                System.exit(0);
                return true;
        /*case R.id.UploadMenuItem:
			Intent i = new Intent(BaseActivity.this, NoteListScreen.class);
			startActivity(i);
			return true;*/
            case R.id.loginMenuItem:
                Intent intnt = new Intent(BaseActivity.this, LoginPref.class);
                startActivity(intnt);
                return true;
		/*case R.id.OnlineMenuItem:
			String onlineUrl = "http://www.paperify.net/";
			Intent onlineIntent = new Intent(Intent.ACTION_VIEW);
			onlineIntent.setData(Uri.parse(onlineUrl));
			startActivity(onlineIntent);
			return true;  */
			/*
			 * case R.id.SettingsMenuItem: Intent i = new Intent(RadioApp.this,
			 * Preferences.class); startActivity(i); return true;
			 */
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public HashMap<String, String> audioFilePathFromNote(int pos) {
        Cursor audioCursor = null;
        int i = 0;
        HashMap<String, String> note = new HashMap<String, String>();
        try {
            String[] projection = {DBAccessor.KEY_BEGINSEC,
                    DBAccessor.KEY_ENDSEC, DBAccessor.KEY_NOTETEXT,
                    DBAccessor.KEY_SONGPATH};
            if (path == null) {
                Log.e("", "it is null");
                if (lastPlayObj == null) {
                    fillLastPlayedList();
                    audioCursor = dbHelper.fetchNote(pos, lastPlayObj
                            .getSongpath().get(pos));
                } else {
                    audioCursor = dbHelper.fetchNote(pos, lastPlayObj
                            .getSongpath().get(pos));
                }

            } else {
                Log.e("pos", "" + pos);
                audioCursor = dbHelper.fetchNote(pos, path);
            }

            if (audioCursor.moveToFirst()) {
                do {
                    int rowID = audioCursor.getInt(audioCursor
                            .getColumnIndex(DBAccessor.KEY_ID));
                    note.put("rowID", rowID + "");
                    String beginPos = audioCursor.getString(audioCursor
                            .getColumnIndex(DBAccessor.KEY_BEGINSEC));
                    note.put("beginPos", beginPos);
                    String endPos = audioCursor.getString(audioCursor
                            .getColumnIndex(DBAccessor.KEY_ENDSEC));
                    note.put("endPos", endPos);
                    String noteText = audioCursor.getString(audioCursor
                            .getColumnIndex(DBAccessor.KEY_NOTETEXT));
                    note.put("noteText", noteText);
                    String audioFilePath = audioCursor.getString(audioCursor
                            .getColumnIndex(DBAccessor.KEY_SONGPATH));
                    note.put("audioFilePath", audioFilePath);

                    return note;
                } while (audioCursor.moveToNext());
            }
        } finally {
            if (null != audioCursor) {
                audioCursor.close();
            }
        }

        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void showMyDialog(String msg) {
if(builder == null)
    builder = new AlertDialog.Builder(this);
        builder.setMessage(msg)
                .setCancelable(false)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alert = builder.create();
        alert.show();
    }

    public void hideDialog(){
        if(alert != null)
            alert.hide();
    }
    
    public void showConfirmDialog(String message, String positiveText, String negativeText, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener){

         // if(themeWrapper == null)
           //   themeWrapper = new ContextThemeWrapper(this, R.style.THE);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(message)
			.setPositiveButton(positiveText, positiveListener) ;
			builder.setNegativeButton(negativeText, negativeListener);
			AlertDialog alert = builder.create();
			alert.show();

    }


}
