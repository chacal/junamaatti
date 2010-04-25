package fi.jihartik.androidtest

import android.app.ProgressDialog


trait ProgressDialogs {
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
}