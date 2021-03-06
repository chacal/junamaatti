package fi.jihartik.androidtest

import android.content.Context
import android.app.{AlertDialog, ProgressDialog}


trait ProgressDialogs extends Context {
  def withProgress(message: String)(work : => Unit) {
    val progress = ProgressDialog.show(this, "", message, true)
    AsyncTask(work, progress.dismiss)
  }

  def withProgressAndResult[T](message: String)(work : => T)(callback: (T) => Unit) {
    val progress = ProgressDialog.show(this, "", message, true)
    AsyncTask(work, { result : T =>
      progress.dismiss
      callback(result)
    })
  }

  def showMessage(message: String) {
    val builder = new AlertDialog.Builder(this).setMessage(message)
    builder.setNeutralButton("OK", null).show
  }  
}