package com.javathlon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by ocakcit on 27/10/2016.
 */

public class JavathlonUtil {

    public static void alert(Context context, String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(context);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        bld.create().show();
    }

    public void showYesNoDialog(Activity activity, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(msg)
                .setCancelable(false)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
