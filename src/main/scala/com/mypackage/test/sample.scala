package com.mypackage.test

import java.util

import collection.JavaConverters._
import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.{LinearLayoutManager, RecyclerView}
import android.support.v7.widget.RecyclerView.{Adapter, LayoutManager}

class MainActivity extends Activity with TypedFindView {
    lazy val textview = findView(TR.text)
    lazy val  mRecyclerView = findView(TR.my_recycler_view);
    lazy val mAdapter = new DataAdapter(new util.ArrayList(List("bla","basl").asJava));
    lazy val mLayoutManager = new LinearLayoutManager(this);




    /** Called when the activity is first created. */
    override def onCreate(savedInstanceState: Bundle): Unit = {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        textview.setText("Hello world, from test")
        textview.setText("Hello world, from test 43")


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mAdapter);


    }
}

