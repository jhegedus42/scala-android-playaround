package com.mypackage.test
//import scala.slick.driver.SQLiteDriver.simple._
import java.io.File

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import slick.driver.SQLiteDriver.api._
import android.test.{ActivityInstrumentationTestCase2, InstrumentationTestCase}
import org.junit.Test
import slick.lifted.{Query => _, Rep => _, TableQuery => _, Tag => _, _}

import scala.collection.immutable.HashMap
import scala.concurrent.Future
import scala.concurrent.duration.Duration


object Helper {
  def exec[T](db:Database, action: DBIO[T]): T = Await.result(db.run(action), Duration.Inf)
}

import Helper._

//
//object Person{
//  def findFirst : List[Person]=>Person = {
//
//    ???
//  }
////  def prevMap : (List[Person], Database) => Map[Int,Person] = (lp,db) => {
////    def next:Person =>Int = p => {
////            val q:Query[Rep[Int], Int, Seq] = p.next_Person.map(_.id)
////            val r=Await.result(db.run(q.result), Duration.Inf)
////            r.head
////          }
////        lp.foldLeft(HashMap.empty[Int,Person])((hm,p)=> hm.+((next(p),p)))
////  }
//
//}



case class Person(id: Int = 0, name: String, street: String, next_id:Int)

final class PersonTable(tag: Tag)
  extends Table[Person](tag, "PERSONS") {
  // This is the primary key column:
  def id: Rep[Int] = column[Int]("PER_ID", O.PrimaryKey)
  def next_id: Rep[Int] = column[Int]("NEXT_ID")
  def name: Rep[String] = column[String]("PER_NAME")
  def street: Rep[String] = column[String]("STREET")
 // def next_Person: ForeignKeyQuery[Person, (Int, String, String, Int)] = foreignKey("SUP_FK", next_id, TableQuery[Person])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
 // def isFirst: Rep[Boolean] =column[Boolean]("IS_FIRST")

  // Every table needs a * projection with the same type as the table's type parameter
  def *  = (id, name, street,next_id) <> (Person.tupled, Person.unapply)
}



object DBLinkedList {
  def insert (db:Database)= ???

  def delete = ???

  def swap = ???
}


class TestSlick() extends ActivityInstrumentationTestCase2[MainActivity]("com.mypackage.test", classOf[MainActivity])
{
  val persons: TableQuery[PersonTable] = TableQuery[PersonTable]

  val createSchema :DBIO[Unit]=persons.schema.create

  val addTestPerson1  = persons+=Person(1,"Bela","Vaci utca 42",2)

  val addTestPerson2  = persons+=Person(2,"Geza","Vaci utca 43",0)


  val setupAction:DBIO[Unit]= DBIO.seq(createSchema, addTestPerson1)

  lazy val fn=this.getInstrumentation().getContext().getFilesDir +
    "/slick-sandbox.txt"

  lazy val db = Database.forURL("jdbc:sqlite:" + fn,
    driver = "org.sqldroid.SQLDroidDriver")

  def test1()
  {

    println("test slick 1")

    new File(fn).delete()

    println("fileName : "+fn)

    val setupFuture: Future[Unit] = db.run(setupAction)

    import scala.concurrent.{Future, Await}

    Await.result(setupFuture,Duration.Inf)

    val names=        persons.map(_.name).result

    def nameAction: Future[Seq[String]] =  db.run(names)

    def printNames: Future[Seq[String]] => Future[Unit] = _.map(_.map(println))

    print("test slick 1 - after ")

    println("db = "+db)

    println("Names : " )

    Await.result(printNames(nameAction),Duration.Inf)

    exec(db,addTestPerson2)

    println("Names : " )

    Await.result(printNames(nameAction),Duration.Inf)

    db.close()

    lazy val db2 = Database.forURL("jdbc:sqlite:" + fn,
      driver = "org.sqldroid.SQLDroidDriver")

    println("Names again : ")

    exec(db2, names)

    val res: Seq[Person] =exec(db2,persons.result)
    println("Persons  : ")

    println(res)
    //need a list of persons

   // val pr=Person.prevMap

  //  println("prevMap of " + pr)

    db2.close()
  }

  @Test def test2()
  {
    println("test slick 2")
    System.out.println("test slick 2 - context=" +
      this.getInstrumentation().getContext())
  }

}
