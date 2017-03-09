/*
 * Copyright 2012 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.javathlon;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.javathlon.adapters.BuyableSubscriptionAdapter;
import com.javathlon.adapters.GroupAndChildAdapter;
import com.javathlon.apiclient.ApiClient;
import com.javathlon.apiclient.api.PurchaseresourceApi;
import com.javathlon.apiclient.api.SubscriptionitemresourceApi;
import com.javathlon.apiclient.model.Podcast;
import com.javathlon.apiclient.model.PurchaseDTO;
import com.javathlon.apiclient.model.SubscriptionItem;
import com.javathlon.db.DBAccessor;
import com.javathlon.inapp_purchase.IabBroadcastReceiver;
import com.javathlon.inapp_purchase.IabHelper;
import com.javathlon.inapp_purchase.IabResult;
import com.javathlon.inapp_purchase.Inventory;
import com.javathlon.inapp_purchase.Purchase;
import com.javathlon.model.podcastmodern.Subscription;

import org.joda.time.LocalDate;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BuySubscriptionActivity extends BaseActivity implements IabBroadcastReceiver.IabBroadcastListener,
        OnClickListener {
    // Debug tag, for logging
    static final String TAG = "Javathlon";

    private TextView descriptionTextView;

    private ListView itemsList;

    // Does the user have the premium upgrade?
    boolean mIsPremium = false;

    // Does the user have an active subscription to the infinite gas plan?
    boolean mSubscribedToInfiniteGas = false;

    // Will the subscription auto-renew?
    boolean mAutoRenewEnabled = false;

    static final String FULL_MEMBERSHIP_MONTHLY = "full_membership";
    static final String JAVA_CORE_MONTHLY = "java_core_monthly";

    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10001;

    // The helper object
    IabHelper mHelper;

    boolean isServiceRegistered = false;

    // Provides purchase notification while this app is running
    IabBroadcastReceiver mBroadcastReceiver;

    GroupAndChildAdapter adapter;

    List<GroupAndChildAdapter.GroupItem> items = new ArrayList<GroupAndChildAdapter.GroupItem>();

    List<SubscriptionItem> subscriptionItems = new ArrayList<>();

    private SubscriptionItem selectedSubscription;

    private DBAccessor dbAccessor;

    AnimatedExpandableListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.buy_subscription);

        this.getSupportActionBar().hide();

        adapter = new GroupAndChildAdapter(this);

        adapter.setData(items);

        listView = (AnimatedExpandableListView) findViewById(R.id.listView);
        listView.setDividerHeight(0);
        listView.setAdapter(adapter);

        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.
                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                } else {
                    listView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }

        });

        // Set indicator (arrow) to the right
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                50, r.getDisplayMetrics());
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            listView.setIndicatorBounds(width - px, width);
        } else {
            listView.setIndicatorBoundsRelative(width - px, width);
        }

        if (dbAccessor == null) {
            dbAccessor = new DBAccessor(this.getApplicationContext());
            dbAccessor.open();
        }

        loadData();


        /* base64EncodedPublicKey should be YOUR APPLICATION'S PUBLIC KEY
         * (that you got from the Google Play developer console). This is not your
         * developer public key, it's the *app-specific* public key.
         *
         * Instead of just storing the entire literal string here embedded in the
         * program,  construct the key at runtime from pieces or
         * use bit manipulation (for example, XOR with some other string) to hide
         * the actual key.  The key itself is not secret information, but we don't
         * want to make it easy for an attacker to replace the public key with one
         * of their own and then fake messages from the server.
         */
        String base64EncodedPublicKey = getApplicationContext().getResources().getString(R.string.appKey);

        // Some sanity checks to see if the developer (that's you!) really followed the
        // instructions to run this sample (don't put these checks on your app!)
        if (base64EncodedPublicKey.contains("CONSTRUCT_YOUR")) {
            throw new RuntimeException("Please put your app's public key in BuySubscriptionActivity.java. See README.");
        }
        if (getPackageName().startsWith("com.example")) {
            throw new RuntimeException("Please change the sample's package name! See README.");
        }

        // Create the helper, passing it our context and the public key to verify signatures with
        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(true);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                isServiceRegistered = true;
                // Important: Dynamically register for broadcast messages about updated purchases.
                // We register the receiver here instead of as a <receiver> in the Manifest
                // because we always call getPurchases() at startup, so therefore we can ignore
                // any broadcasts sent while the app isn't running.
                // Note: registering this listener in an Activity is a bad idea, but is done here
                // because this is a SAMPLE. Regardless, the receiver must be registered after
                // IabHelper is setup, but before first call to getPurchases().
                mBroadcastReceiver = new IabBroadcastReceiver(BuySubscriptionActivity.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                registerReceiver(mBroadcastReceiver, broadcastFilter);

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error querying inventory. Another async operation in progress.");
                }
            }
        });
    }

    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // Do we have the premium upgrade?
            Purchase premiumPurchase = inventory.getPurchase(FULL_MEMBERSHIP_MONTHLY);
            mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
            Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));

            // First find out which subscription is auto renewing
            Purchase fullMembership = inventory.getPurchase(FULL_MEMBERSHIP_MONTHLY);
            Purchase javaCore = inventory.getPurchase(JAVA_CORE_MONTHLY);
            if (fullMembership != null && fullMembership.isAutoRenewing()) {
                mAutoRenewEnabled = true;
            } else if (javaCore != null && javaCore.isAutoRenewing()) {
                mAutoRenewEnabled = true;
            } else {
                mAutoRenewEnabled = false;
            }

            // The user is subscribed if either subscription exists, even if neither is auto
            // renewing
            mSubscribedToInfiniteGas = (fullMembership != null && verifyDeveloperPayload(fullMembership))
                    || (javaCore != null && verifyDeveloperPayload(javaCore));

            Purchase gasPurchase = inventory.getPurchase(FULL_MEMBERSHIP_MONTHLY);
            if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {

                try {
                    mHelper.consumeAsync(inventory.getPurchase(FULL_MEMBERSHIP_MONTHLY), mConsumeFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {

                }
                return;
            }

            updateUi();
            setWaitScreen(false);

        }
    };

    @Override
    public void receivedBroadcast() {
        // Received a broadcast notification that the inventory of items has changed
        Log.d(TAG, "Received broadcast notification. Querying inventory.");
        try {
            mHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error querying inventory. Another async operation in progress.");
        }
    }


    public void initiatePurchasingItem(int position) {
        setWaitScreen(true);

        selectedSubscription = subscriptionItems.get(position);

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";

        try {
            mHelper.launchPurchaseFlow(this, selectedSubscription.getSkuName(), RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
            setWaitScreen(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    /**
     * Verifies the developer payload of a purchase.
     */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }


    private void savePurchasedItem(SubscriptionItem subscription) {

        for(Podcast p : subscription.getPodcasts()) {
            dbAccessor.savePurchasedPodcastItem(p.getId(), new Date());
        }

    }

    private void savePurchaseToserver(final PurchaseDTO purchaseDTO) {
        ApiClient apiClient = ApiClient.getApiClient(getApplicationContext());
        PurchaseresourceApi api = apiClient.createService(PurchaseresourceApi.class);
        api.createPurchaseUsingPOST(purchaseDTO).enqueue(new Callback<PurchaseDTO>() {

            @Override
            public void onResponse(Call<PurchaseDTO> call, Response<PurchaseDTO> response) {
                if (response.isSuccessful()) {
                    complain("Purchase is successful, thank you!");
                } else {
                    if(response.code() == 401) {
                        login();
                        savePurchaseToserver(purchaseDTO);
                    }

                    complain("Purchase is successful,  but operation failed!");
                }
            }

            @Override
            public void onFailure(Call<PurchaseDTO> call, Throwable t) {
                complain("Purchase failed!");
            }
        });
    }

    private void createPurchase(Double price, Long subscriptionId) {
        ApiClient apiClient = ApiClient.getApiClient(getApplicationContext());

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String subscriberID = preferences.getString(QuickstartPreferences.SUBSCRIBER_ID, "");

        savePurchasedItem(selectedSubscription);

        PurchaseDTO purchaseDTO = new PurchaseDTO();
        purchaseDTO.setPurchaseDate(LocalDate.now());
        purchaseDTO.setChannel("android-app");
        purchaseDTO.setPrice(price);
        purchaseDTO.setItemId(subscriptionId);
        purchaseDTO.setApplicationId(apiClient.appId);
        purchaseDTO.setSubscriberId(Long.valueOf(subscriberID));
        purchaseDTO.setPaymentType("google wallet");

        savePurchaseToserver(purchaseDTO);

    }

    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;


            if (result.isFailure() || purchase == null) {

                // item already owned
                if(result.getResponse() == 7) {
                    complain("You already bought this item, thank you!");
                } else {
                    //complain("Error purchasing: " + result);
                }
                setWaitScreen(false);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                setWaitScreen(false);
                return;
            }

            try {

                Double price = mHelper.getPricesDev(getPackageName(), selectedSubscription.getSkuName());

                createPurchase(price, selectedSubscription.getId());

            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
            } catch (IabHelper.IabAsyncInProgressException e) {
                complain("Error consuming gas. Another async operation in progress.");
                setWaitScreen(false);
                return;
            }

        }
    };

    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isSuccess()) {
                Log.d(TAG, "Consumption successful. Provisioning.");
                saveData();

            } else {
                complain("Error while consuming: " + result);
            }
            updateUi();
            setWaitScreen(false);
            Log.d(TAG, "End consumption flow.");
        }
    };


    // We're being destroyed. It's important to dispose of the helper here!
    @Override
    public void onDestroy() {
        super.onDestroy();

        // very important:
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }

        // very important:
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            if (isServiceRegistered) {
                mHelper.disposeWhenFinished();
                mHelper = null;
            }
        }
    }

    public void updateUi() {


    }

    // Enables or disables the "please wait" screen.
    void setWaitScreen(boolean set) {
        //findViewById(R.id.screen_main).setVisibility(set ? View.GONE : View.VISIBLE);
        //findViewById(R.id.screen_wait).setVisibility(set ? View.VISIBLE : View.GONE);
    }

    void complain(String message) {
        alert(message);
    }

    void alert(String message) {
        Builder bld = new Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        bld.create().show();
    }

    void saveData() {

        /*
         * WARNING: on a real application, we recommend you save data in a secure way to
         * prevent tampering. For simplicity in this sample, we simply store the data using a
         * SharedPreferences.
         */

        SharedPreferences.Editor spe = getPreferences(MODE_PRIVATE).edit();
        // spe.putInt("tank", mTank);
        spe.apply();
        // Log.d(TAG, "Saved data: tank = " + String.valueOf(mTank));
    }

    void loadData() {

        SubscriptionitemresourceApi api = ApiClient.getApiClient(getApplicationContext()).createService(SubscriptionitemresourceApi.class);

        api.getSubscriptionItemsByPodcast(ApiClient.getApiClient(getApplicationContext()).appId).enqueue(new Callback<List<SubscriptionItem>>() {

            @Override
            public void onResponse(Call<List<SubscriptionItem>> call, Response<List<SubscriptionItem>> response) {
                if (response.isSuccessful()) {

                    for(SubscriptionItem subscriptionItem : response.body()) {
                        GroupAndChildAdapter.GroupItem item = new GroupAndChildAdapter.GroupItem();
                        item.title = subscriptionItem.getName();
                        item.subtitle = subscriptionItem.getDescription();
                        item.imageUrl = "http://www.centerforfinancialinclusion.org/storage/images/Logo/SC_logo_clear.png";
                        item.items = new ArrayList<GroupAndChildAdapter.ChildItem>();
                        for(Podcast podcast : subscriptionItem.getPodcasts()) {
                            GroupAndChildAdapter.ChildItem child;
                            child = new GroupAndChildAdapter.ChildItem();
                            child.title = podcast.getName();
                            child.imageUrl = "http://www.centerforfinancialinclusion.org/storage/images/Logo/SC_logo_clear.png";
                            item.items.add(child);
                        }
                        item.skuName = subscriptionItem.getSkuName();
                        items.add(item);
                        subscriptionItems.add(subscriptionItem);
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    boolean reauthenticate = anyFailedRequest(response);
                    if (reauthenticate) {
                        loadData();
                    }
                }

            }

            @Override
            public void onFailure(Call<List<SubscriptionItem>> call, Throwable t) {
                Log.d("alamadik", "itemlari alamadik");
            }
        });


        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        //mTank = sp.getInt("tank", 2);
        // Log.d(TAG, "Loaded data: tank = " + String.valueOf(mTank));
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
