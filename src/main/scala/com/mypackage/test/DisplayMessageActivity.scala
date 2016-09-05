package com.mypackage.test

import java.io.File

import android.R
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.View.OnClickListener
import android.widget.{ImageView, Toast}
import com.mypackage.test.helper.{RealPathUtil, Utils}
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.{CropImage, CropImageView}
import Helper.uri2Path
import Helper.fullPath
import com.mypackage.test.helper.Utils._
import com.mypackage.test.list.Line

object DisplayMessageActivity{
  val SELECT_PHOTO = 100;

}

class DisplayMessageActivity extends Activity with TypedFindView {
  lazy val textview = findView(TR.editText)
  lazy val uri_value = findView(TR.uri_value)
  lazy implicit val  c=getApplicationContext
  lazy val button = findView(TR.button)
  lazy val sel_img_btn = findView(TR.select_img)
  lazy val crop_img_btn = findView(TR.crop)

  lazy val mLayoutManager = new LinearLayoutManager(this);
  var item : Line=null
  import DisplayMessageActivity._

  override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Unit = {
    println("matching")
    requestCode match {

      case SELECT_PHOTO => {
        val selectedImage = data.getData();
        val sourceFileName=uri2Path(selectedImage)
        uri_value.setText(sourceFileName)
        val targetFileName: String =fullPath( item.getImgFileName )
        copyFile(new File(sourceFileName),new File(targetFileName))
        Picasso.`with`(getBaseContext()).invalidate(targetFileName);

      }
      case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE => {
        val result = CropImage.getActivityResult(data);

        if (resultCode == Activity.RESULT_OK) {
          Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
          val u=result.getUri()
          val sourceFileName=uri2Path(u)
          val targetFileName=fullPath(item.getCroppedImgFileName)
          copyFile(new File(sourceFileName),new File(targetFileName))

          println("cropped image:"+targetFileName)
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
          val e =result.getError()
          Toast.makeText(this, "Cropping failed: " + e, Toast.LENGTH_LONG).show();
        }
      }
    }
  }

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.editcard)
    val intent = getIntent();
    item = Line.decodeFromIntent(intent)
    textview.setText(""+item.title)

    button.setOnClickListener(new View.OnClickListener {
      override def onClick(view: View): Unit = {
        val i = new Intent()
        val return_item=item.copy(title=textview.getText.toString)
        i.putExtra(Line.as_json,return_item.serialize)
        setResult(MainActivity.CHANGE_TEXT,i)
        finish()
      }
    })

    crop_img_btn.setOnClickListener(new OnClickListener {
      override def onClick(view: View): Unit = {
        println("cropped")
        val d =DisplayMessageActivity.this.getApplicationContext().getFilesDir().getParent()
    //    val f= new File(d+"/files/"+item.getImgFileName)
        val u=Uri.parse("file://" + d+"/files/"+item.getImgFileName)
        startCropImageActivity(u)
      }

      private def startCropImageActivity(imageUri: Uri) {
        val a=CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON).setMultiTouchEnabled(true)
        a.start(DisplayMessageActivity.this)
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
