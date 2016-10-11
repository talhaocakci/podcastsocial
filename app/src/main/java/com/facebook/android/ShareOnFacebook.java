package com.facebook.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.Facebook.DialogListener;
import com.javathlon.CommonStaticClass;
import com.javathlon.R;
import com.podmark.twitter.TweetOnTwitter;

public class ShareOnFacebook extends Activity {

    private static final String APP_ID = CommonStaticClass.APP_ID;
    private static final String[] PERMISSIONS = new String[]{"public_profile", "publish_actions"};

    private static final String TOKEN = "access_token";
    private static final String EXPIRES = "expires_in";
    private static final String KEY = "facebook-credentials";

    private Facebook facebook;
    private String messageToPost, originalMessage;
    private Context con;
    private boolean tweetToo;
    private boolean loginOnly = true;
    private TextView dialogInfo;
    private boolean only;

    public boolean saveCredentials(Facebook facebook) {
        Editor editor = getApplicationContext().getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        editor.putString(TOKEN, facebook.getAccessToken());
        editor.putLong(EXPIRES, facebook.getAccessExpires());
        return editor.commit();
    }

    public boolean restoreCredentials(Facebook facebook) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(KEY, Context.MODE_PRIVATE);
        facebook.setAccessToken(sharedPreferences.getString(TOKEN, null));
        facebook.setAccessExpires(sharedPreferences.getLong(EXPIRES, 0));
        return facebook.isSessionValid();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        con = this;
        facebook = new Facebook(APP_ID);
        restoreCredentials(facebook);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sharefb);


        String facebookMessage = getIntent().getStringExtra("url");
        String pid = getIntent().getStringExtra("pid");
        only = getIntent().getBooleanExtra("only", true);
        if (only) {
            tweetToo = false;
        } else {
            tweetToo = true;
        }
        if (facebookMessage == null) {
            facebookMessage = "Test wall post";
        }
        originalMessage = facebookMessage;
        messageToPost = facebookMessage;
    }

    public void doNotShare(View button) {
        finish();
    }

    public void share(View button) {
        if (!facebook.isSessionValid()) {
            loginAndPostToWall();
        } else {
            postToWall(messageToPost);
        }
    }

    public void loginAndPostToWall() {
        facebook.authorize(this, PERMISSIONS, new LoginDialogListener());
    }

    public void showMyDialog(String msg) {

    }

    public void postToWall(String message) {
        Bundle parameters = new Bundle();
        parameters.putString("name", "Podmark Share");
        parameters.putString("caption", "Discuss audiobook and podcast snippets");
        parameters.putString("message", "I have cut something for you: " + message);
        //   parameters.putString("link",message);
        parameters.putString("description", "Podmark is the tool for cutting and sharing your podcasts and audiobooks. Give it a try...");
        parameters.putString("picture", "http://s3.amazonaws.com/paperify/podmark.jpg");
        try {
            facebook.request("me");
            String response = facebook.request("me/feed", parameters, "POST");
            Log.d("Tests", "got response: " + response);
            if (response == null || response.equals("") ||
                    response.equals("false")) {
                showToast("Failed. Possibly we did something wrong. This problem will be solved as soon as possible.");
            } else {
//                SessionEvents.onLogoutBegin();
//                AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(facebook);
//                asyncRunner.logout(getBaseContext(), new LogoutRequestListener());
                if (tweetToo) {
                    Intent postOnFacebookWallIntent = new Intent(con, TweetOnTwitter.class);
                    postOnFacebookWallIntent.putExtra("message", originalMessage);
                    postOnFacebookWallIntent.putExtra("only", true);
                    con.startActivity(postOnFacebookWallIntent);
                }
                showToast("Sharing is successful. We wish your share attracts your friends.");
                finish();
            }
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setMessage("Tweet this too")
//			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog,
//						int whichButton) {
//					Intent iSubmitQustion = new Intent(con,
//							TwitterPin.class);
//					
//					iSubmitQustion.putExtra("status",messageToPost);
//
//					con.startActivity(iSubmitQustion);
//					finish();
//				}
//			});
//			builder.setNegativeButton("Cancel",
//					new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog,
//								int whichButton) {
//							finish();
//						}
//					});
//			AlertDialog alert = builder.create();
//			alert.show();

//			if(tweetToo){
//
//			}else{
//				finish();
//			}
        } catch (Exception e) {
            showToast("Failed to post to wall!");
            e.printStackTrace();
            finish();
        }
    }

    class LoginDialogListener implements DialogListener {
        public void onComplete(Bundle values) {
            SessionEvents.onLoginSuccess();
            saveCredentials(facebook);
            if (messageToPost != null) {
                postToWall(messageToPost);
            }
        }

        public void onFacebookError(FacebookError error) {
            SessionEvents.onLoginError(error.getMessage());
            showToast("Authentication with Facebook failed!");
            finish();
        }

        public void onError(DialogError error) {
            SessionEvents.onLoginError(error.getMessage());
            showToast("Authentication with Facebook failed!");
            finish();
        }

        public void onCancel() {
            SessionEvents.onLoginError("Action Canceled");
            showToast("Authentication with Facebook cancelled!");
            finish();
        }
    }

    class LogoutRequestListener extends BaseRequestListener {
        public void onComplete(String response, final Object state) {
            // callback should be run in the original thread, 
            // not the background thread
//            mHandler.post(new Runnable() {
//                public void run() {
            SessionEvents.onLogoutFinish();
//                }
//            });
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}