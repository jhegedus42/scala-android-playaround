package scala.com.mypackage.test

import android.support.test.runner.AndroidJUnit4
import android.test.InstrumentationTestCase
import org.junit.Assert._
import org.junit.runner.RunWith
import org.junit.{Before, Test}
import org.scalatest.junit.AssertionsForJUnit

import scala.collection.mutable.ListBuffer


@RunWith (classOf[AndroidJUnit4] )
class JUnitScalaTest extends InstrumentationTestCase with AssertionsForJUnit {
  
  var sb: StringBuilder = _
  var lb: ListBuffer[String] = _
  
  @Before def initialize() {
    sb = new StringBuilder("ScalaTest is ")
    lb = new ListBuffer[String]
  }
  
  @Test def verifyEasy() { // Uses JUnit-style assertions
    sb.append("easy!")
    assertEquals("ScalaTest is easy!", sb.toString)
    assertTrue(lb.isEmpty)
    lb += "sweet"
    try {
      "verbose".charAt(-1)
      fail()
    }
    catch {
      case e: StringIndexOutOfBoundsException => // Expected
    }
  }
  
  @Test def verifyFun() { // Uses ScalaTest assertions
    sb.append("fun!")
    assert(sb.toString === "ScalaTest is fun!")
    assert(lb.isEmpty)
    lb += "sweeter"
    intercept[StringIndexOutOfBoundsException] {
      "concise".charAt(-1)
    }
  }
}