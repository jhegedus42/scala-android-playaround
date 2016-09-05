package com.mypackage.test

import java.io.{File, IOException}

import android.content.Context
import android.net.Uri
import com.mypackage.test.helper.RealPathUtil

/**
  * Created by joco on 05/09/16.
  */
object Helper {
  def uuid = java.util.UUID.randomUUID.toString


  def saveToFile(s:String, fileName:String, context: Context): Unit = {
    try {
      val outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
      outputStream.write(s.getBytes());
      outputStream.close();
    } catch {
      case ex: IOException => {
        println("IO Exception")
      }
    }
  }

  def uri2Path(p:Uri)(implicit c:Context)=RealPathUtil.getRealPathFromURI_API11to18(c,p)

  def fullPath(p:String)(implicit c:Context):String = c.getFilesDir().getParent()+"/files/"+p


}
