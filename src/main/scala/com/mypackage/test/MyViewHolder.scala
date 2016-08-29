package com.mypackage.test

import android.content.Context
import android.support.v4.view.MotionEventCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.mypackage.test.R
import com.mypackage.test.TR
import com.mypackage.test.helper.ItemTouchHelperAdapter
import com.mypackage.test.helper.ItemTouchHelperViewHolder
import org.w3c.dom.Text
import java.util.ArrayList
import android.view.View.inflate

class MyViewHolder(val view: View) extends RecyclerView.ViewHolder(view) with ItemTouchHelperViewHolder {
  lazy val tv_country = view.findViewById(R.id.tv_country).asInstanceOf[TextView]
  lazy val handleView = itemView.findViewById(R.id.handle).asInstanceOf[ImageView]

  def onItemSelected() {
  }

  def onItemClear() {
  }
}