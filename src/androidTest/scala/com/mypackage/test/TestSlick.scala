package com.mypackage.test


import android.test.ActivityInstrumentationTestCase2
import android.test.suitebuilder.annotation.SmallTest
import slick.lifted.Tag
import slick.model.Table

import scala.slick.driver

class TestSlick() extends ActivityInstrumentationTestCase2[MainActivity]("com.mypackage.test", classOf[MainActivity])
{
  @SmallTest def test2()
  {
    print("ykis")
    System.out.println("fasz " + this.getInstrumentation.getContext)
  }

  @SmallTest def test1()
  {
    print("ykis kasi")

  }





}
