package com.test;

import android.test.InstrumentationTestCase;

import com.javathlon.download.PodcstModernUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by ocakcit on 12/11/15.
 */
public class TextClass extends InstrumentationTestCase {
    private static final String TAG = "FetchRESTResponseTaskTests";
    private static final String BOUNDS = "30.371737637334984,120.33188021826174,30.23568452886941,119.97825778173831";

    private static boolean called;

    protected void setUp() throws Exception {
        super.setUp();
        called = false;
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public final void testSuccessfulFetch() throws Throwable {
        // create  a signal to let us know when our task is done.
        final CountDownLatch signal = new CountDownLatch(1);

        // Execute the async task on the UI thread! THIS IS KEY!
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {

                PodcstModernUtil.getPodcastsByApplication(3);
            }
        });

	    /* The testing thread will wait here until the UI thread releases it
	     * above with the countDown() or 30 seconds passes and it times out.
	     */
        signal.await(10, TimeUnit.SECONDS);
        assertTrue(called);
    }
}
