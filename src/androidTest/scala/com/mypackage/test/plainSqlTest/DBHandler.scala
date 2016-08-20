package mypackage.test.plainSqlTest

import android.content.{ContentValues, Context}
import android.database.Cursor
import android.database.sqlite.{SQLiteDatabase, SQLiteOpenHelper}


object DBHandler{
  val DATABASE_VERSION = 1
}



class DBHandler(context: Context,DATABASE_NAME:String)
  extends SQLiteOpenHelper(context, DATABASE_NAME, null, DBHandler.DATABASE_VERSION){




  override def onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int): Unit = {throw new Exception("not implemented")}

  override def onCreate(db: SQLiteDatabase): Unit = {
    println("creating table")
    LineHelper.createTable(db)
  }


}
