package com.test;

import android.test.InstrumentationTestCase;

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


}
