package com.javathlon.fragments;


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
        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.GooglePlayServicesUtil;
        import com.google.android.gms.common.SignInButton;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.common.api.Result;
        import com.google.android.gms.common.api.ResultCallback;
        import com.google.android.gms.plus.Plus;
        import com.google.android.gms.plus.model.people.Person;
        import com.google.gson.Gson;
        import com.javathlon.ApplicationSettings;
        import com.javathlon.MainActivity;
        import com.javathlon.R;
        import com.javathlon.db.DBAccessor;
        import com.javathlon.QuickstartPreferences;
        import com.javathlon.memsoft.WebServiceAsyncTaskFormPost;
        import com.javathlon.model.User;

        import android.app.Activity;
        import android.app.AlertDialog;
        import android.app.Dialog;
        import android.content.Intent;
        import android.content.IntentSender;
        import android.content.SharedPreferences;
        import android.graphics.drawable.Drawable;
        import android.net.Uri;
        import android.os.Build;
        import android.os.Bundle;
        import android.preference.PreferenceManager;
        import android.text.TextUtils;
        import android.util.Log;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.TextView;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.Arrays;
        import java.util.concurrent.atomic.AtomicBoolean;

public class SignInActivity extends Activity implements OnClickListener,
        GoogleApiClient.ConnectionCallbacks, ResultCallback,
        GoogleApiClient.OnConnectionFailedListener
{

    CallbackManager callBackManager;
    DBAccessor dbAccessor = null;


    private static final String TAG  = "SignInActivity";

    private static final int DIALOG_GET_GOOGLE_PLAY_SERVICES = 1;

    private static final int REQUEST_CODE_SIGN_IN = 1;
    private static final int REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES = 2;

    private static final String KEY_NEW_CODE_REQUIRED = "codeRequired";

    private static String GOOGLE_ID = "";
    private static String FB_ID = "";

    private TextView mSignInStatus;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton mSignInButton;
    private View mSignOutButton;

    //password: android
    //alias: podcastmoderndevelopment
    private static String podcastmoderndevelopment = "334941374162-evcoffbh8a5sjj64khpo6jblambu9uh5.apps.googleusercontent.com";

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
     * Whether Verbose is loggable.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     boolean mIsLogVerbose;



        if (!FacebookSdk.isInitialized()) {
            FacebookSdk.sdkInitialize(getApplicationContext());
        }

        Bundle bundle = this.getIntent().getExtras();
        if(bundle == null || Boolean.FALSE.equals(bundle.getBoolean("intentionally"))){
            if (dbAccessor == null) {
                dbAccessor = new DBAccessor(SignInActivity.this);
                dbAccessor.open();
            }
            User user = dbAccessor.getAnyUser();
            if(user.getUserId() != 0L){
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

        callBackManager = CallbackManager.Factory.create();

        LoginButton fbLoginButton = (LoginButton) findViewById(R.id.fb_login_button);

        Bundle b =  this.getIntent().getExtras();

        if(b!= null && b.getString("filepath") != null ) {

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
        drawable.setBounds(0, 0, (int)(drawable.getIntrinsicWidth()*fbIconScale),
                (int)(drawable.getIntrinsicHeight()*fbIconScale));
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
                                            if(-1l == checkId )
                                            {
                                                User user = new User();
                                                user.setDisplayName(name);
                                                if("male".equals(gender))
                                                    user.setGender("m");
                                                else
                                                    user.setGender("f");
                                                user.setEmail(email);
                                                if(minAge != null && !minAge.equals(""))
                                                    user.setMinAge(Integer.parseInt(minAge));
                                                user.setFbToken(id);
                                                dbAccessor.insertUser(user);
                                            }else
                                            {
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



        mGoogleApiClient = buildGoogleApiClient(true);

        mSignInStatus = (TextView) findViewById(R.id.sign_in_status);
        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(this);



        mSignOutButton = findViewById(R.id.sign_out_button);
        mSignOutButton.setOnClickListener(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
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

                        if (resultCode == RESULT_CANCELED) {
                            mSignInStatus.setText(getString(R.string.signed_out_status));
                        } else {
                            mSignInStatus.setText(getString(R.string.sign_in_error_status));
                            Log.w(TAG, "Error during resolving recoverable error.");
                        }
                    }
                }

                else {
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
                    .setServerClientId(serverClientId)
                    .build())
                    .addScope(Plus.SCOPE_PLUS_LOGIN).addScope(Plus.SCOPE_PLUS_PROFILE);


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
        super.onStart();
        logVerbose("Activity onStart, starting connecting GoogleApiClient");
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        logVerbose("Activity onStop, disconnecting GoogleApiClient");
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(KEY_NEW_CODE_REQUIRED, mServerAuthCodeRequired.get());
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.sign_in_button:
                if (!mGoogleApiClient.isConnecting()) {
                    int available = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
                    if (available != ConnectionResult.SUCCESS) {
                        showDialog(DIALOG_GET_GOOGLE_PLAY_SERVICES);
                        return;
                    }

                    mSignInClicked = true;
                    mSignInStatus.setText(getString(R.string.signing_in_status));
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
        if(person == null) {
            return;
        }
            String birthday = person.getBirthday();
            String name = person.getDisplayName();
            int gender = person.getGender();
            Person.Image image = person.getImage();
            String currentLocation = person.getCurrentLocation();
            String id = person.getId();
            String lang = person.getLanguage();
            int relationShip = person.getRelationshipStatus();
            Person.AgeRange ageRange = person.getAgeRange();


        if (dbAccessor == null) {
            dbAccessor = new DBAccessor(SignInActivity.this);
            dbAccessor.open();
        }
        long checkId = dbAccessor.getUserIdWithEmail(email);

        User user = new User();
        String userJson = "";
        if(-1l == checkId )
        {

            user.setName(person.getName().getGivenName());
            user.setSurname(person.getName().getFamilyName());
            user.setDisplayName(name);
            user.setLanguage(lang);
            user.setLocation(currentLocation);
            if(0 == gender)
                user.setGender("m");
            else
                user.setGender("f");
            user.setEmail(email);
            if(ageRange != null )
                user.setMinAge(ageRange.getMin());
            user.setGoogleToken(id);



            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String gcmToken = sharedPreferences.getString(QuickstartPreferences.GCM_TOKEN, "");
            user.setGcmToken(gcmToken);


            Gson gson = new Gson();
            userJson = gson.toJson(user, User.class);



            new WebServiceAsyncTaskFormPost().execute(ApplicationSettings.podcastModernServerUrl + "/application/"+ApplicationSettings.appId, "userString", userJson);
            dbAccessor.insertUser(user);
        }else
        {
            dbAccessor.updateUserSocialId(checkId, User.SOCIAL_TYPES.GOOGLE, id);
        }


        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(SignInActivity.this);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("googletoken", id);
        editor.apply();

        mSignInStatus.setText(getString(R.string.signed_in_status, currentPersonName));
        updateButtons(true /* isSignedIn */);
        mSignInClicked = false;
    }

    @Override
    public void onConnectionSuspended(int cause) {
        logVerbose("GoogleApiClient onConnectionSuspended");
        mSignInStatus.setText(R.string.loading_status);
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
                mSignInStatus.setText(getString(R.string.loading_status));
            } else {
                // Enable the sign-in button since a connection result is available.
                mSignInButton.setVisibility(View.VISIBLE);
                mSignInStatus.setText(getString(R.string.signed_out_status));
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
}

