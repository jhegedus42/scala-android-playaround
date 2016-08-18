package mypackage.test.testInsert

//import scala.slick.driver.SQLiteDriver.simple._
import _root_.slick.driver.SQLiteDriver.backend.DatabaseDef
import android.test.ActivityInstrumentationTestCase2
import com.mypackage.test.{MainActivity, Person}
import mypackage.test.testInsert
import org.scalatest.junit.AssertionsForJUnit
import slick.driver.SQLiteDriver.api._
import slick.lifted.{Query => _, Rep => _, TableQuery => _, Tag => _}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}


case class Person( name: String, street: String, next_id:Int, id: Int = 0)

final class PersonTable(tag: Tag) extends Table[Person](tag, "PERSONS") {
  def id: Rep[Int] = column[Int]("PER_ID", O.PrimaryKey, O.AutoInc)
  def next_id: Rep[Int] = column[Int]("NEXT_ID")
  def name: Rep[String] = column[String]("PER_NAME")
  def street: Rep[String] = column[String]("STREET")

  def *  = (name, street,next_id,id) <> (Person.tupled, Person.unapply)
}

object PersonHelper{

  val persons=TableQuery[PersonTable]

  def insertAfter(target:Person,inserted:Person)={
    val insert  =persons returning persons.map(_.id)+=(inserted.copy(next_id = target.next_id))
    // insert inpppserted (where inserted.next_id=target.next_id)
    // update target.next_id => inserted.id

    def update (inserted_id:Int)= persons.filter(_.id===target.id).map(_.next_id).update(inserted_id)
    insert.flatMap { id=>update(id) }
  }

  val names = persons.map(_.name).result


  def nameAction: DatabaseDef =>Future[Seq[String]] = db => db.run(names)
  def map_map_println: Future[Seq[String]] => Future[Unit] = _.map(_.map(println))
  def printNames(db: DatabaseDef )= Await.result(map_map_println(nameAction(db)),Duration.Inf)

}


class TestInsertAfter extends ActivityInstrumentationTestCase2[MainActivity]("com.mypackage.test", classOf[MainActivity]) with AssertionsForJUnit {
  import PersonHelper._
  lazy val fn=this.getInstrumentation().getContext().getFilesDir + "/slick-sandbox.txt"

  def exec[T](db:Database, action: DBIO[T]): T = Await.result(db.run(action), Duration.Inf)

  val p1 = testInsert.Person("Bela",   "Vaci utca 42",      -1)     //  1
  val p2 = testInsert.Person("Geza",   "Lemberg utca 155",   1)  //  2
  val p3 = testInsert.Person("Jani",   "Rock Road 12",       2)      //  3
  val p4 = testInsert.Person("Gerzson","Gisonenweg 2",      -1)

  //val db = Database.forConfig("h2mem1")
  lazy val db = Database.forURL("jdbc:sqlite:" + fn, driver = "org.sqldroid.SQLDroidDriver")

  def testInsertAfter {

    try {

      val createSchema: DBIO[Unit] = persons.schema.create
      val addTestPerson1 = persons += p1
      val setupAction: DBIO[Unit] = DBIO.seq(createSchema, addTestPerson1)

      exec(db, setupAction)

      printNames(db)

      val addTestPerson2 = persons += p2
      val addTestPerson3 = persons += p3

      exec(db, addTestPerson2)
      exec(db, addTestPerson3)

      printNames(db)

      val ia = insertAfter(p2, p4)

      exec(db, ia)

      printNames(db)

    } finally db.close
  }
}
