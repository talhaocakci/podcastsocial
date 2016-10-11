package com.podmark.twitter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.android.ShareOnFacebook;
import com.javathlon.CommonStaticClass;
import com.javathlon.LoginPref;
import com.javathlon.R;
import com.podmark.twitter.store.SharedPreferencesCredentialStore;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;

public class TweetOnTwitter extends Activity {
    private SharedPreferences prefs;
    private boolean only;
    private boolean fbToo;
    private String messageToPost, originalMessage;
    Context con;
    private EditText userText;
    Button TwitterShareButton, TwitterShareNotButton;
    int length = 159;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sharetotwitter);
        con = this;
        prefs = PreferenceManager.getDefaultSharedPreferences(this);


        userText = (EditText) findViewById(R.id.userText);
        TwitterShareNotButton = (Button) findViewById(R.id.TwitterShareNotButton);
        TwitterShareButton = (Button) findViewById(R.id.TwitterShareButton);

        String twitterMessage = getIntent().getStringExtra("message");
        only = getIntent().getBooleanExtra("only", true);
        if (only) {
            fbToo = false;
        } else {
            fbToo = true;
        }
        if (twitterMessage == null) {
            twitterMessage = "Test wall post";
        }
        originalMessage = twitterMessage;
        messageToPost = twitterMessage;

        TwitterShareButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String usermsg = userText.getText().toString();
                if (usermsg.length() < 1 || usermsg.length() > (159 - messageToPost.length())) {
                    showToast("Please put your message correctly within 0 to " + (159 - messageToPost.length()) + " character");
                } else {
                    messageToPost = usermsg + messageToPost;
                    try {
                        statusUpdate(messageToPost);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        });
        TwitterShareNotButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }

    private void statusUpdate(String status) throws Exception {
        Exception e = new TwitterException("myexception");
        try {
            String[] tokens = new SharedPreferencesCredentialStore(prefs).read();
            if ((tokens[0].length() > 2) || (tokens[1].length() > 2)) {
                AccessToken a = new AccessToken(tokens[0], tokens[1]);
                Twitter twitter = new TwitterFactory().getInstance();
                twitter.setOAuthConsumer(CommonStaticClass.CONSUMER_KEY, CommonStaticClass.CONSUMER_SECRET);
                twitter.setOAuthAccessToken(a);
                twitter.updateStatus(status);
                showToast("Tweet successful");
                if (fbToo) {
                    Intent postOnFacebookWallIntent = new Intent(con, ShareOnFacebook.class);
                    postOnFacebookWallIntent.putExtra("message", originalMessage);
                    postOnFacebookWallIntent.putExtra("only", true);
                    con.startActivity(postOnFacebookWallIntent);
                }

                finish();
            } else {
                throw (e);
            }
        } catch (TwitterException ex) {
            ex.printStackTrace();
            if (ex.getStatusCode() == 403) {
                showToast("Satus is a duplicate try again");
            } else {
                showToast("Twitter need re-login");
                Intent i = new Intent(TweetOnTwitter.this, LoginPref.class);
                startActivity(i);
            }

        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}