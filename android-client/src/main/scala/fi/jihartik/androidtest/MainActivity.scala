package fi.jihartik.androidtest

import android.os.Bundle
import android.view.{LayoutInflater, ViewGroup, View, Window}
import scala.collection.jcl.Conversions._
import android.widget._
import android.app.{AlertDialog, ListActivity}
import android.location.{Address, Geocoder, Location, LocationManager}
import android.content.{DialogInterface, Context}
import java.util.Locale
import scala.None

class MainActivity extends ListActivity with HttpUtils with ProgressDialogs with LocationUtils with NullHandling {

  var timetableParser = new TimetableParser

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.main)
    setListAdapter(new TimetableListAdapter(this, R.id.station_name))

    loadTimetableOrShowCached
  }

  def loadTimetableOrShowCached {
    nullOption(getLastNonConfigurationInstance.asInstanceOf[TimetableParser]) match {
      case Some(parser) if(parser.getCachedData.isDefined) => {
        timetableParser = parser
        renderTimetable(parser.getCachedData.get)
      }
      case _ => loadAndShowTimetableForCurrentLocation
    }
  }

  override def onRetainNonConfigurationInstance = timetableParser

  def loadAndShowTimetableForCurrentLocation {
    withProgressAndResult("Getting location...") {
      getCurrentAddress
    } { address =>
      address match {
        case Some(a) => loadAndShowTimetable(a)
        case _ => showMessage("Location not available.")
      }
    }
  }

  def loadAndShowTimetable(addr: Address) {
    withProgressAndResult("Loading timetable...") {
      val data = httpGet("http://www.omatlahdot.fi/omatlahdot/web?stopid=" + timetableParser.resolveStopId(addr) + "&command=quicksearch&view=mobile")
      timetableParser.parse(data)
    } { table =>
      table match {
        case Some(t) => renderTimetable(t)
        case None => showMessage("Timetable not available.")
      }
    }
  }

  def renderTimetable(table: Timetable) {
    val stationText = findViewById(R.id.station_name).asInstanceOf[TextView]
    stationText.setText(table.station.name)
    stationText.setVisibility(View.VISIBLE)
    val adapter = getListAdapter.asInstanceOf[TimetableListAdapter]
    table.rows.foreach(adapter.add)
  }
}

class TimetableListAdapter(ctx: Context, textViewResource: Int)
        extends ArrayAdapter[TimetableRow](ctx, textViewResource) with NullHandling {

  override def getView(position: Int, convertView: View, parent: ViewGroup) = {
    def inflateLayoutFromXml = LayoutInflater.from(ctx).inflate(R.layout.timetable_row_item, parent, false)

    val layout = nullOption(convertView).getOrElse(inflateLayoutFromXml).asInstanceOf[TableLayout]
    layout.findViewById(R.id.train).asInstanceOf[TextView].setText(getItem(position).train)
    layout.findViewById(R.id.time).asInstanceOf[TextView].setText(getItem(position).time)
    layout.findViewById(R.id.destination).asInstanceOf[TextView].setText(getItem(position).destination)
    layout
  }
}

trait NullHandling {
  def nullOption[T](value: T) = if(value != null) Some(value) else None
}

trait LocationUtils extends Context {
  def getCurrentAddress = {
    try {
      val loc = getSystemService(Context.LOCATION_SERVICE).asInstanceOf[LocationManager].getLastKnownLocation("network")
      new Geocoder(this).getFromLocation(loc.getLatitude, loc.getLongitude, 1).toList.firstOption
    } catch {
      case _ => None
    }
  }
}