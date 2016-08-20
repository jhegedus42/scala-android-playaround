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

  def assertLineOrder(db: SQLiteDatabase, l:List[Int]): Unit ={
    val res = LineHelper.getAllLines(db)

    println("Persons  : ")

    println(res)
    val l1 = res
    val ol = LineHelper.orderedList(res)
    junit.framework.Assert.assertEquals(ol.map(_.id), l)
  }

  def testDB() {
    val dBHandler: DBHandler = new DBHandler(this.getInstrumentation.getContext,DATABASE_NAME)
    val db: SQLiteDatabase =dBHandler.getWritableDatabase

    Log.d("Insert: ", "Inserting ..")
    LineHelper.addLine(Line("short1", " fu ll1",2),db)
    LineHelper.addLine(Line("short2", " fu ll2",3),db)
    LineHelper.addLine(Line("short3", " fu ll3",-1),db)

    Log.d("Reading: ", "Reading all lines..")
    val lines = LineHelper.getAllLines(db)
    for (line <- lines) {
      val log: String = "Id: " + line.id + " ,Name: " + line.title + " ,Address: " + line.text+", Next id:" +line.next_id
      Log.d("Line: : ", log)
    }
    assertLineOrder(db,List(1,2,3))
    db.close()
  }

}