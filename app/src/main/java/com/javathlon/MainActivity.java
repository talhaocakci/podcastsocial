package com.javathlon;

import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.javathlon.R;
import com.javathlon.download.DownloadReceiver;
import com.javathlon.download.DownloadTask;
import com.javathlon.fragments.FragmentHome;
import com.javathlon.fragments.LastBrowsedFragment;
import com.javathlon.fragments.LastNotesFragment;
import com.javathlon.fragments.LibraryFragment;
import com.javathlon.fragments.SignInActivity;
import com.javathlon.player.PlayerScreen;
import com.javathlon.rss.RssListPlayerActivity;


import java.util.Calendar;
import java.util.HashMap;


public class MainActivity extends BaseActivity {

    public static HashMap<Integer, DownloadTask> map = new HashMap<Integer, DownloadTask>();

    // Sol Slider için Yapýlmýþ özel layout android.support.v4 ün içinde
    private DrawerLayout mDrawerLayout;

    // Sol Slider Açýldýðýnda Görünecek ListView
    private ListView mDrawerList;

    // Navigation Drawer nesnesini ActionBar'da gösterir.
    private ActionBarDrawerToggle mDrawerToggle;

    // ActionBar'ýn titlesi dinamik olarak deðiþecek draweri açýp kapattýkça
    private String mTitle = "";

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "BuySubscriptionActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_for_drawer);



        // Content alanýna fragment yüklemek için
        FragmentManager fragmentManager = getFragmentManager();


        FragmentTransaction ft = fragmentManager.beginTransaction();

        FragmentHome fragmentHome = new FragmentHome();
        ft.add(R.id.content_frame, fragmentHome);
        ft.commit();

        mTitle = getResources().getString(R.string.topmenutitle);
        getActionBar().setTitle(mTitle);

        // Font path
        String fontPath = "Roboto-Medium.ttf";

        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerList = (ListView) findViewById(R.id.drawer_list);

        // iconu ve açýlýp kapandýðýnda görünecek texti veriyoruz.
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {

            // drawer kapatýldýðýnda tetiklenen method
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();

            }

            // drawer açýldýðýnda tetiklenen method
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(getResources().getString(R.string.navigationdrawertitle));
                invalidateOptionsMenu();
            }

        };

        // Açýlýp kapanmayý dinlemek için register
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Navigationdaki Drawer için listview adapteri
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),
                R.layout.drawer_list_item, getResources().getStringArray(R.array.menu));

        // adapteri listviewe set ediyoruz
        mDrawerList.setAdapter(adapter);

        // actionbar home butonunu aktif ediyoruz
        // getActionBar().setHomeButtonEnabled(true);

        // navigationu týklanabilir hale getiriyoruz
        getActionBar().setDisplayHomeAsUpEnabled(true);


        /**
         Birincil alarmı kur
         * */


        if (!CommonStaticClass.receivers.containsKey(CommonStaticClass.DAILY_RSSREFRESH_DOWNLOADITEMS)) {
            AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
            Intent i = new Intent("com.paperify.podmark.scheduledtask.RssUpdateTask");
            IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            DownloadReceiver dr = new DownloadReceiver();
            this.registerReceiver(dr, filter);

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY,15);
            cal.set(Calendar.MINUTE, 53);

         /*   PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, i, 0);
            alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 10000, pi);
            CommonStaticClass.receivers.put(CommonStaticClass.DAILY_RSSREFRESH_DOWNLOADITEMS, dr);*/
            /*****************************************************************/
        }


        // sol slider açýldýðýnda gelen listviewin týklama eventi
        mDrawerList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // itemleri arraya tekrar aldýk
                String[] menuItems = getResources().getStringArray(R.array.menu);

                // dinamik title yapmak için actionbarda týklananýn titlesi görünecek
                mTitle = menuItems[position];

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();

                if (position == 0) {
                    FragmentHome fragmentHome = new FragmentHome();
                    ft.replace(R.id.content_frame, fragmentHome).addToBackStack("tag");
                    ft.commit();
                } else if (position == 1) {
                    Intent i = new Intent(MainActivity.this, SignInActivity.class);
                    i.putExtra("intentionally",true);
                    startActivity(i);

                } else if (position == 2) {
                    LibraryFragment libraryFragment = new LibraryFragment();
                    ft.replace(R.id.content_frame, libraryFragment).addToBackStack("tag");
                    ft.commit();
                }
                else if (position == 3) {
                    Intent i = new Intent(MainActivity.this, PlayerScreen.class);
                    startActivity(i);

                }

                else if (position == 4) {
                    LastBrowsedFragment lastBrowsedFragment = new LastBrowsedFragment();
                    ft.replace(R.id.content_frame, lastBrowsedFragment).addToBackStack("tag");
                    ft.commit();
                }
                else if (position == 5) {
                    LastNotesFragment lastNotesFragment = new LastNotesFragment();
                    ft.replace(R.id.content_frame, lastNotesFragment).addToBackStack("tag");
                    ft.commit();
                }
                else if (position == 6) {
                    Intent i = new Intent(MainActivity.this, BuySubscriptionActivity.class);
                    startActivity(i);
                }
                 /*else if (position == 3) {
                    SearchFragment searchFragment = new SearchFragment();
                    ft.replace(R.id.content_frame, searchFragment).addToBackStack("tag");
                    ft.commit();
                } else if (position == 4) {
                    LastBrowsedFragment lastBrowsedFragment = new LastBrowsedFragment();
                    ft.replace(R.id.content_frame, lastBrowsedFragment).addToBackStack("tag");
                    ft.commit();
                } else if (position == 5) {
                    LastNotesFragment lastNotesFragment = new LastNotesFragment();
                    ft.replace(R.id.content_frame, lastNotesFragment).addToBackStack("tag");
                    ft.commit();
                }*/

                // draweri kapat
                mDrawerLayout.closeDrawer(mDrawerList);

            }
        });

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }


        /* GCM messagindan gelen notification main activityi açar. o da rss list fragmentını koyar*/
        Bundle b = getIntent().getExtras();
        if(b != null && b.getString("action")!= null)
        {
            if(b.getString("action").equals("rsslist")) {
                RssListPlayerActivity fragmentRss = new RssListPlayerActivity();
                fragmentRss.setArguments(b);
                ft.replace(R.id.content_frame, fragmentRss);

            }
        }



    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //draweri sadece swipe ederek açma yerine sol tepedeki butona basarak açmak için
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        // navigationDrawer açýldýðýnda ayarlarý gizlemek için
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
