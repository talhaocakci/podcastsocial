package com.javathlon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognito.internal.util.StringUtils;
import com.javathlon.apiclient.StringUtil;


public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    EditText _emailText;
    EditText _passwordText;
    Button _loginButton;
    TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _emailText = (EditText) findViewById(R.id.input_email);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _loginButton = (Button) findViewById(R.id.btn_login);
        _signupLink = (TextView) findViewById(R.id.link_signup);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userName = preferences.getString("username", "");
        String password = preferences.getString("password", "");

        _emailText.setText(userName);
        if(!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(password)) {
            boolean loggedIn = login(userName, password);
            _passwordText.setText(password);
            if(loggedIn) {
                gotToOpeningScreen();
            }
                return;
        }

        if (getIntent() != null && getIntent().getExtras() != null) {

            userName = getIntent().getExtras().getString("username");
            password = getIntent().getExtras().getString("password");

            if (!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(password)) {

                _emailText.setText(userName);
                _passwordText.setText(password);
                _loginButton.setEnabled(true);

                boolean issuccess = login(userName, password);
                if (issuccess) {
                    gotToOpeningScreen();
                } else {
                    Toast.makeText(getApplicationContext(), "Username or password is wrong"
                            , Toast.LENGTH_SHORT).show();
                }
            }
        }


        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean issuccess = login(_emailText.getText().toString(), _passwordText.getText().toString());
                if (issuccess) {
                    gotToOpeningScreen();
                } else {
                    Toast.makeText(getApplicationContext(), "Username or password is wrong"
                            , Toast.LENGTH_SHORT).show();
                }

            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void gotToOpeningScreen() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

       /* if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }*/

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}