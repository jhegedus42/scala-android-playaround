package com.mypackage.test
import android.test.ActivityInstrumentationTestCase2
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import org.scalatest.junit.AssertionsForJUnit
sealed trait Foo

case class Bar(xs: List[String]) extends Foo
case class Qux(i: Int, d: Option[Double]) extends Foo


object TestCirce {
  val foo: Foo = Qux(13, Some(14.0))

}
class Tester() extends ActivityInstrumentationTestCase2[MainActivity]("com.mypackage.test", classOf[MainActivity]) with AssertionsForJUnit {
    def testOne(): Unit ={
      println("teszt one")
    }
}