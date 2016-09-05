package com.mypackage.test

import java.io.File
import java.util

import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

import collection.JavaConverters._
import android.app.Activity
import android.content.{Context, Intent}
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.{LinearLayoutManager, RecyclerView}
import android.support.v7.widget.RecyclerView.{Adapter, LayoutManager}
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.view.View.OnClickListener
import com.mypackage.test.helper.{RealPathUtil, SimpleItemTouchHelperCallback, Utils}
import com.mypackage.test.list.{DataAdapter, Line}
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.{CropImage, CropImageView}

object MainActivity {
  val CHANGE_TEXT = 200;


}

class MainActivity extends Activity with TypedFindView {
  lazy val textview = findView(TR.text)
  lazy val mRecyclerView = findView(TR.my_recycler_view);
  lazy val mLayoutManager = new LinearLayoutManager(this);

  lazy val mAdapter: DataAdapter = new DataAdapter((1 to 50).map(x => Line(x.toString, "")).toList, this.getApplicationContext, this);


  /** Called when the activity is first created. */
  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)
    textview.setText("Hello world, from test")
    textview.setText("Hello world, from test 42")


    // use this setting to improve performance if you know that changes
    // in content do not change the layout size of the RecyclerView
    mRecyclerView.setHasFixedSize(true);

    // use a linear layout manager
    mRecyclerView.setLayoutManager(mLayoutManager);


    val callback = new SimpleItemTouchHelperCallback(mAdapter);
    val mItemTouchHelper = new ItemTouchHelper(callback);
    mAdapter.setIth(mItemTouchHelper)

    mRecyclerView.setAdapter(mAdapter);

    mItemTouchHelper.attachToRecyclerView(mRecyclerView);

  }


  override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Unit = {
    requestCode match {
      case MainActivity.CHANGE_TEXT => {
        println("we returned with requestCode: " + requestCode + " and resultcode: " + resultCode)
        val item = Line.decodeFromIntent(data)


        println("item : " + item)
        mAdapter.updateItem(item)
      }

    }
  }


}


