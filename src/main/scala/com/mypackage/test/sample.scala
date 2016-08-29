package com.mypackage.test

import java.util

import collection.JavaConverters._
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.{LinearLayoutManager, RecyclerView}
import android.support.v7.widget.RecyclerView.{Adapter, LayoutManager}
import android.support.v7.widget.helper.ItemTouchHelper
import com.mypackage.test.helper.SimpleItemTouchHelperCallback


class MainActivity extends Activity with TypedFindView {
    lazy val textview = findView(TR.text)
    lazy val mRecyclerView = findView(TR.my_recycler_view);
    lazy val mLayoutManager = new LinearLayoutManager(this);



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

        val mAdapter : DataAdapter = new DataAdapter((1 to 50).map(_.toString).toList, this.getApplicationContext );

        val callback = new SimpleItemTouchHelperCallback(mAdapter);
        val mItemTouchHelper = new ItemTouchHelper(callback);
        mAdapter.setIth(mItemTouchHelper)

        mRecyclerView.setAdapter(mAdapter);

        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

    }
}

