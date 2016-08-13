package com.mypackage.test

import android.test.ActivityInstrumentationTestCase2
import android.test.suitebuilder.annotation.SmallTest
import com.mypackage.test.MainActivity

class ScalaTestTest2() extends ActivityInstrumentationTestCase2[MainActivity]("com.mypackage.test", classOf[MainActivity])
{
 // print("start ! 42")
  @SmallTest def test2()
  {
   //  assertEquals(true, true)
    print("ykis")
    System.out.println("fasz " + this.getInstrumentation.getContext)
  }
  
  @SmallTest def test1()
  {
    //assertEquals(true, true)
    print("ykis kasi")

  }
}
