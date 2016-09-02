package com.mypackage.test

import java.io.File

import android.support.v4.view.MotionEventCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.mypackage.test.helper.{ItemTouchHelperAdapter, Utils}
import android.content.{Context, Intent}
import com.squareup.picasso.Picasso
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import io.circe.Decoder

object Helper {
  def uuid = java.util.UUID.randomUUID.toString
}

trait HasUUID {
  val uuid: String = Helper.uuid
}

case class Line(title: String, text: String,  uuid: String) {
  def serialize = this.asJson.noSpaces
}

object Line {
  val as_json = "line_as_json"

  def decodeFromIntent(i: Intent) = decode[Line](i.getStringExtra(Line.as_json)).toOption.get

  def apply(title: String, text: String): Line = new Line(title, text, Helper.uuid)
}

object Experiment {

  object Test {
    val l = Line("bla", "bal")
  }

}

import scala.collection.mutable

class DataAdapter(val initData: List[Line], val c: Context, val ma: MainActivity) extends RecyclerView.Adapter[MyViewHolder] with ItemTouchHelperAdapter {
  lazy val l: mutable.ListBuffer[Line] = mutable.ListBuffer()

  def saveState(): Unit = {
    val s = Serialize.serializeLL(l.toList)
    println(s)
    Serialize.saveToFile(s, "list.json", c)
  }

  import io.circe._, io.circe.generic.semiauto._

  lazy val lo: Option[List[Line]] = Serialize.loadListOfLinesFromFile("list.json", c)
  lo match {
    case Some(x) => {
      setDataOnInit(x)
      println("list read in:" + x.toString())
    }

    case None => {
      println("it was a none")
      setDataOnInit(initData)
    }
  }

  def setIth(ith: ItemTouchHelper) {
    this.ith = ith
  }

  private var ith: ItemTouchHelper = null

  private def setDataOnInit(ll: List[Line]) = {
    l.clear()
    l.appendAll(ll)
  }

  def onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder = {
    val view: View = LayoutInflater.from(viewGroup.getContext).inflate(R.layout.cardview, viewGroup, false)
    val vh = new MyViewHolder(view, c, ma)
    view.setOnClickListener(vh)
    return vh
  }

  def onBindViewHolder(viewHolder: MyViewHolder, i: Int) {
    viewHolder.tv_country.setText(l(i).title)
    viewHolder.line = l(i)
    val cc=viewHolder.thumbnailView.getContext
    val d =c.getFilesDir().getParent()
    val f =new File(d+"/files/img_"+viewHolder.line.uuid)
    Picasso.`with`(cc).load(f).into(viewHolder.thumbnailView);
  //  viewHolder.thumbnailView.setImageResource(TR.drawable.ic_reorder_grey_500_24dp.resid)

    //viewHolder.thumbnailView.setImageURI()


    // Start a drag whenever the handle view is touched
    viewHolder.handleView.setOnTouchListener(new View.OnTouchListener() {
      def onTouch(v: View, event: MotionEvent): Boolean = {
        if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
          ith.startDrag(viewHolder)
        }
        return false
      }
    })
  }

  def updateItem(ln: Line) = {
    val pos = l.map(_.uuid).indexOf(ln.uuid)
    l(pos) = ln
    notifyItemChanged(pos)
    saveState()
  }

  def getItemCount: Int = {
    return l.size
  }

  def onItemMove(fromPosition: Int, toPosition: Int) {
    println("before move")
    saveState()
    val prev: Line = l.remove(fromPosition)
    println("after removing PINAAAA")
    saveState()
    val insertAtPos = if (toPosition > fromPosition) toPosition - 1 else toPosition
    val insertAtPos2 = toPosition

    l.insert(insertAtPos2, prev)
    println("insertAtPos:" + insertAtPos)
    println("insertAtPos2:" + insertAtPos2)

    notifyItemMoved(fromPosition, toPosition)
    saveState()
  }

  def onItemDismiss(position: Int) {
    l.remove(position)
    notifyItemRemoved(position)
  }

}