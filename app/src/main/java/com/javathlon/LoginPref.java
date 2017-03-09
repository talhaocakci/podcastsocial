package com.javathlon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.Facebook;
import com.facebook.android.LoginButton;
import com.facebook.android.SessionEvents;
import com.facebook.android.SessionEvents.AuthListener;
import com.facebook.android.SessionEvents.LogoutListener;
import com.facebook.android.SessionStore;
import com.facebook.android.ShareOnFacebook;
import com.facebook.android.Util;
import com.podmark.twitter.OAuthAccessTokenActivity;
import com.podmark.twitter.store.SharedPreferencesCredentialStore;

import org.json.JSONObject;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;

public class LoginPref extends Activity {
    private SharedPreferences app_preferences;
    ImageButton fbShareToggle, twitterShareToggle;
    ToggleButton twitterLoginToggle;
    private LoginButton mLoginButton;
    LoginButton loginButton;
    String APP_ID = CommonStaticClass.APP_ID;
    private Facebook mFacebook;
    private AsyncFacebookRunner mAsyncRunner;
    ShareOnFacebook shareFb;
    private static final String TOKEN = "access_token";
    private static final String EXPIRES = "expires_in";
    private static final String KEY = "facebook-credentials";

    public boolean saveCredentials(Facebook facebook) {
        Editor editor = getApplicationContext().getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        editor.putString(TOKEN, facebook.getAccessToken());
        editor.putLong(EXPIRES, facebook.getAccessExpires());
        return editor.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginsharepref);
        if (APP_ID == null) {
            Util.showAlert(this, "Warning", "Facebook Applicaton ID must be " +
                    "specified before running this example: see Example.java");
        }
        app_preferences = PreferenceManager.getDefaultSharedPreferences(this);

        mLoginButton = (LoginButton) findViewById(R.id.btnloginFromFacebook);
        twitterLoginToggle = (ToggleButton) findViewById(R.id.twitterLoginToggle);

        mFacebook = new Facebook(APP_ID);
        mAsyncRunner = new AsyncFacebookRunner(mFacebook);
        shareFb = new ShareOnFacebook();
        SessionStore.restore(mFacebook, this);
        SessionEvents.addAuthListener(new SampleAuthListener());
        SessionEvents.addLogoutListener(new SampleLogoutListener());
        mLoginButton.init(this, mFacebook);

        fbShareToggle = (ImageButton) findViewById(R.id.fbShareToggle);
        fbShareToggle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform action on clicks
                SharedPreferences.Editor editor = app_preferences
                        .edit();
                if (CommonStaticClass.facebookshareon == false) {

                    loginButton = (LoginButton) findViewById(R.id.btnloginFromFacebook);
                    if (loginButton.isSessionActive) {
                        editor.putBoolean(CommonStaticClass.FBSHARE, true);
                        CommonStaticClass.facebookshareon = true;
                        CommonStaticClass.facebookLogin = true;
                        Toast.makeText(LoginPref.this, "Facebook sharing is activated. All notes you uploaded will be shared in your Facebook account. Whenever you want, you may disable sharing here.", Toast.LENGTH_LONG).show();
                        fbShareToggle.setImageResource(R.drawable.toggle_on);
                    } else {
                        Toast.makeText(LoginPref.this, "You need to login via Facebook first", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    editor.putBoolean(CommonStaticClass.FBSHARE, false);
                    CommonStaticClass.facebookshareon = false;
                    Toast.makeText(LoginPref.this, "Facebook sharing is deactivated.", Toast.LENGTH_SHORT);
                    fbShareToggle.setImageResource(R.drawable.toggle_off);
                }
                editor.commit();
            }
        });
        Boolean isFbShareEnable = app_preferences.getBoolean(CommonStaticClass.FBSHARE, false);


        if (isFbShareEnable) {
            CommonStaticClass.facebookshareon = true;
            fbShareToggle.setImageResource(R.drawable.toggle_on);
            CommonStaticClass.facebookLogin = true;

        } else {
            CommonStaticClass.facebookshareon = false;
            fbShareToggle.setImageResource(R.drawable.toggle_off);

        }


        twitterShareToggle = (ImageButton) findViewById(R.id.twitterShareToggle);
        twitterShareToggle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!twitterLoginToggle.isChecked()) {
                    Toast.makeText(LoginPref.this, "You need to login via Twitter first", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Perform action on clicks
                SharedPreferences.Editor editor = app_preferences
                        .edit();
                if (CommonStaticClass.twittershareon == false) {
                    editor.putBoolean(CommonStaticClass.TWITTERSHARE, true);
                    twitterShareToggle.setImageResource(R.drawable.toggle_on);
                    CommonStaticClass.twittershareon = true;
                } else {
                    editor.putBoolean(CommonStaticClass.TWITTERSHARE, false);
                    twitterShareToggle.setImageResource(R.drawable.toggle_off);
                    CommonStaticClass.twittershareon = false;
                }
                editor.commit();
            }
        });
        Boolean isTwitterShareEnable = app_preferences.getBoolean(CommonStaticClass.TWITTERSHARE, false);

        if (isTwitterShareEnable) {
            CommonStaticClass.twittershareon = true;


        } else {
            CommonStaticClass.twittershareon = false;


        }
        performApiCall();

        twitterLoginToggle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform action on clicks
                if (twitterLoginToggle.isChecked()) {
                    startActivity(new Intent().setClass(v.getContext(), OAuthAccessTokenActivity.class));
                } else {
                    clearCredentials();
                }
            }
        });
    }

    public class SampleAuthListener implements AuthListener {

        public void onAuthSucceed() {
            saveCredentials(mFacebook);
            mAsyncRunner.request("me", new meRequestListener());
            CommonStaticClass.facebookLogin = true;
//        	mAsyncRunner.request("me/likes", new meLikesListener());

        }

        public void onAuthFail(String error) {

        }
    }

    class meRequestListener extends BaseRequestListener {
        public void onComplete(String response, final Object state) {
            String email = "";
            try {
                JSONObject jsonObj = new JSONObject(response);
                if (jsonObj.NULL != null) {
                    email = jsonObj.getString("email");
                }
                CommonStaticClass.facebookUserId = email;
                CommonStaticClass.facebookLogin = true;
                Log.e("Response email", "" + email);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
//        	finish();
        }
    }

    //    class meLikesListener extends BaseRequestListener {
//        public void onComplete(String response, final Object state) {
//        	Log.e("Response likes",""+response);
////        	String email = "";
////    		try {
////    			JSONObject jsonObj = new JSONObject(response);
////    			if (jsonObj.NULL != null) {
////					email = jsonObj.getString("email");
////    			}
////    			CommonStaticClass.facebookUserId = email;
////            	Log.e("Response email",""+email);
////    		} catch (Exception e) {
////    			e.printStackTrace();
////    		}
////        	finish();
//        }
//    }
    public class SampleLogoutListener implements LogoutListener {
        public void onLogoutBegin() {
        }

        public void onLogoutFinish() {
            CommonStaticClass.facebookLogin = false;
            loginButton.isSessionActive = false;
            fbShareToggle.setImageResource(R.drawable.toggle_off);
        }
    }

    private void performApiCall() {
        Exception e = new Exception();
        try {
            String[] tokens = new SharedPreferencesCredentialStore(app_preferences).read();
            if ((tokens[0].length() > 2) || (tokens[1].length() > 2)) {
                AccessToken a = new AccessToken(tokens[0], tokens[1]);
                Twitter twitter = new TwitterFactory().getInstance();
                twitter.setOAuthConsumer(CommonStaticClass.CONSUMER_KEY, CommonStaticClass.CONSUMER_SECRET);
                twitter.setOAuthAccessToken(a);
                CommonStaticClass.twitterLogin = true;
                CommonStaticClass.twitterUserId = twitter.getScreenName();
            } else {
                throw (e);
            }

            twitterLoginToggle.setChecked(true);
        } catch (Exception ex) {
            twitterLoginToggle.setChecked(false);
            ex.printStackTrace();
        }
    }

    private void clearCredentials() {
        new SharedPreferencesCredentialStore(app_preferences).clearCredentials();
    }
}
