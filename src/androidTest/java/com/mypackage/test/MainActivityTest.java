package com.mypackage.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.mypackage.test.MainActivityTest \
 * com.mypackage.test.tests/android.test.InstrumentationTestRunner
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public MainActivityTest() {
        super("com.mypackage.test", MainActivity.class);
    }

    @SmallTest
    public void test2()
    {
        assertEquals(true,true);
        System.out.println("fasz "+this.getInstrumentation().getContext());
    }

    @SmallTest
    public void test1()
    {
        assertEquals(true,true);
    }


}
