package com.mypackage.test

import java.util

import collection.JavaConverters._
import android.app.Activity
import android.content.{Context, Intent}
import android.os.Bundle
import android.support.v7.widget.{LinearLayoutManager, RecyclerView}
import android.support.v7.widget.RecyclerView.{Adapter, LayoutManager}
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import com.mypackage.test.helper.SimpleItemTouchHelperCallback


class MainActivity extends Activity with TypedFindView {
    lazy val textview = findView(TR.text)
    lazy val mRecyclerView = findView(TR.my_recycler_view);
    lazy val mLayoutManager = new LinearLayoutManager(this);

    lazy val mAdapter : DataAdapter = new DataAdapter((1 to 50).map(_.toString).toList, this.getApplicationContext,this );


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


    override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Unit =
    {
        println("we returned with requestCode: "+requestCode+" and resultcode: "+resultCode)
        val b =data.getStringExtra("newString")
        val idx =data.getIntExtra("index",-1)

        println("new string : "+b)
        println("index : "+idx)
        mAdapter.updateItem(idx,b)

    }
}

class DisplayMessageActivity extends Activity with TypedFindView {
    lazy val textview = findView(TR.editText)
    lazy val button = findView(TR.button)

    lazy val mLayoutManager = new LinearLayoutManager(this);



    /** Called when the activity is first created. */
    override def onCreate(savedInstanceState: Bundle): Unit = {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.message)
        val intent = getIntent();
        val message = intent.getStringExtra("message");
        val idx = intent.getIntExtra("index",-1);

        textview.setText(""+message)

        //todo return with user edited text, update the model with it
        //todo add text editor field

        button.setOnClickListener(new View.OnClickListener {
            override def onClick(view: View): Unit = {
                val i = new Intent()
                i.putExtra("newString",textview.getText.toString)
          //      i.putExtra("newString",textview.getText.toString)
                i.putExtra("index",idx)
                setResult(42,i)
                finish()
            }
        })


    }

}

