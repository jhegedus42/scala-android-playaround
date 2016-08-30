package com.mypackage.test

import android.content.{Context, Intent}
import android.support.v4.view.MotionEventCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.{EditText, ImageView, TextView, Toast}
import com.mypackage.test.R
import com.mypackage.test.TR
import com.mypackage.test.helper.ItemTouchHelperAdapter
import com.mypackage.test.helper.ItemTouchHelperViewHolder
import org.w3c.dom.Text
import java.util.ArrayList

import android.view.View.inflate

class MyViewHolder(val view: View, val c:Context,val mainActivity: MainActivity) extends RecyclerView.ViewHolder(view)
  with ItemTouchHelperViewHolder with View.OnClickListener {
  lazy val tv_country = view.findViewById(R.id.tv_country).asInstanceOf[TextView]
  lazy val handleView = itemView.findViewById(R.id.handle).asInstanceOf[ImageView]
  var pos=0

  override def onClick(v:View) {

    Toast.makeText(c,"The Item Clicked is: "+getPosition(),Toast.LENGTH_SHORT).show();
    println("clicked")

    val intent = new Intent(c, classOf[DisplayMessageActivity]);
    val message = tv_country.getText;
    intent.putExtra("message", message);
    intent.putExtra("index", pos);

    mainActivity.startActivityForResult(intent,42);
  }
  def onItemSelected() {
  }

  def onItemClear() {
  }
}