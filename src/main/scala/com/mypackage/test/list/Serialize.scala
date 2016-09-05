package com.mypackage.test.list

import java.io.{BufferedReader, FileReader, IOException}

import android.content.Context
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

import scala.reflect.ClassTag

/**
  * Created by joco on 28/08/16.
  */

object Serialize {

  def myClassOf[T:ClassTag] = implicitly[ClassTag[T]].runtimeClass

  def serializeLS(ls:List[String]): String =  ls.asJson.noSpaces

  def serializeLL(ls:List[Line]): String =  ls.asJson.noSpaces



  def loadListOfLinesFromFile(fileName:String, context: Context):Option[List[Line]]= {
    try {
      print("dir:"+context.getApplicationInfo().dataDir)
      val br = new BufferedReader(new FileReader("/data/data/com.mypackage.test/files/"+fileName));
      val text = new StringBuilder();
      var line:String=""
      import scala.util.control.Breaks._
      breakable {
        while (true) {
          line = br.readLine()
          if (line == null) break;
          text.append(line);
          text.append('\n');
        }
      }

      br.close();
      val s=text.toString()
      val r= decode[List[Line]](s)
        r.toOption
    }
      catch {
        case e: IOException => {
        }
          println("io ex:"+e.toString)
          None
      }

  }


}
