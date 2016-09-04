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
import android.os.Bundle
import android.support.v7.widget.{LinearLayoutManager, RecyclerView}
import android.support.v7.widget.RecyclerView.{Adapter, LayoutManager}
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.view.View.OnClickListener
import com.mypackage.test.helper.{RealPathUtil, SimpleItemTouchHelperCallback, Utils}
import com.squareup.picasso.Picasso

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

    println(Experiment.Test.l.uuid)
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

object DisplayMessageActivity{
  val SELECT_PHOTO = 100;

}
class DisplayMessageActivity extends Activity with TypedFindView {
    lazy val textview = findView(TR.editText)
    lazy val uri_value = findView(TR.uri_value)

    lazy val button = findView(TR.button)
    lazy val sel_img_btn = findView(TR.select_img)
    lazy val mLayoutManager = new LinearLayoutManager(this);
    var item : Line=null
  import DisplayMessageActivity._

  override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Unit = {
    requestCode match {

      case SELECT_PHOTO => {
        val selectedImage = data.getData();
        val p = selectedImage
        val rp=RealPathUtil.getRealPathFromURI_API11to18(getApplicationContext,p)
        uri_value.setText(rp)
        val d =this.getApplicationContext().getFilesDir().getParent()
        val f= new File(d+"/files/img_"+item.uuid)
        Utils.copyFile(new File(rp),f)
        Picasso.`with`(getBaseContext()).invalidate(f);

      }
    }
  }
    /** Called when the activity is first created. */
    override def onCreate(savedInstanceState: Bundle): Unit = {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.message)
        val intent = getIntent();
        item = Line.decodeFromIntent(intent)
        textview.setText(""+item.title)

        //todo return with user edited text, update the model with it
        //todo add text editor field

        button.setOnClickListener(new View.OnClickListener {
            override def onClick(view: View): Unit = {
                val i = new Intent()
                val return_item=item.copy(title=textview.getText.toString)
                i.putExtra(Line.as_json,return_item.serialize)
                setResult(MainActivity.CHANGE_TEXT,i)
                finish()
            }
        })



      sel_img_btn.setOnClickListener(new OnClickListener {
        override def onClick(view: View): Unit = {
          val photoPickerIntent = new Intent(Intent.ACTION_PICK);
          photoPickerIntent.setType("image/*");
          startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        }
      })


    }

}

