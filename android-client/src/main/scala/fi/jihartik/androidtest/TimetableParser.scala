package fi.jihartik.androidtest

import util.matching.Regex
import java.util.Date
import android.location.Address

class TimetableParser {

  private var cachedData : Option[Timetable] = None

  def parse(input: String) = {
    val stationName = stationRegex.findFirstMatchIn(input).map(_.group(1)).getOrElse("N/A")
    val rows = rowRegex.findAllIn(input).matchData.map(m => TimetableRow(m.group(2), m.group(1), m.group(3))).toList
    cachedData = Some(Timetable(Station(stationName), rows))
    cachedData
  }

  def getCachedData = cachedData

  val stationRegex = new Regex("""span class="departures_title">(.*)</span>""")
  val rowRegex = new Regex("""(?s)<tr class="departures_row.*?text'>(.*?) e</td>.*?text'>(.*?)</td>.*?text'>(.*?)</td>.*?</tr>""")

  def resolveStopId(addr: Address) = {
    addr.getLocality.contains("Espoo") match {
      case true => "E1058"  // Leppävaara/VR
      case false => "0070"  // Helsinki/VR
    }
  }
}


case class Station(name: String) {
  override def toString = name
}
case class TimetableRow(train: String, time: String, destination: String)

case class Timetable(station: Station, rows: List[TimetableRow])