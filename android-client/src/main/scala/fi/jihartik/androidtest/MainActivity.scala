package fi.jihartik.androidtest

import android.os.Bundle
import android.app.{ProgressDialog, Activity}
import android.widget.TextView

class MainActivity extends Activity with HttpUtils with ProgressDialogs {
  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)

    withProgressAndResult("Loading timetable...") {
      val data = httpGet("http://www.omatlahdot.fi/omatlahdot/web?stopid=E1058&command=quicksearch&view=mobile")
      new TimetableParser().parse(data)
    } { table =>
      findViewById(R.id.station_name).asInstanceOf[TextView].setText(table.station.name)
    }
  }
}
