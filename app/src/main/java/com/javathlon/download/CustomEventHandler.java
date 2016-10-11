package com.javathlon.download;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.javathlon.CheckInternet;

/**
 * Created by talha on 20.07.2015.
 */
public class CustomEventHandler extends Handler {
    public static final int UPLOADFILE = 90, UPLOADSEQ = 99, UPLOAD_COMPLETED = 199, CROPCOMPLETED = 299 ;

    Context context;

    public CustomEventHandler(Context context) {
        this.context = context;
    }

    @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPLOADFILE:

                    if (CheckInternet.checkAnyConnectionExists(context)) {
                        Bundle bundle = msg.getData();
                        String filePath = bundle.getString("filepath");
                        int sec = bundle.getInt("beginsec");
                        long noteId = bundle.getLong("noteid");
                        long podcastId = bundle.getLong("podcastid");


                        //  new Cropm
                        // handleUpload(uploadNotePos);
                    } else {
                        String dialogmsg = "Internet connection is not available";
                        //    BaseActivity.showMyDialog(dialogmsg);
                    }
                    break;

                case CROPCOMPLETED:

                case UPLOAD_COMPLETED:
                    Bundle bundle = msg.getData();
                    final String url = bundle.getString("url");
                    final String devicePath = bundle.getString("devicepath");
                   /* if (context instanceof Activity) {
                        Activity a = (Activity) context;
                        AlertDialog.Builder builder = new AlertDialog.Builder(a);
                        builder.setTitle(context.getResources().getString(R.string.shareNoteConfirmTitle));
                        builder.setMessage(context.getResources().getString(R.string.shareNoteOnSocialMedia));
                        builder.setPositiveButton(context.getResources().getString(R.string.share), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(context, SocialLoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("filepath", url);
                                context.getApplicationContext().startActivity(intent);
                            }
                        });


                        builder.setNegativeButton(context.getResources().getString(R.string.negative), null);
                        builder.show();

                    }*/

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("plain/text");
                    i.putExtra(Intent.EXTRA_TEXT, url);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.getApplicationContext().startActivity(i);



            }
        }



}
