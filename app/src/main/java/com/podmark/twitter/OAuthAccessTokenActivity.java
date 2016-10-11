package com.podmark.twitter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.api.client.auth.oauth.OAuthAuthorizeTemporaryTokenUrl;
import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import com.google.api.client.auth.oauth.OAuthGetAccessToken;
import com.google.api.client.auth.oauth.OAuthGetTemporaryToken;
import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.javathlon.CommonStaticClass;
import com.podmark.twitter.store.CredentialStore;
import com.podmark.twitter.store.SharedPreferencesCredentialStore;
import com.podmark.twitter.util.QueryStringParser;

import java.io.IOException;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;

public class OAuthAccessTokenActivity extends Activity {

    final String TAG = getClass().getName();

    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Starting task to retrieve request token.");
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        WebView webview = new WebView(this);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setVisibility(View.VISIBLE);
        setContentView(webview);

        Log.i(TAG, "Retrieving request token from Google servers");

        try {

            final OAuthHmacSigner signer = new OAuthHmacSigner();
            signer.clientSharedSecret = CommonStaticClass.CONSUMER_SECRET;

            OAuthGetTemporaryToken temporaryToken = new OAuthGetTemporaryToken(CommonStaticClass.REQUEST_URL);
            temporaryToken.transport = new ApacheHttpTransport();
            temporaryToken.signer = signer;
            temporaryToken.consumerKey = CommonStaticClass.CONSUMER_KEY;
            temporaryToken.callback = CommonStaticClass.OAUTH_CALLBACK_URL;

            OAuthCredentialsResponse tempCredentials = temporaryToken.execute();
            signer.tokenSharedSecret = tempCredentials.tokenSecret;

            OAuthAuthorizeTemporaryTokenUrl authorizeUrl = new OAuthAuthorizeTemporaryTokenUrl(CommonStaticClass.AUTHORIZE_URL);
            authorizeUrl.temporaryToken = tempCredentials.token;
            String authorizationUrl = authorizeUrl.build();

	        
	        /* WebViewClient must be set BEFORE calling loadUrl! */
            webview.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageStarted(WebView view, String url, Bitmap bitmap) {
                    System.out.println("onPageStarted : " + url);
                }

                @Override
                public void onPageFinished(WebView view, String url) {

                    if (url.startsWith(CommonStaticClass.OAUTH_CALLBACK_URL)) {
                        try {

                            if (url.indexOf("oauth_token=") != -1) {

                                String requestToken = extractParamFromUrl(url, "oauth_token");
                                String verifier = extractParamFromUrl(url, "oauth_verifier");

                                signer.clientSharedSecret = CommonStaticClass.CONSUMER_SECRET;

                                OAuthGetAccessToken accessToken = new OAuthGetAccessToken(CommonStaticClass.ACCESS_URL);
                                accessToken.transport = new ApacheHttpTransport();
                                accessToken.temporaryToken = requestToken;
                                accessToken.signer = signer;
                                accessToken.consumerKey = CommonStaticClass.CONSUMER_KEY;
                                accessToken.verifier = verifier;

                                OAuthCredentialsResponse credentials = accessToken.execute();
                                signer.tokenSharedSecret = credentials.tokenSecret;

                                CredentialStore credentialStore = new SharedPreferencesCredentialStore(prefs);
                                credentialStore.write(new String[]{credentials.token, credentials.tokenSecret});
                                view.setVisibility(View.INVISIBLE);

                                try {
                                    AccessToken a = new AccessToken(credentials.token, credentials.tokenSecret);
                                    Twitter twitter = new TwitterFactory().getInstance();
                                    twitter.setOAuthConsumer(CommonStaticClass.CONSUMER_KEY, CommonStaticClass.CONSUMER_SECRET);
                                    twitter.setOAuthAccessToken(a);
                                    CommonStaticClass.twitterLogin = true;
                                    CommonStaticClass.twitterUserId = twitter.getScreenName();
                                } catch (IllegalStateException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (TwitterException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                finish();
//					  		      startActivity(new Intent(OAuthAccessTokenActivity.this,AndroidTwitterGoogleApiJavaClientActivity.class));
                            } else if (url.indexOf("error=") != -1) {
                                view.setVisibility(View.INVISIBLE);
                                new SharedPreferencesCredentialStore(prefs).clearCredentials();
                                finish();
//	            				startActivity(new Intent(OAuthAccessTokenActivity.this,AndroidTwitterGoogleApiJavaClientActivity.class));
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    System.out.println("onPageFinished : " + url);

                }

                private String extractParamFromUrl(String url, String paramName) {
                    String queryString = url.substring(url.indexOf("?", 0) + 1, url.length());
                    QueryStringParser queryStringParser = new QueryStringParser(queryString);
                    return queryStringParser.getQueryParamValue(paramName);
                }

            });

            webview.loadUrl(authorizationUrl);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
