package com.javathlon;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognito.internal.util.StringUtils;
import com.javathlon.apiclient.ApiClient;
import com.javathlon.apiclient.api.AccountresourceApi;
import com.javathlon.apiclient.model.UsernamePassword;
import com.javathlon.db.DBAccessor;
import com.javathlon.upload.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import retrofit2.Response;

public class BaseActivity extends AppCompatActivity {
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


    public int uploadNotePos;
    protected boolean exited = false;
    Context con;
    protected volatile boolean musicThreadFinished = false;
    protected ArrayList<Runnable> playrCouter = new ArrayList<Runnable>();
    AlertDialog.Builder builder = null;
    AlertDialog alert = null;
    ContextThemeWrapper themeWrapper;


    protected boolean login() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userName = preferences.getString("username", "");
        String password = preferences.getString("password", "");

        return login(userName, password);
    }

    protected boolean login(final String email, final String password) {


        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        //showProgress(true);
        final AccountresourceApi api = ApiClient.getApiClient(getApplicationContext()).createService(AccountresourceApi.class);

        UsernamePassword usernamePassword = new UsernamePassword();
        usernamePassword.setUsername(email);
        usernamePassword.setPassword(password);
        usernamePassword.setRememberMe(true);

        boolean isSuccess = false;
        try {
            isSuccess = new AsyncTask<String, Void, Boolean>() {
                protected Boolean doInBackground(String... param) {

                    Response<Void> response = null;
                    boolean result = false;
                    try {
                        response = api.authenticate(email, password).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (response.isSuccessful()) {

                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(BaseActivity.this).edit();
                        editor.putString("username", email);
                        editor.putString("password", password);
                        editor.apply();
                        result = true;

                    }
                    // Toast.makeText(getApplicationContext(), "Username or password is wrong"
                    //       , Toast.LENGTH_SHORT).show();


                    return result;


                }

            }.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return isSuccess;
    }

    protected boolean anyFailedRequest(Response response) {

        if (response.code() == 500) {
            Toast.makeText(getApplicationContext(), "We have some server issues, sorry for this", Toast.LENGTH_SHORT).show();
            return false;
        }


        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString("jsessionid", null);
        editor.apply();

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        if (StringUtils.isEmpty(prefs.getString("username", "")) || StringUtils.isEmpty(prefs.getString("password", ""))) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            return false;
        }
        return login(prefs.getString("username", ""), prefs.getString("password", ""));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        con = this;

    }

    public void goToLoginActivity() {
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
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

            case R.id.loginMenuItem:
                Intent intnt = new Intent(BaseActivity.this, LoginPref.class);
                startActivity(intnt);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void showMyDialog(String msg) {
        if (builder == null)
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

    public void hideDialog() {
        if (alert != null)
            alert.hide();
    }

    public void showConfirmDialog(String message, String positiveText, String negativeText, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {

        // if(themeWrapper == null)
        //   themeWrapper = new ContextThemeWrapper(this, R.style.THE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton(positiveText, positiveListener);
        builder.setNegativeButton(negativeText, negativeListener);
        AlertDialog alert = builder.create();
        alert.show();

    }


    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
