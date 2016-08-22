package mypackage.test.plainSqlTest

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

import scala.collection.immutable.HashMap

case class Line(title: String, text:String, next_id:Int = -1, id:Int = -1)
object LineOrder {
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
}
trait LineHelper{
  this: OpenDB =>

  // Contacts table name

  val TABLE_LINES = "lines"

  // Lines Table Columns names
  val KEY_ID = "id"
  val KEY_TITLE = "title"
  val KEY_TEXT = "line_address"
  val KEY_NEXTID = "next_id"

  def createTable={
    val CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_LINES + "(" +
      KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
      KEY_TITLE + " TEXT," +
      KEY_TEXT + " TEXT," +
      KEY_NEXTID+ " INTEGER" +
      ")"
    db.execSQL(CREATE_CONTACTS_TABLE)
  }

  def addLine(line : Line) :Int={
    assert(line.next_id!=0)
    val values = new ContentValues()
    values.put(KEY_TITLE, line.title)
    values.put(KEY_TEXT, line.text)
    values.put(KEY_NEXTID, new Integer(line.next_id))

    db.insert(TABLE_LINES, null, values).toInt
  }

  def getLine(id: Int,db: SQLiteDatabase): Line = {
    val cursor: Cursor = db.query(TABLE_LINES, Array[String](KEY_ID, KEY_TITLE, KEY_TEXT), KEY_ID + "=?", Array[String](String.valueOf(id)), null, null, null, null)
    if (cursor != null) cursor.moveToFirst
    val contact = Line(cursor.getString(1), cursor.getString(2), cursor.getString(3).toInt, cursor.getString(0).toInt)
    contact
  }

  def getAllLines: List[Line] = {
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

  def getLinesCount: Int = {
    val countQuery: String = "SELECT  * FROM " + TABLE_LINES
    val cursor: Cursor = db.rawQuery(countQuery, null)
    cursor.close()
    // return count
    cursor.getCount
  }

  def updateLine(line: Line): Int = {
    val values: ContentValues = new ContentValues
    values.put(KEY_TITLE, line.title)
    values.put(KEY_TEXT, line.text)
    values.put(KEY_NEXTID, new Integer(line.next_id))
    // updating row
    db.update(TABLE_LINES, values, KEY_ID + " = ?", Array[String](String.valueOf(line.id)))
  }

  def deleteLine(line: Line) = {
    db.delete(TABLE_LINES, KEY_ID + " = ?", Array[String](String.valueOf(line.id)))
    //TODO keep order
  }


  def insertAfter(target:Line,inserted:Line)={
    val inserted_id  = addLine(inserted.copy(next_id = target.next_id))
    updateLine(target.copy(next_id = inserted_id))
  }

  def swapLines(a:Line,b:Line)={

  }

}