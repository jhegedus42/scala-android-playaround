package mypackage.test.plainSqlTest

import java.io.File

import android.database.sqlite.SQLiteDatabase
import android.test.ActivityInstrumentationTestCase2
import android.util.Log
import com.mypackage.test.MainActivity
import org.scalatest.junit.AssertionsForJUnit
import slick.jdbc.JdbcBackend.DatabaseDef


class PlainSQLTest() extends ActivityInstrumentationTestCase2[MainActivity]("com.mypackage.test", classOf[MainActivity]) with AssertionsForJUnit {
  val  DATABASE_NAME = "linesInfo"
  override def setUp(): Unit = {
    println("ebcsont beforr")
    new File("/data/data/com.mypackage.test/databases/linesInfo").delete()
  }

  def assertLineOrder(lh: LineHelper, l:List[Int]): Unit ={
    val res = lh.getAllLines

    println("Persons  : ")

    println(res)
    val l1 = res
    val ol = LineOrder.orderedList(res)
    junit.framework.Assert.assertEquals(ol.map(_.id), l)
  }

  def testDB() {
    val dBHandler: DBHandler = new DBHandler(this.getInstrumentation.getContext, DATABASE_NAME)
    val sql_db: SQLiteDatabase =dBHandler.getWritableDatabase
    val lh= new LineHelper with OpenDB {val db:SQLiteDatabase=sql_db}

    Log.d("Insert: ", "Inserting ..")
    lh.addLine(Line("short1", " fu ll1",2))
    lh.addLine(Line("short2", " fu ll2",3))
    lh.addLine(Line("short3", " fu ll3",-1))

    Log.d("Reading: ", "Reading all lines..")
    val lines = lh.getAllLines
    for (line <- lines) {
      val log: String = "Id: " + line.id + " ,Name: " + line.title + " ,Address: " + line.text+", Next id:" +line.next_id
      Log.d("Line: : ", log)
    }
    val l1=lines.filter(_.id==1).head
    val l2=lines.filter(_.id==2).head

    assertLineOrder(lh,List(1,2,3))

    lh.insertAfter(l1,Line("short4","full4"))
    assertLineOrder(lh,List(1,4,2,3))

    lh.insertAfter(l2,Line("short5","full5"))
    assertLineOrder(lh,List(1,4,2,5,3))

    lh.db.close()
  }

}