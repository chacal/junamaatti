package fi.jihartik.androidtest

import android.os.Bundle
import android.app.ListActivity
import android.content.Context
import android.view.{LayoutInflater, ViewGroup, View, Window}
import android.widget.{TableLayout, ListView, ArrayAdapter, TextView}

class MainActivity extends ListActivity with HttpUtils with ProgressDialogs {

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.main)

    setListAdapter(new TimetableListAdapter(this, R.id.station_name))

    withProgressAndResult("Loading timetable...") {
      val data = httpGet("http://www.omatlahdot.fi/omatlahdot/web?stopid=E1058&command=quicksearch&view=mobile")
      new TimetableParser().parse(data)
    } { table =>
      findViewById(R.id.station_name).asInstanceOf[TextView].setText(table.station.name)
      val adapter = getListAdapter.asInstanceOf[TimetableListAdapter]
      table.rows.foreach(adapter.add)
    }
  }
}

class TimetableListAdapter(ctx: Context, textViewResource: Int)
        extends ArrayAdapter[TimetableRow](ctx, textViewResource) {

  override def getView(position: Int, convertView: View, parent: ViewGroup) = {
    def inflateLayoutFromXml = LayoutInflater.from(ctx).inflate(R.layout.timetable_row_item, parent, false)

    val layout = nullOption(convertView).getOrElse(inflateLayoutFromXml).asInstanceOf[TableLayout]
    layout.findViewById(R.id.train).asInstanceOf[TextView].setText(getItem(position).train)
    layout.findViewById(R.id.time).asInstanceOf[TextView].setText(getItem(position).time)
    layout.findViewById(R.id.destination).asInstanceOf[TextView].setText(getItem(position).destination)
    layout
  }

  def nullOption[T](value: T) = if(value != null) Some(value) else None
}