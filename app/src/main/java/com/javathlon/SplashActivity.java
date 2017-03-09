package com.javathlon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Window;

public class SplashActivity extends Activity {
    protected boolean _active = true;
    protected int _splashTime = 3000; // time to display the splash screen in ms


    int i = 0;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            showFinalAlert(getResources().getText(R.string.sdcard_readonly));
            return;
        }
        if (status.equals(Environment.MEDIA_SHARED)) {
            showFinalAlert(getResources().getText(R.string.sdcard_shared));
            return;
        }
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            showFinalAlert(getResources().getText(R.string.no_sdcard));
            return;
        }
        setContentView(R.layout.splash);


        // loading=(TextView)findViewById(R.id.txtLoading);

        final SplashActivity sPlashScreen = this;

        final Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (_active && waited < _splashTime) {
                        sleep(_splashTime / 100);
                        if (_active) {
                            waited += _splashTime / 100;
                            i++;
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                }
                            });
                        }
                    }
                } catch (final InterruptedException e) {
                    // do nothing
                } finally {

					/*
                     * This Thread only For progressBar
					 */
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub

                            // loading.setVisibility(View.INVISIBLE);

                            final Intent intent = new Intent();
                            intent.setClass(sPlashScreen, MainActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    });

                }
            }
        };

        splashTread.start();

    }

    private void showFinalAlert(CharSequence message) {
        new AlertDialog.Builder(SplashActivity.this)
                .setTitle(getResources().getText(R.string.alert_title_failure))
                .setMessage(message)
                .setPositiveButton(
                        R.string.alert_ok_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                finish();
                            }
                        })
                .setCancelable(false)
                .show();
    }
}