package com.mypackage.test;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import android.test.InstrumentationTestCase;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class TestTest extends InstrumentationTestCase
{

    @Before
    public void createLogHistory() {
        System.out.println("ebcsont before");
    }

    @Test
    public void test1(){
        Context c=InstrumentationRegistry.getTargetContext();
        System.out.println("zero");

        System.out.println(c);
        Assert.assertEquals(1,2);
        System.out.println("one");

        System.out.println("two");

    }
}