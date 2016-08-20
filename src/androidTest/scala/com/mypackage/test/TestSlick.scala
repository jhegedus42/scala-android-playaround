package com.mypackage.test

import java.io.File
import _root_.slick.driver.SQLiteDriver.backend.DatabaseDef
import android.support.test.runner.AndroidJUnit4

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import slick.driver.SQLiteDriver.api._
import android.test.{ActivityInstrumentationTestCase2, InstrumentationTestCase}
import org.junit.runner.RunWith
import org.junit.{Before, Test}
import org.scalatest.junit.AssertionsForJUnit
import slick.lifted.{Query => _, Rep => _, TableQuery => _, Tag => _, _}

import scala.collection.immutable.HashMap
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import junit.framework.Assert._

object Helper {
  def exec[T](db:Database, action: DBIO[T]): T = Await.result(db.run(action), Duration.Inf)
}

import Helper._

object PersonHelper{
  val persons=TableQuery[PersonTable]


  def orderedList(l:List[Person]):List[Person]= {
    def findFirst : List[Person]=>Person = lp => {
      def prevMap : (List[Person]) => Map[Int,Person] = (lp) => {

        lp.filter(_.next_id>0).foldLeft(HashMap.empty[Int,Person]) ( (hm,p:Person)=> hm.+( (p.next_id, p)) )

      }
      val m=prevMap(lp)
      val guess=lp.head
      def go(l:Set[Person],g:Person) : Person= {
        m.get(g.id) match {
          case Some(g2) => go(l.-(g),g2)
          case None => g
        }
      }
      go(lp.toSet,guess)
    }
    def getPersonWithID : (List[Person],Int)=>Option[Person] = (ps,id)=>ps.filter(_.id==id).headOption
    val f=findFirst(l)
    val res=scala.collection.mutable.ListBuffer[Person]()
    var next_id=f.next_id
    res+=f
    while (next_id > 0){
      val np=getPersonWithID(l,next_id).head
      res+=np
      next_id=np.next_id
    }
    res.toList
  }



  def insertAfter(target:Person,inserted:Person)={
    val insert  = persons returning persons.map(_.id)+=(inserted.copy(next_id = target.next_id))

    def update (inserted_id:Int)=
      persons.filter(_.id===target.id).map(_.next_id).update(inserted_id)


    insert.flatMap { id=>update(id) }
  }
}

case class Person( name: String, street: String, next_id:Int, id: Int = 0)

final class PersonTable(tag: Tag) extends Table[Person](tag, "PERSONS") {
  // This is the primary key column:
  def id: Rep[Int] = column[Int]("PER_ID", O.PrimaryKey, O.AutoInc)
  def next_id: Rep[Int] = column[Int]("NEXT_ID")
  def name: Rep[String] = column[String]("PER_NAME")
  def street: Rep[String] = column[String]("STREET")

  // def next_Person = foreignKey("SUP_FK", next_id, TableQuery[PersonTable])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
  // def isFirst: Rep[Boolean] =column[Boolean]("IS_FIRST")

  // Every table needs a * projection with the same type as the table's type parameter

  def *  = (name, street,next_id,id) <> (Person.tupled, Person.unapply)
}

class TestSlick () extends ActivityInstrumentationTestCase2[MainActivity]("com.mypackage.test", classOf[MainActivity]) with AssertionsForJUnit {
  lazy val fn=this.getInstrumentation().getContext().getFilesDir + "/slick-sandbox.txt"

  object TestStuff {
    val persons: TableQuery[PersonTable] = TableQuery[PersonTable]

    val createSchema: DBIO[Unit] = persons.schema.create

    val p1 = Person("Bela", "Vaci utca 42", -1)     //  1
    val p2 = Person("Geza", "Lemberg utca 155", 1)  //  2
    val p3 = Person("Jani", "Rock Road 12", 2)      //  3
    val p4 = Person("Gerzson","Gisonenweg 2",-1)

    val addTestPerson1 = persons += p1
    val addTestPerson2 = persons += p2
    val addTestPerson3 = persons += p3

    val setupAction: DBIO[Unit] = DBIO.seq(createSchema, addTestPerson1)

    val names = persons.map(_.name).result
    def nameAction: Future[Seq[String]] = db.run(names)
    def printNames: Future[Seq[String]] => Future[Unit] = _.map(_.map(println))

    lazy val db = Database.forURL("jdbc:sqlite:" + fn, driver = "org.sqldroid.SQLDroidDriver")

    def runInDBContext(fn: String, run: DatabaseDef => Unit ) = {
      lazy val db2: DatabaseDef = Database.forURL("jdbc:sqlite:" + fn, driver = "org.sqldroid.SQLDroidDriver")
      run(db2)
      db2.close()

    }
  }

  import TestStuff._

  override def setUp(): Unit ={
    println("42 beforr - seti   47it up")

    new File(fn).delete()

    println("fileName deleted: "+fn)

    val setupFuture: Future[Unit] = db.run(setupAction)

    import scala.concurrent.{Future, Await}

    Await.result(setupFuture,Duration.Inf)
    print("test slick 1 - after ")

    println("db = "+db)

    println("Names : " )

    Await.result(printNames(nameAction),Duration.Inf)

    exec(db,addTestPerson2)
    exec(db,addTestPerson3)

    println("Names : " )
    Await.result(printNames(nameAction),Duration.Inf)


    val ia= PersonHelper.insertAfter(p2,p4)
    //   assertPersonOrder(db2,List(3,2,1))
    exec(db,ia)

    println("Names (after insert): " )
    Await.result(printNames(nameAction),Duration.Inf)


    db.close()
  }

  def order() {
    runInDBContext(fn, db=> {

      assertPersonOrder(db, List(3, 2, 1))
    })
  }

  def update() {
    System.out.println("test slick 2 - context=" + this.getInstrumentation().getContext())
    runInDBContext(fn,  db2 => {
      val p3: Person = exec(db2, persons.filter(_.id===3).result).head
      assertEquals(p3.name,"Jani")
      println("p3.name="+p3.name)
      exec(db2, persons.filter(_.id===3).map(_.name).update("Jaanus"))
    })

    runInDBContext(fn,  db2 => {
      val p3: Person = exec(db2, persons.filter(_.id===3).result).head
      assertEquals(p3.name,"Jaanus")
      println("p3.name="+p3.name)
   //   exec(db2, persons.filter(_.id===3).map(_.name).update("Jaanus"))
    })

  }

  def assertPersonOrder(db: DatabaseDef,l:List[Int]): Unit ={

      val res: Seq[Person] = exec(db, persons.result)

      println("Persons  : ")

      println(res)
      val l1 = res.toList
//      val ol = PersonHelper.orderedList(res.toList)
//      assertEquals(ol.map(_.id), l)
  }

  def insertAfter: Unit = {
    runInDBContext(fn,  db2 => {
      val p2:  Person = exec(db2, persons.filter(_.id===2).result).head
      val ia   = PersonHelper.insertAfter(p2,p4)
      //   assertPersonOrder(db2,List(3,2,1))
      exec(db2,ia)
      assertPersonOrder(db2,List(3,2,4,1))

      //   assertEquals(p3.name,"Jaanus")

      println("p3.name="+p3.name)
    })


  }


}
