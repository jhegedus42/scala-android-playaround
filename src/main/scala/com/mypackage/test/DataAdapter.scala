package com.mypackage.test

import android.support.v4.view.MotionEventCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.mypackage.test.helper.ItemTouchHelperAdapter

import android.content.Context

import scala.collection.mutable

 class DataAdapter(val initData:List[String],val c:Context,val ma:MainActivity) extends RecyclerView.Adapter[MyViewHolder] with ItemTouchHelperAdapter {

  lazy val l :mutable.ListBuffer[String]= mutable.ListBuffer()

  def saveState(): Unit ={
    val s= Serialize.serialize(l.toList)
    println(s)
    Serialize.saveToFile(s,"list.json",c)
  }

   lazy val lo=Serialize.loadListOfStringsFromFile("list.json",c)
   lo match {
     case Some(x) =>   { setDataOnInit(x)
     println("list read in:"+x.toString())
     }

     case None =>   {
       println("it was a none")
       setDataOnInit(initData)
     }
   }

  def setIth(ith: ItemTouchHelper) {
    this.ith = ith
  }

  private var ith: ItemTouchHelper = null

  private  def setDataOnInit(ll:List[String])={
    l.clear()
    l.appendAll(ll)
  }

  def onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder = {
    val view: View = LayoutInflater.from(viewGroup.getContext).inflate(R.layout.cardview, viewGroup, false)
    val vh=new MyViewHolder(view,c,ma)
    view.setOnClickListener(vh)
    vh.pos=i
    return  vh
  }

  def onBindViewHolder(viewHolder: MyViewHolder, i: Int) {
    viewHolder.tv_country.setText(l(i))

    viewHolder.pos=i
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

   def updateItem(pos:Int,str:String)={
     l(pos)=str
     notifyItemChanged(pos)
     saveState()
   }

  def getItemCount: Int = {
    return l.size
  }

  def onItemMove(fromPosition: Int, toPosition: Int) {
    println("before move")
    saveState()
    val prev: String = l.remove(fromPosition)
    println("after removing PINAAAA")
    saveState()
    val insertAtPos=if (toPosition > fromPosition) toPosition -1 else toPosition
    val insertAtPos2=toPosition

    l.insert(insertAtPos2, prev)
    println("insertAtPos:"+insertAtPos)
    println("insertAtPos2:"+insertAtPos2)

    notifyItemMoved(fromPosition, toPosition)
    saveState()
  }

  def onItemDismiss(position: Int) {
    l.remove(position)
    notifyItemRemoved(position)
  }

}