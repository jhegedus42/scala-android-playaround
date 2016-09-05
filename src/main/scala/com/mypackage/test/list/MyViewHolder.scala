package com.mypackage.test.list

import android.content.{Context, Intent}
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.{ImageView, TextView, Toast}
import com.mypackage.test.helper.ItemTouchHelperViewHolder
import com.mypackage.test.{DisplayMessageActivity, MainActivity, R}

class MyViewHolder(val view: View, val c:Context,val mainActivity: MainActivity) extends RecyclerView.ViewHolder(view)
  with ItemTouchHelperViewHolder with View.OnClickListener {
  lazy val tv_country = view.findViewById(R.id.tv_country).asInstanceOf[TextView]
  lazy val handleView = itemView.findViewById(R.id.handle).asInstanceOf[ImageView]
  lazy val thumbnailView = itemView.findViewById(R.id.thumbnail).asInstanceOf[ImageView]

  var line:Line=null



  override def onClick(v:View) {

    Toast.makeText(c,"The Item Clicked is: "+getPosition(),Toast.LENGTH_SHORT).show();
    println("clicked")

    val intent = new Intent(c, classOf[DisplayMessageActivity]);
    val s:String = (line.serialize)
    intent.putExtra("line_as_json",s );

    mainActivity.startActivityForResult(intent,MainActivity.CHANGE_TEXT);
  }
  def onItemSelected() {
  }

  def onItemClear() {
  }
}