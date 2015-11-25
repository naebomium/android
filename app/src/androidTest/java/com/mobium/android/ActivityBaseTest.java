package com.mobium.android;

import android.test.ActivityInstrumentationTestCase2;

import com.mobium.reference.activity.MainDashboardActivity;

/**
 *  on 24.08.15.
 */
public class ActivityBaseTest extends ActivityInstrumentationTestCase2<MainDashboardActivity> {
    private MainDashboardActivity mFirstTestActivity;


    public ActivityBaseTest(Class<MainDashboardActivity> activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mFirstTestActivity = getActivity();
        testPreconditions();
    }


    public void testPreconditions() {
        assertNotNull("mFirstTestActivity is null", mFirstTestActivity);
    }
}
