package com.mypackage.test
import android.test.ActivityInstrumentationTestCase2
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import org.scalatest.junit.AssertionsForJUnit
sealed trait Foo

case class Bar(xs: List[String]) extends Foo
case class Qux(i: Int, d: Option[Double],l:List[Int],ls:List[Line]) extends Foo

case class Line(title:String)

object TestCirce {
  val foo: Foo = Qux(13, Some(14.0), List(1,2,3,8), List(Line("bazmeg"), Line("azzeg")))

}

class Tester() extends ActivityInstrumentationTestCase2[MainActivity]("com.mypackage.test", classOf[MainActivity]) with AssertionsForJUnit {
    def testOne(): Unit ={
      println("teszt one")
      import TestCirce._
      println (foo.asJson.noSpaces)
      println (decode[Foo](foo.asJson.spaces4))
    }

  def testTwo(): Unit ={
    println("teszt three")
  }
}
