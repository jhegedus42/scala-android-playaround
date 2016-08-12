package com.mypackage.test

import android.app.Activity
import android.os.Bundle

class MainActivity extends Activity with TypedFindView {
    lazy val textview = findView(TR.text)

    /** Called when the activity is first created. */
    override def onCreate(savedInstanceState: Bundle): Unit = {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        textview.setText("Hello world, from test")
        textview.setText("Hello world, from test 42")
    
    }
}