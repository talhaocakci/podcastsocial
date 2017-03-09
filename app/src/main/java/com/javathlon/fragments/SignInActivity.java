package com.javathlon.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.javathlon.BaseActivity;
import com.javathlon.LoginActivity;
import com.javathlon.MainActivity;
import com.javathlon.QuickstartPreferences;
import com.javathlon.R;
import com.javathlon.RegisterActivity;
import com.javathlon.apiclient.ApiClient;
import com.javathlon.apiclient.api.ApplicationresourceApi;
import com.javathlon.apiclient.api.SessionConstants;
import com.javathlon.apiclient.api.SubscriberresourceApi;
import com.javathlon.apiclient.api.UserresourceApi;
import com.javathlon.apiclient.model.ApplicationDTO;
import com.javathlon.apiclient.model.ManagedUserVM;
import com.javathlon.apiclient.model.Subscriber;
import com.javathlon.apiclient.model.SubscriberDTO;
import com.javathlon.db.DBAccessor;
import com.javathlon.memsoft.ImageUtil;
import com.javathlon.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.data;

public class SignInActivity extends AppCompatActivity implements OnClickListener,
        GoogleApiClient.ConnectionCallbacks, ResultCallback,
        GoogleApiClient.OnConnectionFailedListener {

    CallbackManager callBackManager;
    DBAccessor dbAccessor = null;

    private Context context;
    private static final String TAG = "SignInActivity";

    private static final int DIALOG_GET_GOOGLE_PLAY_SERVICES = 1;

    private static final int REQUEST_CODE_SIGN_IN = 1;
    private static final int REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES = 2;

    private static final String KEY_NEW_CODE_REQUIRED = "codeRequired";

    private static String GOOGLE_ID = "";
    private static String FB_ID = "";

    private GoogleApiClient mGoogleApiClient;
    private SignInButton mSignInButton;
    private View mSignOutButton;
    private TextView descriptionTextView;
    private ImageView logoView;
    private ImageView coverImageView;
    private TextView applicationNameView;
    private TextView loginButton;


    boolean intentionally = false;

    //password: android
    //alias: podcastmoderndevelopment
    private static String podcastmoderndevelopment = "634509108343-lhhvoqthbhptt2cfbc0r7qmoib6miv66.apps.googleusercontent.com";

    /*
     * Stores the connection result from onConnectionFailed callbacks so that we can resolve them
     * when the user clicks sign-in.
     */
    private ConnectionResult mConnectionResult;

    /*
     * Tracks whether the sign-in button has been clicked so that we know to resolve all issues
     * preventing sign-in without waiting.
     */
    private boolean mSignInClicked;

    /*
     * Tracks whether a resolution Intent is in progress.
     */
    private boolean mIntentInProgress;

    /**
     * Tracks the emulated state of whether a new server auth code is required.
     */
    private final AtomicBoolean mServerAuthCodeRequired = new AtomicBoolean(false);
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * Whether Verbose is loggable.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean mIsLogVerbose;

        if (getIntent() != null && getIntent().getExtras() != null)
            intentionally = getIntent().getExtras().getBoolean("intentionally", false);

        context = this;

        if (!FacebookSdk.isInitialized()) {
            FacebookSdk.sdkInitialize(getApplicationContext());
        }

        Bundle bundle = this.getIntent().getExtras();
        if (bundle == null || Boolean.FALSE.equals(bundle.getBoolean("intentionally"))) {
            if (dbAccessor == null) {
                dbAccessor = new DBAccessor(SignInActivity.this);
                dbAccessor.open();
            }
            User user = dbAccessor.getAnyUser();
            if (user.getUserId() != 0L) {
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            }
        }


        // If you want to understand the life cycle more, you can use below command to turn on
        // verbose logging for this Activity on your testing device:
        // adb shell setprop log.tag.SignInActivity VERBOSE
        mIsLogVerbose = Log.isLoggable(TAG, Log.VERBOSE);

        setContentView(R.layout.social_login);
        restoreState(savedInstanceState);

        getSupportActionBar().hide();

        descriptionTextView = (TextView) findViewById(R.id.applicationDescription);
        logoView = (ImageView) findViewById(R.id.mainlogo);
        coverImageView = (ImageView) findViewById(R.id.coverImage);
        applicationNameView = (TextView) findViewById(R.id.applicationName);

        callBackManager = CallbackManager.Factory.create();

        LoginButton fbLoginButton = (LoginButton) findViewById(R.id.fb_login_button);

        Bundle b = this.getIntent().getExtras();

        final ApiClient apiClient = ApiClient.getApiClient(getApplicationContext());
        final ApplicationresourceApi api = apiClient.createService(ApplicationresourceApi.class);

        loginButton = (TextView) findViewById(R.id.link_login);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        if (!BaseActivity.isNetworkAvailable(getBaseContext())) {

            Toast.makeText(getBaseContext(), "Please check internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        mGoogleApiClient = buildGoogleApiClient(true);

        Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(this);

        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(this);


        mSignOutButton = findViewById(R.id.sign_out_button);
        mSignOutButton.setOnClickListener(this);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        try {
            Response<ApplicationDTO> app = new AsyncTask<String, Void, Response<ApplicationDTO>>() {
                protected Response<ApplicationDTO> doInBackground(String... param) {
                    Response<ApplicationDTO> app = null;
                    try {
                        app = api.getApplicationUsingGET(apiClient.appId).execute();


                        return app;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return app;
                }

            }.execute().get();

            if (app == null || app.body() == null) {
                Toast.makeText(getBaseContext(), "Our server has problems, sorry for that...", Toast.LENGTH_SHORT).show();
                return;
            }

            if (app.body() != null) {
                descriptionTextView.setText(app.body().getDescription());
                ImageUtil.displayImage(logoView, app.body().getLogo(), null);
                ImageUtil.displayImage(coverImageView, app.body().getCoverImage(), null);
                applicationNameView.setText(app.body().getName());
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        if (b != null && b.getString("filepath") != null) {

            if (AccessToken.getCurrentAccessToken() != null) {
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(b.getString("filepath")))
                        .setContentDescription("podcast snippet")
                        .setImageUrl(Uri.parse("https://lh6.ggpht.com/gEvGApz7lFGZvAcFaQDBBbpNmWCbaZ-zCU0irP8YFDURcUJdQMC4MrVmDb8h0meDnQs=w300"))
                        .build();

                ShareDialog.show(SignInActivity.this, content);
            }
        }


        float fbIconScale = 1.45F;
        Drawable drawable = this.getResources().getDrawable(
                com.facebook.R.drawable.com_facebook_button_icon);
        drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * fbIconScale),
                (int) (drawable.getIntrinsicHeight() * fbIconScale));
        fbLoginButton.setCompoundDrawables(drawable, null, null, null);
        fbLoginButton.setCompoundDrawablePadding(this.getResources().
                getDimensionPixelSize(R.dimen.fb_margin_override_textpadding));
        fbLoginButton.setPadding(
                fbLoginButton.getResources().getDimensionPixelSize(
                        R.dimen.fb_margin_override_lr),
                fbLoginButton.getResources().getDimensionPixelSize(
                        R.dimen.fb_margin_override_top),
                0,
                fbLoginButton.getResources().getDimensionPixelSize(
                        R.dimen.fb_margin_override_bottom));

        fbLoginButton.setReadPermissions(Arrays.asList(new String[]{"user_friends", "public_profile"}));


        fbLoginButton.registerCallback(callBackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                AccessToken token = loginResult.getAccessToken();
                Log.d(token.getUserId(), token.getUserId());

                GraphRequest request = GraphRequest.newMeRequest(
                        token,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Application code
                                //{"id":"10152967857023199","gender":"male","email":"btocakci@gmail.com","age_range":{"min":21},"locale":"en_US","link":"https:\/\/www.facebook.com\/app_scoped_user_id\/10152967857023199\/","name":"Talha Ocakçı"}
                                JSONObject userObj = response.getJSONObject();
                                try {
                                    String id = userObj.getString("id");
                                    String gender = userObj.getString("gender");
                                    String email = userObj.getString("email");
                                    JSONObject age = userObj.getJSONObject("age_range");
                                    String minAge = age.getString("min");
                                    String name = userObj.getString("name");

                                    if (dbAccessor == null) {
                                        dbAccessor = new DBAccessor(SignInActivity.this);
                                        dbAccessor.open();
                                    }
                                    long checkId = dbAccessor.getUserIdWithEmail(email);
                                    if (-1l == checkId) {
                                        User user = new User();
                                        user.setDisplayName(name);
                                        if ("male".equals(gender))
                                            user.setGender("m");
                                        else
                                            user.setGender("f");
                                        user.setEmail(email);
                                        if (minAge != null && !minAge.equals(""))
                                            user.setMinAge(Integer.parseInt(minAge));
                                        user.setFbToken(id);
                                        dbAccessor.insertUser(user);
                                    } else {
                                        dbAccessor.updateUserSocialId(checkId, User.SOCIAL_TYPES.FACEBOOK, id);
                                    }

                                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(SignInActivity.this);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("fbtoken", id);
                                    editor.apply();


                                    Log.d("fb auth", name);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.d("", object.toString());
                                ShareLinkContent content = new ShareLinkContent.Builder()
                                        .setContentUrl(Uri.parse("https://developers.facebook.com"))
                                        .setContentDescription("pod addict deneme")
                                        .setImageUrl(Uri.parse("https://lh6.ggpht.com/gEvGApz7lFGZvAcFaQDBBbpNmWCbaZ-zCU0irP8YFDURcUJdQMC4MrVmDb8h0meDnQs=w300"))
                                        .build();

                                ShareDialog.show(SignInActivity.this, content);


                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,email, gender, hometown, locale, location, sports,age_range, address");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                Log.d("", "");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d("", "");
            }
        });


        // FACEBOOK END/////////


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        logVerbose(String.format("onActivityResult - requestCode:%d resultCode:%d", requestCode,
                resultCode));

        if (requestCode == REQUEST_CODE_SIGN_IN
                || requestCode == REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES) {
            mIntentInProgress = false; //Previous resolution intent no longer in progress.

            if (resultCode == RESULT_OK) {
                // After resolving a recoverable error, now retry connect(). Note that it's possible
                // mGoogleApiClient is already connected or connecting due to rotation / Activity
                // restart while user is walking through the (possibly full screen) resolution
                // Activities. We should always reconnect() and ignore earlier connection attempts
                // started before completion of the resolution. (With only one exception, a
                // connect() attempt started late enough in the resolution flow and it actually
                // succeeded)
                if (!mGoogleApiClient.isConnected()) {
                    logVerbose("Previous resolution completed successfully, try connecting again");
                    mGoogleApiClient.reconnect();
                }
            } else {
                mSignInClicked = false; // No longer in the middle of resolving sign-in errors.

            }
        } else {
            callBackManager.onActivityResult(requestCode, resultCode, data);
        }


    }

    private void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mServerAuthCodeRequired.set(isUsingOfflineAccess());
        } else {
            mServerAuthCodeRequired.set(
                    savedInstanceState.getBoolean(KEY_NEW_CODE_REQUIRED, false));
        }
    }

    private GoogleApiClient buildGoogleApiClient(boolean useProfileScope) {
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this);

        String serverClientId = podcastmoderndevelopment;

        if (!TextUtils.isEmpty(serverClientId)) {
            //TODO builder.requestServerAuthCode(serverClientId, this);
        }


        builder.addApi(Plus.API, Plus.PlusOptions.builder()
                .build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE);


        return builder.build();
    }

    private boolean isUsingOfflineAccess() {
        // the emulation of offline access negotiation is enabled/disabled by
        // specifying the server client ID of the app in strings.xml - if no
        // value is present, we do not request offline access.
        return !TextUtils.isEmpty(podcastmoderndevelopment);
    }

    @Override
    public void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        logVerbose("Activity onStart, starting connecting GoogleApiClient");
        mGoogleApiClient.connect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        logVerbose("Activity onStop, disconnecting GoogleApiClient");
        mGoogleApiClient.disconnect();
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(KEY_NEW_CODE_REQUIRED, mServerAuthCodeRequired.get());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                if (!mGoogleApiClient.isConnecting()) {
                    int available = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
                    if (available != ConnectionResult.SUCCESS) {
                        showDialog(DIALOG_GET_GOOGLE_PLAY_SERVICES);
                        return;
                    }

                    mSignInClicked = true;
                    resolveSignInError();
                }
                break;

            case R.id.sign_out_button:
                if (mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.clearDefaultAccountAndReconnect();
                }
                break;

        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id != DIALOG_GET_GOOGLE_PLAY_SERVICES) {
            return super.onCreateDialog(id);
        }

        int available = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (available == ConnectionResult.SUCCESS) {
            return null;
        }
        if (GooglePlayServicesUtil.isUserRecoverableError(available)) {
            return GooglePlayServicesUtil.getErrorDialog(
                    available, this, REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES);
        }
        return new AlertDialog.Builder(this)
                .setMessage(R.string.plus_generic_error)
                .setCancelable(true)
                .create();
    }


    public boolean onUploadServerAuthCode(String idToken, String serverAuthCode) {
        Log.d(TAG, "upload server auth code " + serverAuthCode + " requested, faking success");
        mServerAuthCodeRequired.set(false);
        return true;
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        logVerbose("GoogleApiClient onConnected");

        Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
        String currentPersonName = person != null
                ? person.getDisplayName()
                : getString(R.string.unknown_person);

        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(this.getIntent());


        String birthday = "";
        String name = "";
        int gender = 0;
        Person.Image image = null;
        String currentLocation = "";
        String id = "";
        String lang = "";
        int relationShip = 0;

        if (person != null) {
            birthday = person.getBirthday();
            name = person.getDisplayName();
            gender = person.getGender();
            image = person.getImage();
            currentLocation = person.getCurrentLocation();
            id = person.getId();
            lang = person.getLanguage();
            relationShip = person.getRelationshipStatus();
        }


        if (dbAccessor == null) {
            dbAccessor = new DBAccessor(SignInActivity.this);
            dbAccessor.open();
        }
        long checkId = dbAccessor.getUserIdWithEmail(email);


        SubscriberDTO subscriber = new SubscriberDTO();

        if (-1l == checkId) {

            subscriber.setName(person.getName().getGivenName());
            subscriber.setSurname(person.getName().getFamilyName());
            subscriber.setLanguage(lang);
            if (0 == gender)
                subscriber.setGender("m");
            else
                subscriber.setGender("f");
            subscriber.setEmail(email);
            //subscriber.setGcmToken();
            subscriber.setGoogleId(person.getId());
            subscriber.setServiceName("google");

            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String gcmToken = sharedPreferences.getString(QuickstartPreferences.GCM_TOKEN, "");
            subscriber.setGcmToken(gcmToken);

            SubscriberresourceApi subscriberAPI = ApiClient.getApiClient(getApplicationContext()).createService(SubscriberresourceApi.class);
            final UserresourceApi userApi = ApiClient.getApiClient(getApplicationContext()).createService(UserresourceApi.class);


            subscriberAPI.createSubscriberUsingPOST(subscriber).enqueue(new Callback<SubscriberDTO>() {
                @Override
                public void onResponse(Call<SubscriberDTO> call, Response<SubscriberDTO> response) {
                    if (response.isSuccessful()) {

                        SubscriberDTO subscriber = response.body();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(QuickstartPreferences.SUBSCRIBER_ID, subscriber.getId().toString());
                        editor.putString(QuickstartPreferences.NAME, subscriber.getName());
                        editor.putString(QuickstartPreferences.SURNAME, subscriber.getSurname());
                        editor.putString(QuickstartPreferences.EMAIL, subscriber.getEmail());
                        editor.putString("username", subscriber.getEmail());

                        editor.apply();

                        boolean isUser = false;
                        try {
                             isUser = new AsyncTask<String, Void, Boolean>() {
                                @Override
                                protected Boolean doInBackground(String... params) {
                                    return userApi.getUserUsingGET(params[0]) != null;
                                }
                            }.execute(subscriber.getEmail()).get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                        if(!isUser) {
                            Intent i = new Intent(context, RegisterActivity.class);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(context, LoginActivity.class);
                            startActivity(i);
                        }
                    }

                }

                @Override
                public void onFailure(Call<SubscriberDTO> call, Throwable t) {
                    Toast.makeText(context, "Google signin failed", Toast.LENGTH_SHORT).show();
                }
            });

            User user = new User();
            user.setGcmToken(gcmToken);
            user.setEmail(email);
            dbAccessor.insertUser(user);
            SessionConstants.subscriber = subscriber;

        } else {
            dbAccessor.updateUserSocialId(checkId, User.SOCIAL_TYPES.GOOGLE, id);
            if (!intentionally) {
                Intent i = new Intent(context, LoginActivity.class);
                startActivity(i);
            }
            intentionally = false;
        }


        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(SignInActivity.this);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("googletoken", id);
        editor.apply();

        //mSignInStatus.setText(getString(R.string.signed_in_status, currentPersonName));
        updateButtons(true /* isSignedIn */);
        mSignInClicked = false;
    }

    @Override
    public void onConnectionSuspended(int cause) {
        logVerbose("GoogleApiClient onConnectionSuspended");
        mGoogleApiClient.connect();
        updateButtons(false /* isSignedIn */);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        logVerbose(String.format("GoogleApiClient onConnectionFailed, error code: %d, with " +
                "resolution: %b", result.getErrorCode(), result.hasResolution()));
        if (!mIntentInProgress) {
            logVerbose("Caching the failure result");
            mConnectionResult = result;

            if (mSignInClicked) {
                resolveSignInError();
            }
            updateButtons(false /* isSignedIn */);
        } else {
            logVerbose("Intent already in progress, ignore the new failure");
        }
    }

    private void updateButtons(boolean isSignedIn) {
        if (isSignedIn) {
            mSignInButton.setVisibility(View.GONE);
            mSignOutButton.setVisibility(View.VISIBLE);
            mSignOutButton.setEnabled(true);
        } else {
            if (mConnectionResult == null) {
                // Disable the sign-in button until onConnectionFailed is called with result.
                mSignInButton.setVisibility(View.INVISIBLE);
            } else {
                // Enable the sign-in button since a connection result is available.
                mSignInButton.setVisibility(View.VISIBLE);
                mSignOutButton.setVisibility(View.GONE);
            }

            mSignOutButton.setEnabled(false);
        }
    }

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                logVerbose("Start the resolution intent, flipping the intent-in-progress bit.");
                mConnectionResult.startResolutionForResult(this, REQUEST_CODE_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default state and
                // attempt to connect to get an updated ConnectionResult.
                mIntentInProgress = false;
                mGoogleApiClient.connect();
                Log.w(TAG, "Error sending the resolution Intent, connect() again.");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:


                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logVerbose(String message) {

        Log.v(TAG, message);

    }

    @Override
    public void onResult(Result result) {

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("SignIn Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
}

