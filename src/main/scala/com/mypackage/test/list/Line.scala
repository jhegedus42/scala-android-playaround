package com.mypackage.test.list

import android.content.Intent
import com.mypackage.test.Helper
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

/**
  * Created by joco on 05/09/16.
  */
case class Line(title: String, text: String, uuid: String) {
  def serialize = this.asJson.noSpaces
  def getImgFileName="img_"+uuid
  def getCroppedImgFileName="cropped_img_"+uuid

}

object Line {
  val as_json = "line_as_json"

  def decodeFromIntent(i: Intent) = decode[Line](i.getStringExtra(Line.as_json)).toOption.get

  def apply(title: String, text: String): Line = new Line(title, text, Helper.uuid)


}