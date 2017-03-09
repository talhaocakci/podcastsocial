package com.javathlon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.javathlon.apiclient.ApiClient;
import com.javathlon.apiclient.api.AccountresourceApi;
import com.javathlon.apiclient.api.SessionConstants;
import com.javathlon.apiclient.api.SubscriptionresourceApi;
import com.javathlon.apiclient.model.ManagedUserVM;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";

    EditText _nameText;
    EditText _emailText;
    EditText _passwordText;
    EditText _reEnterPasswordText;
    Button _signupButton;
    TextView _loginLink;

    String subscriberId = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        _nameText = (EditText) findViewById(R.id.input_name);
        _emailText = (EditText) findViewById(R.id.input_email);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _reEnterPasswordText = (EditText) findViewById(R.id.input_reEnterPassword);
        _signupButton = (Button) findViewById(R.id.btn_signup);
        _loginLink = (TextView) findViewById(R.id.link_login);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String subscriberId = sharedPreferences.getString(QuickstartPreferences.SUBSCRIBER_ID, "");
        _nameText.setText(sharedPreferences.getString(QuickstartPreferences.NAME, ""));
        _emailText.setText(sharedPreferences.getString(QuickstartPreferences.EMAIL, ""));
        String gcmToken = sharedPreferences.getString(QuickstartPreferences.GCM_TOKEN, "");

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String name = _nameText.getText().toString();
        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();


        AccountresourceApi api = ApiClient.getApiClient(getApplicationContext()).createService(AccountresourceApi.class);
        final SubscriptionresourceApi subscriptionAPI = ApiClient.getApiClient(getApplicationContext()).createService(SubscriptionresourceApi.class);

        ManagedUserVM user = new ManagedUserVM();
        user.setEmail(email);
        user.setLogin(email);
        user.setPassword(password);

        if (SessionConstants.subscriber != null) {
            user.setFirstName(SessionConstants.subscriber.getName());
            user.setLastName(SessionConstants.subscriber.getSurname());
        }

        api.registerUser(user).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "user createad");
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Registration successfull", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);

                    i.putExtra("username", email);
                    i.putExtra("password", password);

                    startActivity(i);

                } else {
                    if (response.code() == 500) {
                        Toast.makeText(getApplicationContext(), "User can not be registered, please try again later", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "User is already registered, try logging in", Toast.LENGTH_SHORT).show();

                    }

                    progressDialog.dismiss();
                    Log.d(TAG, "user not created");

                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "user not created");
                progressDialog.dismiss();
            }
        });
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }
}
