package fi.jihartik.androidtest

import android.os.Bundle
import android.app.{ProgressDialog, Activity}

class MainActivity extends Activity with HttpUtils {
  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)

    withProgress("Loading timetable...") {
      val data = httpGet("http://www.omatlahdot.fi/omatlahdot/web?stopid=E1058&command=quicksearch&view=mobile")
      val timeTable = new TimetableParser().parse(data)
      println(timeTable)
    }
  }

  def withProgress(message: String)(work : => Unit) {
    val progress = ProgressDialog.show(this, "", message, true)
    AsyncTask(work, progress.dismiss)
  }
}
