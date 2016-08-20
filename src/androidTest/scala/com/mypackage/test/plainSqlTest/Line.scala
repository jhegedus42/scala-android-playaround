package mypackage.test.plainSqlTest

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

import scala.collection.immutable.HashMap

case class Line(short: String, full:String, next_id:Int , id:Int =0)
object LineHelper{
  def orderedList(l: List[Line]): List[Line] = {
    def findFirst: List[Line] => Line = lp => {
      def prevMap: (List[Line]) => Map[Int, Line] = (lp) => {

        lp.filter(_.next_id > 0).foldLeft(HashMap.empty[Int, Line])((hm, p: Line) => hm.+((p.next_id, p)))

      }
      val m = prevMap(lp)
      val guess = lp.head
      def go(l: Set[Line], g: Line): Line = {
        m.get(g.id) match {
          case Some(g2) => go(l.-(g), g2)
          case None => g
        }
      }
      go(lp.toSet, guess)
    }
    def getPersonWithID: (List[Line], Int) => Option[Line] = (ps, id) => ps.filter(_.id == id).headOption
    val f = findFirst(l)
    val res = scala.collection.mutable.ListBuffer[Line]()
    var next_id = f.next_id
    res += f
    while (next_id > 0) {
      val np = getPersonWithID(l, next_id).head
      res += np
      next_id = np.next_id
    }
    res.toList
  }
  // Contacts table name

  val TABLE_LINES = "lines"

  // Lines Table Columns names
  val KEY_ID = "id"
  val KEY_NAME = "name"
  val KEY_SH_ADDR = "line_address"
  val KEY_NEXTID = "next_id"

  def createTable(db: SQLiteDatabase)={
    val CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_LINES + "(" +
      KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
      KEY_NAME + " TEXT," +
      KEY_SH_ADDR + " TEXT," +
      KEY_NEXTID+ " INTEGER" +
      ")"
    db.execSQL(CREATE_CONTACTS_TABLE)
  }

  def addLine(line : Line ,db: SQLiteDatabase) {

    val values = new ContentValues()
    values.put(KEY_NAME, line.short)
    values.put(KEY_SH_ADDR, line.full)
    values.put(KEY_NEXTID, new Integer(line.next_id))

    db.insert(TABLE_LINES, null, values)
  }

  def getLine(id: Int,db: SQLiteDatabase): Line = {
    val cursor: Cursor = db.query(TABLE_LINES, Array[String](KEY_ID, KEY_NAME, KEY_SH_ADDR), KEY_ID + "=?", Array[String](String.valueOf(id)), null, null, null, null)
    if (cursor != null) cursor.moveToFirst
    val contact = Line(cursor.getString(1), cursor.getString(2), cursor.getString(3).toInt, cursor.getString(0).toInt)
    contact
  }

  def getAllLines(db: SQLiteDatabase): List[Line] = {
    import scala.collection.mutable.ListBuffer
    val lineList: ListBuffer[Line] = ListBuffer[Line]()
    val selectQuery: String = "SELECT  * FROM " + TABLE_LINES

    val cursor: Cursor = db.rawQuery(selectQuery, null)
    if (cursor.moveToFirst) do {
      val line=Line(cursor.getString(1), cursor.getString(2), cursor.getString(3).toInt, cursor.getString(0).toInt)
      lineList += line
    } while (cursor.moveToNext)
    lineList.toList
  }

  def getLinesCount(db: SQLiteDatabase): Int = {
    val countQuery: String = "SELECT  * FROM " + TABLE_LINES
    val cursor: Cursor = db.rawQuery(countQuery, null)
    cursor.close()
    // return count
    cursor.getCount
  }

  def updateLine(line: Line,db: SQLiteDatabase): Int = {
    val values: ContentValues = new ContentValues
    values.put(KEY_NAME, line.short)
    values.put(KEY_SH_ADDR, line.full)
    // updating row
    db.update(TABLE_LINES, values, KEY_ID + " = ?", Array[String](String.valueOf(line.id)))
  }

  def deleteLine(line: Line,db: SQLiteDatabase) {
    db.delete(TABLE_LINES, KEY_ID + " = ?", Array[String](String.valueOf(line.id)))
  }

}